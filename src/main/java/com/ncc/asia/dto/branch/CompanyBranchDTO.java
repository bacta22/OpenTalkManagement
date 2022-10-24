package com.ncc.asia.dto.branch;

import com.ncc.asia.dto.opentalk.OpenTalkDTOForEntityDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Getter
@Setter
@ToString
public class CompanyBranchDTO {
    private int id;
    private String name;
    private Set<OpenTalkDTOForEntityDTO> openTalks;
}
