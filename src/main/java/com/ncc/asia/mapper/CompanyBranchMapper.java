package com.ncc.asia.mapper;

import com.ncc.asia.dto.branch.CompanyBranchDTO;
import com.ncc.asia.dto.opentalk.OpenTalkDTOForEntityDTO;
import com.ncc.asia.entity.CompanyBranch;
import com.ncc.asia.entity.OpenTalk;
import org.mapstruct.Mapper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface CompanyBranchMapper {

    default CompanyBranch dtoToEntity (CompanyBranchDTO companyBranchDTO) {
        CompanyBranch companyBranch = new CompanyBranch();
        companyBranch.setId(companyBranchDTO.getId());
        companyBranch.setName(companyBranchDTO.getName());
        return companyBranch;
    }

    List<CompanyBranchDTO> entityListToDtoList (List<CompanyBranch> companyBranches);

    default CompanyBranchDTO entityToDto (CompanyBranch companyBranch) {
        CompanyBranchDTO companyBranchDTO = new CompanyBranchDTO();
        companyBranchDTO.setId(companyBranch.getId());
        companyBranchDTO.setName(companyBranch.getName());
        Set<OpenTalkDTOForEntityDTO> opentalks = new HashSet<>();
        Set<OpenTalk> openTalkSet = companyBranch.getOpenTalks();
        for (OpenTalk openTalk : openTalkSet) {
            opentalks.add(new OpenTalkDTOForEntityDTO(openTalk.getId(),openTalk.getSubject()));
        }
        companyBranchDTO.setOpenTalks(opentalks);
        return companyBranchDTO;
    }

}
