package com.ncc.asia.service.CompanyBranchService;

import com.ncc.asia.dto.branch.CompanyBranchDTO;

import java.util.List;

public interface CompanyBranchService {

    List<CompanyBranchDTO> findAll();
    CompanyBranchDTO findById(int id);
    CompanyBranchDTO save (CompanyBranchDTO companyBranchDTO);
    void delete (int id);

}
