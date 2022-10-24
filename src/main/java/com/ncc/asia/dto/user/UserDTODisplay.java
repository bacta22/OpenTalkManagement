package com.ncc.asia.dto.user;

import com.ncc.asia.dto.opentalk.OpenTalkDTOForEntityDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Getter
@Setter
@ToString
public class UserDTODisplay {

    private int id;
    private String username;
    private String fullName;
    private String phoneNumber;
    private String companyBranch;
    private int numberOfJoinedOpenTalk;
    private Set<String> roles;
    private Set<OpenTalkDTOForEntityDTO> opentalks;
}
