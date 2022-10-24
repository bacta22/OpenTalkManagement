package com.ncc.asia.repository;

import com.ncc.asia.entity.CompanyBranch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyBranchRepository extends JpaRepository<CompanyBranch, Integer> {

    public CompanyBranch findByName (String name);

}
