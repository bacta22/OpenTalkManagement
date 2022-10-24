package com.ncc.asia.controller;

import com.ncc.asia.dto.branch.CompanyBranchDTO;
import com.ncc.asia.service.CompanyBranchService.CompanyBranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/branch")
public class CompanyBranchRestController {
    @Autowired
    CompanyBranchService service;

    @GetMapping
    public ResponseEntity<List<CompanyBranchDTO>> findAll() {
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{branchId}")
    public ResponseEntity<CompanyBranchDTO> findById(@PathVariable("branchId") int id) {
        return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CompanyBranchDTO> create (@RequestBody CompanyBranchDTO companyBranchDTO) {
        companyBranchDTO.setId(0);
        CompanyBranchDTO savedCompanyBranchDTO = service.save(companyBranchDTO);
        return new ResponseEntity<>(savedCompanyBranchDTO, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<CompanyBranchDTO> update (@RequestBody CompanyBranchDTO companyBranchDTO) {
        CompanyBranchDTO updatedCompanyBranchDTO = service.save(companyBranchDTO);
        return new ResponseEntity<>(updatedCompanyBranchDTO,HttpStatus.OK);
    }

    @DeleteMapping("/{branchId}")
    public ResponseEntity<Void> delete (@PathVariable("branchId") int id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
