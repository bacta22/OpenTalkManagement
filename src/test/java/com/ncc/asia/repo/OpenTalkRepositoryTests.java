package com.ncc.asia.repo;

import com.ncc.asia.dto.opentalk.IOpenTalkCount;
import com.ncc.asia.dto.opentalk.OpenTalkCount;
import com.ncc.asia.dto.opentalk.OpenTalkIsHosted;
import com.ncc.asia.entity.CompanyBranch;
import com.ncc.asia.entity.OpenTalk;
import com.ncc.asia.entity.User;
import com.ncc.asia.mapper.OpenTalkMapper;
import com.ncc.asia.repository.OpenTalkIsHostedRepository;
import com.ncc.asia.repository.OpenTalkRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(value = false)
public class OpenTalkRepositoryTests {

    @Autowired
    private OpenTalkRepository openTalkRepository;

    @Autowired
    private TestEntityManager entityManager;


    @Test
    public void testCreateOpenTalk () {
        LocalDateTime localDateTime1 = LocalDateTime.now();
        CompanyBranch branchHN1 = entityManager.find(CompanyBranch.class,1);
        CompanyBranch branchHN2 = entityManager.find(CompanyBranch.class,2);

        OpenTalk openTalk1 = new OpenTalk("Presentation skill",localDateTime1,"def.com");
        OpenTalk openTalk2 = new OpenTalk("Java Programing",localDateTime1,"abc.com");

        openTalk1.addCompanyBranch(branchHN1);
        openTalk1.addCompanyBranch(branchHN2);
        openTalk2.addCompanyBranch(branchHN2);

        List<OpenTalk> openTalks = openTalkRepository.saveAll(List.of(openTalk1,openTalk2));


        Assertions.assertThat(openTalks.size()).isEqualTo(2);
    }

    @Test
    public void testCreateOpenTalk2 () {
        LocalDateTime localDateTime1 = LocalDateTime.now();
        CompanyBranch branchSG7 = entityManager.find(CompanyBranch.class,7);
        CompanyBranch branchSG8 = entityManager.find(CompanyBranch.class,8);

        User user = entityManager.find(User.class,1);

        OpenTalk openTalk1 = new OpenTalk("C# Programing",localDateTime1,"def.com");
        OpenTalk openTalk2 = new OpenTalk("Golang Programing",localDateTime1,"abc.com");

        openTalk1.addCompanyBranch(branchSG7);
        openTalk2.addCompanyBranch(branchSG8);
        //openTalk1.setHost(user);

        List<OpenTalk> openTalks = openTalkRepository.saveAll(List.of(openTalk1,openTalk2));


        Assertions.assertThat(openTalks.size()).isEqualTo(2);
    }

    @Test
    public void testCreateOpenTalk3 () {
        OpenTalk openTalk = entityManager.find(OpenTalk.class,4);
        User user = entityManager.find(User.class,2);
        openTalk.setHostUser(user);
    }

    @Test
    public void testCreateOpenTalk4 () {
        LocalDateTime localDateTime1 = LocalDateTime.now();
        CompanyBranch branchSG7 = entityManager.find(CompanyBranch.class,4);
        CompanyBranch branchSG8 = entityManager.find(CompanyBranch.class,5);

        User user = entityManager.find(User.class,1);

        OpenTalk openTalk1 = new OpenTalk("PHP Programing",localDateTime1,"def.com");

        openTalk1.addCompanyBranch(branchSG7);
        openTalk1.addCompanyBranch(branchSG8);
        openTalk1.setHost(user);

        openTalkRepository.save(openTalk1);
    }

    @Test
    public void testRegisterOpenTalkForUser () {
        OpenTalk openTalk3 = openTalkRepository.findById(3).get();
        OpenTalk openTalk4 = openTalkRepository.findById(4).get();
        User user3 = entityManager.find(User.class,3);
        openTalk3.setHostUser(user3);
        openTalk4.registerOpenTalkForUser(user3);
    }

    @Test
    public void testGetCompanyBranch () {
        OpenTalk openTalk = openTalkRepository.findById(2).get();
        Set<CompanyBranch> branches = openTalk.getCompanyBranch();
        CompanyBranch branch = entityManager.find(CompanyBranch.class,1);
//        branches.add(branch);
//        openTalk.addCompanyBranch(branch);
//        openTalkRepository.save(openTalk);
        branches.forEach(System.out::println);
    }

    @Test
    public void testFindByUsers_Username() {
        String username = "bac.td";
        List<OpenTalk> openTalks = openTalkRepository.findByUsers_Username(username);
        for (OpenTalk openTalk : openTalks) {
            System.out.println(openTalk.getId());
        }
        System.out.println(openTalks.size());
    }

    @Test
    public void testFindAllByUserName() {
        String username = "bac.td";
        List<OpenTalk> openTalks = openTalkRepository.findAllByUserName(username);
        for (OpenTalk openTalk : openTalks) {
            System.out.println(openTalk.getId());
        }
        System.out.println(openTalks.size());
    }

    @Test
    public void testFindByCompanyBranch() {
        String branchName = "SG1";
        Set<OpenTalk> openTalks = openTalkRepository.findAllByCompanyBranch(branchName);
        for (OpenTalk openTalk : openTalks) {
            System.out.println(openTalk.getId());
        }
        Assertions.assertThat(openTalks.size()).isEqualTo(2);
    }

    @Test
    public void testFindOpenTalkByDateAfter () {
        LocalDateTime afterDate = LocalDateTime.of(2022,8,6,8,0,0);
        List<OpenTalk> openTalks = openTalkRepository.findByDateAfter(afterDate);
        openTalks.forEach(o-> System.out.println(o.getId()));
        Assertions.assertThat(openTalks.size()).isEqualTo(2);
    }

    @Test
    public void testFindOpenTalkByYear () {
        Set<OpenTalk> openTalk = openTalkRepository.findOpenTalksByYear(2020);
        for (OpenTalk o : openTalk) {
            System.out.println(o.getId());
        }
    }

    @Test
    public void testFindOpenTalkByDateAndBranch () {
        LocalDate date = LocalDate.now().minusDays(3);
        OpenTalk openTalks = openTalkRepository.findOpenTalkForSendEmail(date,"HN");
        System.out.println(openTalks);
    }

    @Test
    public void testCountTotalOpenTalkByYearClass () {
        List<OpenTalkCount> openTalkCounts = openTalkRepository.countTotalOpenTalkByYearClass();
        for (OpenTalkCount o : openTalkCounts) {
            System.out.println(o);
        }
    }

    @Test
    public void testCountTotalOpenTalkByYearInterface () {
        List<IOpenTalkCount> openTalkCounts = openTalkRepository.countTotalOpenTalkByYearInterface();
        for (IOpenTalkCount o : openTalkCounts) {
            System.out.println("Year: " + o.getYear() + " - " + "Number: " + o.getNumberOfOpenTalk());
        }
    }

    @Test
    public void testForFetchType() {
        OpenTalk openTalk = openTalkRepository.findById(1).get();
        System.out.println(openTalk.getCompanyBranch());
    }

}
