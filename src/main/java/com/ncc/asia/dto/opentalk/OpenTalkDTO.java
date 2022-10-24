package com.ncc.asia.dto.opentalk;

import com.ncc.asia.dto.user.UserDTOForOpenTalk;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class OpenTalkDTO {
    private int id;
    private String subject;
    private LocalDateTime dateTime;
    private String linkMeeting;
    private UserDTOForOpenTalk host;
    private Set<String> companyBranches;
    private Set<UserDTOForOpenTalk> usersJoinOpenTalk;
}
