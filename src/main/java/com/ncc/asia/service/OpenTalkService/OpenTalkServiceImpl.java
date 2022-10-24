package com.ncc.asia.service.OpenTalkService;

import com.ncc.asia.dto.opentalk.OpenTalkDTO;
import com.ncc.asia.dto.opentalk.OpenTalkIsHosted;
import com.ncc.asia.dto.user.UserDTOForOpenTalk;
import com.ncc.asia.entity.*;
import com.ncc.asia.exception.BadInputException;
import com.ncc.asia.exception.ItemsNotFoundException;
import com.ncc.asia.mapper.OpenTalkMapper;
import com.ncc.asia.repository.CompanyBranchRepository;
import com.ncc.asia.repository.OpenTalkIsHostedRepository;
import com.ncc.asia.repository.OpenTalkRepository;
import com.ncc.asia.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OpenTalkServiceImpl implements OpenTalkService{

    private final OpenTalkRepository openTalkRepository;
    private final OpenTalkMapper openTalkMapper;
    private final UserRepository userRepository;
    private final CompanyBranchRepository branchRepository;

    private final OpenTalkIsHostedRepository openTalkIsHostedRepository;

    // CRUD
    // Find by id
    @Override
    public OpenTalkDTO findById(int id) {
        Optional<OpenTalk> optionalOpenTalk = openTalkRepository.findById(id);
        if (optionalOpenTalk.isEmpty()) {
            throw new ItemsNotFoundException("Could not find any open talk with id : " + id);
        }
        return openTalkMapper.entityToDto(optionalOpenTalk.get());
    }

    // Find all
    @Override
    public List<OpenTalkDTO> findAll() {
        List<OpenTalk> openTalks = openTalkRepository.findAll().stream()
                .sorted(Comparator.comparing(OpenTalk::getDate))
                .collect(Collectors.toList());
        return openTalkMapper.entityListToDtoList(openTalks);
    }

    // Save open talk
    @Override
    public OpenTalkDTO save(OpenTalkDTO openTalkDTO) {
        OpenTalk openTalk = openTalkMapper.dtoToEntity(openTalkDTO);
        Set<User> usersJoinOpenTalk = new HashSet<>();

        // set host from dto
        if (openTalkDTO.getHost() != null) {
            Optional<User> optionalUser = userRepository.findById(openTalkDTO.getHost().getId());
            if (optionalUser.isEmpty()) {
                throw new BadInputException("Could not find any user with id : "+openTalkDTO.getHost().getId() +
                        " for host this open talk");
            } else {
                User user = optionalUser.get();
                openTalk.setHostUser(user);

                OpenTalkIsHosted openTalkIsHosted = openTalkIsHostedRepository.findByIdOpenTalk(openTalk.getId());
                // update OpenTalk case (exist OpenTalkIsHosted), find it in DB set user again
                if (openTalkIsHosted != null) {
                    openTalkIsHosted.setUser(user);
                    openTalkIsHostedRepository.save(openTalkIsHosted);
                    // create OpenTalk case (or not exist OpenTalkIsHosted)
                    // after set idOpenTalk For OpenTalkIsHosted again, because create new OpenTalk, id = 0
                } else {
                    OpenTalkIsHosted newOpenTalkIsHosted = openTalkMapper.openTalkToOpenTalkHosted(openTalk);
                    newOpenTalkIsHosted.setId(0);
                    newOpenTalkIsHosted.setUser(user);
                    openTalkIsHostedRepository.save(newOpenTalkIsHosted);
                }

                usersJoinOpenTalk.add(user);
            }
        } else {
            openTalk.setHost(null);
        }

        // set company branch from dto
        Set<CompanyBranch> companyBranches = new HashSet<>();
        if (openTalkDTO.getCompanyBranches() != null) {
            Set<String> branchesString = openTalkDTO.getCompanyBranches();
            for (String branchString : branchesString) {
                CompanyBranch branch = branchRepository.findByName(branchString);
                if (branch == null) {
                    throw new BadInputException("Could not find any branch with name : "+branchString);
                }
                companyBranches.add(branch);
            }
            openTalk.setCompanyBranch(companyBranches);
        } else {
            openTalk.setCompanyBranch(null);
        }

        // Get user joined open talk before modify (in case : update)
        Set<User> usersJoinOpenTalkBefore = new HashSet<>();
        if (openTalkDTO.getId() != 0) {
            OpenTalk openTalk1 = openTalkRepository.findById(openTalk.getId()).get();
            usersJoinOpenTalkBefore = openTalk1.getUsers();
        }

        // set joined user from dto
        if (openTalkDTO.getUsersJoinOpenTalk() != null) {
            for (UserDTOForOpenTalk userDTO : openTalkDTO.getUsersJoinOpenTalk()) {
                Optional<User> optionalUser = userRepository.findById(userDTO.getId());
                if (optionalUser.isEmpty()) {
                    throw new BadInputException("Could not find any user with id : "+userDTO.getId() +
                            " for joined this open talk");
                }
                usersJoinOpenTalk.add(optionalUser.get());
            }
        }
        openTalk.setUserJoinedOpenTalk(usersJoinOpenTalk);

        // Get all user in before modify and after modify user in set open talk
        Set<User> mergeUserJoinedOpenTalk = new HashSet<>();
        mergeUserJoinedOpenTalk.addAll(usersJoinOpenTalkBefore);
        mergeUserJoinedOpenTalk.addAll(usersJoinOpenTalk);

        OpenTalk savedOpenTalk = openTalkRepository.save(openTalk);

        // Set the id of OpenTalkIsHosted, in case create new OpenTalk
        if (openTalkIsHostedRepository.findByIdOpenTalk(0) != null) {
            openTalkIsHostedRepository.findByIdOpenTalk(0).setIdOpenTalk(savedOpenTalk.getId());
            openTalkIsHostedRepository.save(openTalkIsHostedRepository.findByIdOpenTalk(0));
        }

        // Update number of joined open talk for user again
        for (User user : mergeUserJoinedOpenTalk) {
            user.setNumberOfJoinedOpenTalk(user.getJoinOpenTalkList().size());
            userRepository.save(user);
        }

        return openTalkMapper.entityToDto(savedOpenTalk);
    }


    // Delete by id
    @Override
    public void deleteById(int id) {
        Optional<OpenTalk> optionalOpenTalk = openTalkRepository.findById(id);
        if (optionalOpenTalk.isEmpty()) {
            throw new ItemsNotFoundException("Could not find any open talk with id : " + id);
        }
        openTalkRepository.deleteById(id);
    }

    // Find open talk already finished
    @Override
    public Page<OpenTalkDTO> findByAlreadyFinishedOpenTalk(String branch, String username, LocalDateTime now,
                                                               LocalDate startDate, LocalDate endDate,
                                                               Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo,pageSize,Sort.Direction.DESC,"date");
        Page<OpenTalk> openTalkPagePage =
                openTalkRepository.findByAlreadyFinishedOpenTalk(branch,username,LocalDateTime.now()
                                                                    ,startDate,endDate,pageable);
        Function<OpenTalk, OpenTalkDTO> function = openTalkMapper::entityToDto;
        return openTalkPagePage.map(function);
    }

    // Find open talk is coming
    @Override
    public Page<OpenTalkDTO> findByIsComingOpenTalk(String branch, String username,
                                                        LocalDateTime now, LocalDateTime estimateDate,
                                                        LocalDate startDate, LocalDate endDate,
                                                        Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo,pageSize, Sort.Direction.ASC, "date");
        Page<OpenTalk> openTalkPage
                = openTalkRepository.findByIsComingOpenTalk(branch,username,LocalDateTime.now(),
                                                                LocalDateTime.now().plusMonths(1),
                                                                startDate,endDate,pageable);
        Function<OpenTalk, OpenTalkDTO> function = openTalkMapper::entityToDto;
        return openTalkPage.map(function);
    }

    // Find open talk by user id
    @Override
    public Page<OpenTalkDTO> findByUserId (Integer id, Integer pageNo, Integer pageSize,
                                                  LocalDate startDate, LocalDate endDate) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new ItemsNotFoundException("Could not find any user with id : " + id);
        }
        Pageable pageable = PageRequest.of(pageNo,pageSize);
        Page<OpenTalk> openTalkPage = openTalkRepository.findByUserId(id,startDate,endDate,pageable);
        Function<OpenTalk, OpenTalkDTO> function = openTalkMapper::entityToDto;
        return openTalkPage.map(function);
    }

    // Find open talk already joined for user, sort by register date desc
    @Override
    public Page<OpenTalkDTO> findByAlreadyJoinedOpenTalkForUser(Integer id,
                                                                Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.Direction.DESC, "date");
        Page<OpenTalk> openTalkPage = openTalkRepository.findByAlreadyJoinedOpenTalkForUser(id,pageable);
        Function<OpenTalk, OpenTalkDTO> function = openTalkMapper::entityToDto;
        return openTalkPage.map(function);
    }


}
