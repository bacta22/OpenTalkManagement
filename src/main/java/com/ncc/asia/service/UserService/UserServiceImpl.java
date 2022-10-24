package com.ncc.asia.service.UserService;

import com.ncc.asia.dto.opentalk.OpenTalkIsHosted;
import com.ncc.asia.dto.user.UserDTODisplay;
import com.ncc.asia.dto.user.UserDTOModify;
import com.ncc.asia.entity.CompanyBranch;
import com.ncc.asia.entity.OpenTalk;
import com.ncc.asia.entity.Role;
import com.ncc.asia.entity.User;
import com.ncc.asia.exception.BadInputException;
import com.ncc.asia.exception.ItemsNotFoundException;
import com.ncc.asia.mapper.ModifyUserMapper;
import com.ncc.asia.mapper.OpenTalkMapper;
import com.ncc.asia.mapper.UserMapper;
import com.ncc.asia.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final OpenTalkRepository openTalkRepository;
    private final PasswordEncoder passwordEncoder;
    private final CompanyBranchRepository companyBranchRepository;
    private final ModifyUserMapper modifyUserMapper;
    private final UserMapper userMapper;
    private final OpenTalkMapper openTalkMapper;
    private final OpenTalkIsHostedRepository openTalkIsHostedRepository;

    // CRUD
    // Find by id
    @Override
    public UserDTODisplay findById(int id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new BadInputException("Could not find any user with id : " + id);
        }
        return userMapper.entityToDto(userOptional.get());
    }

    // Find all
    @Override
    public List<UserDTODisplay> findAll() {
        return userMapper.entityListToDtoList(userRepository.findAll());
    }

    // Save
    @Override
    public UserDTODisplay save(UserDTOModify modifyUserDTO) {
        User user = modifyUserMapper.getUserForModify(modifyUserDTO);

        String passwordEncoded = passwordEncoder.encode(user.getPassword());
        user.setPassword(passwordEncoded);

        // set role
        Set<Role> roles = new HashSet<>();
        if (modifyUserDTO.getRolesId() != null && !modifyUserDTO.getRolesId().isEmpty()) {
            for (Integer roleId : modifyUserDTO.getRolesId()) {
                Optional<Role> roleOptional = roleRepository.findById(roleId);
                if (roleOptional.isEmpty()) {
                    throw new BadInputException("Can not find any role with id : " + roleId);
                }
                roles.add(roleOptional.get());
            }
        } else {
            Optional<Role> roleOptionalEmployee = roleRepository.findById(2);
            roleOptionalEmployee.ifPresent(roles::add);
        }
        user.setRoles(roles);

        // set open talk set
        Set<OpenTalk> openTalkSet = new HashSet<>();
        for (Integer openTalkId : modifyUserDTO.getJoinOpenTalkListId()) {
            Optional<OpenTalk> openTalkOptional = openTalkRepository.findById(openTalkId);
            openTalkOptional.ifPresent(openTalkSet::add);
        }
        user.setJoinOpenTalkList(openTalkSet);
        user.setNumberOfJoinedOpenTalk(openTalkSet.size());

        // set company branch
        Optional<CompanyBranch> companyBranchOptional
                = companyBranchRepository.findById(modifyUserDTO.getCompanyBranchId());
        if (companyBranchOptional.isEmpty()) {
            throw new BadInputException("Can not find any company branch with id : "
                                        +modifyUserDTO.getCompanyBranchId());
        }
        user.setCompanyBranch(companyBranchOptional.get());

        User savedUser = userRepository.save(user);
        return userMapper.entityToDto(savedUser);
    }

    // Delete by id
    @Override
    public void deleteById(int id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new ItemsNotFoundException("Could not find any user with id : " + id);
        }
        userRepository.deleteById(id);
    }

    // Find employee by some filter: enabled/disabled, branch, username
    @Override
    public User findByName(String username) {
        return userRepository.findByUsernameIs(username);
    }

    @Override
    public Page<UserDTODisplay> findUserBySomeFilter(Boolean enabled, String branch, String username,
                                                     Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo,pageSize);
        Page<User> userPage = userRepository.findUserBySomeFilter(enabled, branch, username, pageable);
        Function<User,UserDTODisplay> function = userMapper::entityToDto;
        return userPage.map(function);
    }

    // Find user joined open talk sort by ascending the number of joined open talk
    @Override
    public Page<UserDTODisplay> findUserBySortByNoOfOpenTalk(Boolean enabled, String branch, String username,
                                                             Integer pageNo, Integer pageSize) {
        Sort sortByNumberOfJoinedOpenTalk = Sort.by(Sort.Direction.ASC,"numberOfJoinedOpenTalk");
        Pageable pageable = PageRequest.of(pageNo,pageSize,sortByNumberOfJoinedOpenTalk);
        Page<User> userPage = userRepository.findUserBySortByNoOfOpenTalk(enabled,branch,username,pageable);
        Function<User,UserDTODisplay> function = userMapper::entityToDto;
        return userPage.map(function);
    }

    // Register host open talk
    @Override
    public void registerHost(Integer idHost, Integer idOpenTalk) {
        Optional<User> userOptional = userRepository.findById(idHost);
        if (userOptional.isEmpty()) {
            throw new BadInputException("Could not find any user with id: "+idHost);
        }
        Optional<OpenTalk> openTalkOptional = openTalkRepository.findById(idOpenTalk);
        if (openTalkOptional.isEmpty()) {
            throw new BadInputException("Could not find any open talk with id: "+idOpenTalk);
        }
        User user = userOptional.get();
        OpenTalk openTalk = openTalkOptional.get();
        user.registerHostOpenTalk(openTalk);

        // Set OpenTalkIsHosted for user
        // Check OpenTalkIsHosted is existed in DB or not
        OpenTalkIsHosted openTalkIsHosted = openTalkIsHostedRepository.findByIdOpenTalk(openTalk.getId());

        // Exist openTalkIsHosted, set new user host for openTalkIsHosted
        if (openTalkIsHosted !=null ) {
            openTalkIsHosted.setUser(user);
            openTalkIsHostedRepository.save(openTalkIsHosted);

        } else { // Not exist openTalkIsHosted, create new OpenTalkIsHosted, add host
            OpenTalkIsHosted newOpenTalkIsHosted = openTalkMapper.openTalkToOpenTalkHosted(openTalk);
            newOpenTalkIsHosted.setId(0);
            newOpenTalkIsHosted.setUser(user);
            openTalkIsHostedRepository.save(newOpenTalkIsHosted);
        }

        // Save user
        userRepository.save(user);
    }

    // Register joined open talk
    @Override
    public void registerJoinedOpenTalk(Integer idHost, Integer idOpenTalk) {
        Optional<User> userOptional = userRepository.findById(idHost);
        if (userOptional.isEmpty()) {
            throw new BadInputException("Could not find any user with id: "+idHost);
        }
        Optional<OpenTalk> openTalkOptional = openTalkRepository.findById(idOpenTalk);
        if (openTalkOptional.isEmpty()) {
            throw new BadInputException("Could not find any open talk with id: "+idOpenTalk);
        }
        User user = userOptional.get();
        OpenTalk openTalk = openTalkOptional.get();
        user.registerJoinedOpenTalk(openTalk);
        userRepository.save(user);
    }

    // Find user have not hosted open talk in this year, or have not hosted any open talk
    @Override
    public Page<UserDTODisplay> findUserHaveNotHostedOpenTalk(Integer pageNo, Integer pageSize) {
        List<User> users = getUsersHaveNotHostedOpenTalk();

        Pageable pageable = PageRequest.of(pageNo,pageSize);
        Page<User> userPage = new PageImpl<>(users,pageable,users.size()); // paging not working

        Function<User,UserDTODisplay> function = userMapper::entityToDto;
        return userPage.map(function);
    }
    private List<User> getUsersHaveNotHostedOpenTalk () {
        List<User> userList = userRepository.findAll();
        // user have not hosted
        List<User> userListHaveNotHosted = userList.stream()
                .filter(User::isEnabled)
                .filter(user -> user.getOpenTalkIsHosted().size()==0)
                .collect(Collectors.toList());
        // user have not hosted this year
        List<User> userListHaveNotHostedThisYear = userRepository.findByByHaveNotHostedThisYear(2022);
        Set<User> userSet = new HashSet<>(userListHaveNotHosted);
        userSet.addAll(userListHaveNotHostedThisYear);
        // merge, then sort by number of joined open talk
        return userSet.stream()
                .sorted(Comparator.comparing(User::getNumberOfJoinedOpenTalk))
                .collect(Collectors.toList());
    }

    // Find random user for host open talk
    @Override
    public UserDTODisplay findRandomUserForHost() {
        List<User> usersHaveNotHostedOpenTalk = getUsersHaveNotHostedOpenTalk();

        Function<User,UserDTODisplay> function = userMapper::entityToDto;
        List<UserDTODisplay> users = usersHaveNotHostedOpenTalk.stream()
                                        .map(function).collect(Collectors.toList());

        Random rand = new Random();
        return users.get(rand.nextInt(users.size()));
    }






}
