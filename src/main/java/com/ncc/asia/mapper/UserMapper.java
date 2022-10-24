package com.ncc.asia.mapper;

import com.ncc.asia.dto.opentalk.OpenTalkDTOForEntityDTO;
import com.ncc.asia.dto.user.UserDTODisplay;
import com.ncc.asia.entity.OpenTalk;
import com.ncc.asia.entity.Role;
import com.ncc.asia.entity.User;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", uses = {RoleMapper.class})
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    // for display
    default UserDTODisplay entityToDto (User user) {
        UserDTODisplay userDTO = new UserDTODisplay();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setFullName(user.getFullName());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setCompanyBranch(user.getCompanyBranch().getName());
        Set<String> roles = new HashSet<>();
        for (Role role : user.getRoles()) {
            roles.add(role.getName());
        }
        userDTO.setRoles(roles);
        Set<OpenTalkDTOForEntityDTO> openTalks = new HashSet<>();
        for (OpenTalk openTalk : user.getJoinOpenTalkList()) {
            openTalks.add(new OpenTalkDTOForEntityDTO(openTalk.getId(),openTalk.getSubject()));
        }
        userDTO.setOpentalks(openTalks);
        userDTO.setNumberOfJoinedOpenTalk(user.getNumberOfJoinedOpenTalk());
        return userDTO;
    }

    List<UserDTODisplay> entityListToDtoList (List<User> users);





}
