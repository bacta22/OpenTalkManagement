package com.ncc.asia.service.CompanyBranchService;

import com.ncc.asia.dto.branch.CompanyBranchDTO;
import com.ncc.asia.dto.opentalk.OpenTalkDTOForEntityDTO;
import com.ncc.asia.entity.CompanyBranch;
import com.ncc.asia.entity.OpenTalk;
import com.ncc.asia.exception.ItemsNotFoundException;
import com.ncc.asia.mapper.CompanyBranchMapper;
import com.ncc.asia.repository.CompanyBranchRepository;
import com.ncc.asia.repository.OpenTalkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CompanyBranchServiceImpl implements CompanyBranchService {

    @Autowired
    private CompanyBranchRepository companyBranchRepository;

    @Autowired
    private OpenTalkRepository openTalkRepository;

    @Autowired
    private CompanyBranchMapper companyBranchMapper;

    @Override
    public CompanyBranchDTO save(CompanyBranchDTO companyBranchDTO) {
        CompanyBranch companyBranch = companyBranchMapper.dtoToEntity(companyBranchDTO);
        Set<OpenTalk> openTalks = new HashSet<>();
        if (companyBranchDTO.getOpenTalks() != null) {
            for (OpenTalkDTOForEntityDTO openTalkDTO : companyBranchDTO.getOpenTalks()) {
                Optional<OpenTalk> optionalOpenTalk = openTalkRepository.findById(openTalkDTO.getId());
                optionalOpenTalk.ifPresent(openTalks::add);
            }
            companyBranch.setOpenTalks(openTalks);
        } else {
           companyBranch.setOpenTalks(null);
        }
        CompanyBranch savedCompanyBranch = companyBranchRepository.save(companyBranch);
        return companyBranchMapper.entityToDto(savedCompanyBranch);
    }

    @Override
    public List<CompanyBranchDTO> findAll() {
        List<CompanyBranch> companyBranches = companyBranchRepository.findAll();
        return companyBranchMapper.entityListToDtoList(companyBranches);
    }

    @Override
    public CompanyBranchDTO findById(int id) {
        Optional<CompanyBranch> companyBranchOptional = companyBranchRepository.findById(id);
        if (companyBranchOptional.isEmpty()) {
            throw new ItemsNotFoundException("Could not find any company branch with id : " + id);
        }
        return companyBranchMapper.entityToDto(companyBranchOptional.get());
    }

    @Override
    public void delete(int id) {
        Optional<CompanyBranch> optionalCompanyBranch = companyBranchRepository.findById(id);
        if (optionalCompanyBranch.isEmpty()) {
            throw new ItemsNotFoundException("Could not find any company branch with id : " + id);
        }
        companyBranchRepository.deleteById(id);
    }
}
