package com.ncc.asia.mapper;
import com.ncc.asia.dto.opentalk.OpenTalkDTO;
import com.ncc.asia.dto.opentalk.OpenTalkIsHosted;
import com.ncc.asia.dto.user.UserDTOForOpenTalk;
import com.ncc.asia.entity.CompanyBranch;
import com.ncc.asia.entity.OpenTalk;
import com.ncc.asia.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface OpenTalkMapper {

    default OpenTalkDTO entityToDto (OpenTalk openTalk) {
        OpenTalkDTO openTalkDTO = new OpenTalkDTO();
        openTalkDTO.setId(openTalk.getId());
        openTalkDTO.setSubject(openTalk.getSubject());
        openTalkDTO.setDateTime(openTalk.getDate());
        openTalkDTO.setLinkMeeting(openTalk.getLinkMeeting());
        UserDTOForOpenTalk host = new UserDTOForOpenTalk();
        if (openTalk.getHost() != null) {
            host.setId(openTalk.getHost().getId());
            host.setName(openTalk.getHost().getUsername());
            openTalkDTO.setHost(host);
        } else {
            openTalkDTO.setHost(null);
        }

        Set<UserDTOForOpenTalk> usersJoinOpenTalk = new HashSet<>();
        Set<User> users = openTalk.getUsers();
        if (users != null) {
            for (User user : users) {
                UserDTOForOpenTalk userDTO = new UserDTOForOpenTalk();
                userDTO.setId(user.getId());
                userDTO.setName(user.getUsername());
                usersJoinOpenTalk.add(userDTO);
            }
            openTalkDTO.setUsersJoinOpenTalk(usersJoinOpenTalk);
        } else {
            openTalkDTO.setUsersJoinOpenTalk(null);
        }

        Set<String> companyBranchesString = new HashSet<>();
        Set<CompanyBranch> companyBranches = openTalk.getCompanyBranch();
        for (CompanyBranch branch : companyBranches) {
            companyBranchesString.add(branch.getName());
        }
        openTalkDTO.setCompanyBranches(companyBranchesString);
        return openTalkDTO;
    }

    List<OpenTalkDTO> entityListToDtoList (List<OpenTalk> openTalks);

    default OpenTalk dtoToEntity (OpenTalkDTO openTalkDTO) {
        OpenTalk openTalk = new OpenTalk();
        openTalk.setId(openTalkDTO.getId());
        openTalk.setSubject(openTalkDTO.getSubject());
        openTalk.setDate(openTalkDTO.getDateTime());
        openTalk.setLinkMeeting(openTalkDTO.getLinkMeeting());
        return openTalk;
    }


    @Mapping(source = "openTalk.id",target = "idOpenTalk")
    OpenTalkIsHosted openTalkToOpenTalkHosted (OpenTalk openTalk);
}
