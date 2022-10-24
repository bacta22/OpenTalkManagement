package com.ncc.asia.repo;

import com.ncc.asia.entity.CompanyBranch;
import com.ncc.asia.repository.CompanyBranchRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(value = false)
public class CompanyBranchRepositoryTests {

    @Autowired
    private CompanyBranchRepository repo;

    @Test
    public void testCreateOneBranch () {
        CompanyBranch branchHN1 = new CompanyBranch(0,"HN1");
        CompanyBranch savedBranch = repo.save(branchHN1);
        Assertions.assertThat(savedBranch.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateRestBranch () {
        CompanyBranch branchHN2 = new CompanyBranch(0,"HN2");
        CompanyBranch branchHN3 = new CompanyBranch(0,"HN3");
        CompanyBranch branchDN = new CompanyBranch(0,"DN");
        CompanyBranch branchVINH = new CompanyBranch(0,"VINH");
        CompanyBranch branchQN = new CompanyBranch(0,"QN");
        CompanyBranch branchSG1 = new CompanyBranch(0,"SG1");
        CompanyBranch branchSG2 = new CompanyBranch(0,"SG2");
        List<CompanyBranch> companyBranches = repo.saveAll(List.of(branchHN2, branchHN3, branchDN, branchVINH,
                branchQN, branchSG1, branchSG2));
        Assertions.assertThat(companyBranches.size()).isEqualTo(7);
    }

    @Test
    public void testFindByName () {
        CompanyBranch branchHN1 = repo.findByName("HN3");
        System.out.println(branchHN1.getId());
        Assertions.assertThat(branchHN1.getId()).isGreaterThan(0);
    }

}
