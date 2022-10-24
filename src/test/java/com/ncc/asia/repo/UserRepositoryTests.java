package com.ncc.asia.repo;

import com.ncc.asia.dto.opentalk.OpenTalkIsHosted;
import com.ncc.asia.entity.CompanyBranch;
import com.ncc.asia.entity.OpenTalk;
import com.ncc.asia.entity.Role;
import com.ncc.asia.entity.User;
import com.ncc.asia.repository.UserRepository;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(value = false)
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;


    @Test
    public void testCreateUserWithSingleRole () {
        Role roleAdmin = entityManager.find(Role.class,1);
        CompanyBranch branchHN1 = entityManager.find(CompanyBranch.class,1);
        OpenTalk openTalk = entityManager.find(OpenTalk.class,1);

        String password = "$2a$12$wCyRO1At5I6h0KZITzPEAOWNKM97RZBtCjDQ1A3l1Ut2.24LWVEue";
        LocalDate localDate1 = LocalDate.now();
        User user1 = new User("bac.td",password,"Ta Dong Bac",localDate1,true);
        user1.addRole(roleAdmin);
        user1.setCompanyBranch(branchHN1);
        user1.registerHostOpenTalk(openTalk);
        User savedUser = userRepository.save(user1);
        Assertions.assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateUserWithSingleRoleNotYetJoinOT () {
        Role roleAdmin = entityManager.find(Role.class,1);
        CompanyBranch branchHN1 = entityManager.find(CompanyBranch.class,1);
        String password = "$2a$12$wCyRO1At5I6h0KZITzPEAOWNKM97RZBtCjDQ1A3l1Ut2.24LWVEue";
        LocalDate localDate1 = LocalDate.now();
        User user1 = new User("dinh.pv",password,"Pham Van Dinh",localDate1,true);
        user1.addRole(roleAdmin);
        user1.setCompanyBranch(branchHN1);
        User savedUser = userRepository.save(user1);
        Assertions.assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateUserWithMultipleRoleHostOneOpenTalk () {
        Role roleAdmin = entityManager.find(Role.class,1);
        Role roleEmployee = entityManager.find(Role.class,2);
        CompanyBranch branchHN2 = entityManager.find(CompanyBranch.class,2);
        OpenTalk openTalk1 = entityManager.find(OpenTalk.class,1);
        OpenTalk openTalk2 = entityManager.find(OpenTalk.class,2);

        String password = "$2a$12$wCyRO1At5I6h0KZITzPEAOWNKM97RZBtCjDQ1A3l1Ut2.24LWVEue";
        LocalDate localDate1 = LocalDate.now();
        User user1 = new User("cuong.nm",password,"Cuong Nguyen Manh",localDate1,true);
        user1.addRole(roleAdmin);
        user1.addRole(roleEmployee);
        user1.setCompanyBranch(branchHN2);
        user1.registerJoinedOpenTalk(openTalk1);
        user1.registerHostOpenTalk(openTalk2);

        User savedUser = userRepository.save(user1);
        Assertions.assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateUserWithMultipleRoleAndHostMutipleOpenTalk () {
        Role roleAdmin = entityManager.find(Role.class,1);
        Role roleEmployee = entityManager.find(Role.class,2);
        CompanyBranch branchDN = entityManager.find(CompanyBranch.class,4);
        OpenTalk openTalkJava = entityManager.find(OpenTalk.class,3);
        OpenTalk openTalkPresentation = entityManager.find(OpenTalk.class,4);

        String password = "$2a$12$wCyRO1At5I6h0KZITzPEAOWNKM97RZBtCjDQ1A3l1Ut2.24LWVEue";
        LocalDate localDate1 = LocalDate.now();
        User user1 = new User("hung.pq",password,"Hung Pham Quang",localDate1,true);
        user1.addRole(roleAdmin);
        user1.addRole(roleEmployee);
        user1.setCompanyBranch(branchDN);
        user1.registerHostOpenTalk(openTalkPresentation);

        User savedUser = userRepository.save(user1);
        Assertions.assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateUserWithMultipleRole () {
        Role roleAdmin = entityManager.find(Role.class,1);
        Role roleEmployee = entityManager.find(Role.class,2);
        CompanyBranch branchSG1 = entityManager.find(CompanyBranch.class,7);

        String password = "$2a$12$wCyRO1At5I6h0KZITzPEAOWNKM97RZBtCjDQ1A3l1Ut2.24LWVEue";
        LocalDate localDate1 = LocalDate.now();
        User user1 = new User("hung.pq",password,"Hung Pham Quang",localDate1,true);
        user1.addRole(roleAdmin);
        user1.addRole(roleEmployee);
        user1.setCompanyBranch(branchSG1);

        User savedUser = userRepository.save(user1);
        Assertions.assertThat(savedUser.getId()).isGreaterThan(0);
    }

    // Find by name
    @Test
    public void testFindUserByUsername () {
        User user = userRepository.findByUsernameIs("hung.pq");
        System.out.println(user.getFullName());
        Assertions.assertThat(user.getId()).isGreaterThan(0);
    }

    @Test
    public void testUpdateEnabledStatus () {
        userRepository.updateEnabledStatus(10,false);
    }

    @Test
    public void testGetOpenTalkIsHosted () {
        User user = userRepository.findById(3).get();
        Set<OpenTalkIsHosted> openTalkIsHosted = user.getOpenTalkIsHosted();
        System.out.println("Size: "+openTalkIsHosted.size());
        for (OpenTalkIsHosted o : openTalkIsHosted) {
            System.out.println("=============");
            System.out.println(o.getSubject());
        }
    }

    @Test
    public void testGetOpenTalkISHostedByYear () {
        Pageable pageable = PageRequest.of(0,5);
        Page<User> page = userRepository.findUserByHaveNotHosted(2022,pageable);
        List<User> userList = page.getContent();
        for (User user : userList) {
            System.out.println(user.getUsername());
        }
    }

    @Test
    public void testCountTheNumberOpenTalkISHostedByUserId () {
        long count = userRepository.countOpenTalkIsHosted(10);
        System.out.println(count);
    }

    @Test
    public void testGetUserByEnabledAndBranch() {
        List<User> users = userRepository.findByEnabledAndBranch("HN1");
        for (User user : users) {
            System.out.println(user.getUsername());
        }
    }

    @Test
    public void testFindUserForSendEmail () {
        Set<String> branches =  new HashSet<>();
        branches.add("HN1");
        branches.add("HN2");
        branches.add("HN3");
        List<User> users = userRepository.findUserForSendEmail(branches);
        for (User user : users) {
            System.out.println(user.getId() + "-" + user.getUsername());
        }
    }

    @Test
    public void testForFetchType() {
        User user = userRepository.findById(1).get();
        System.out.println("------------------------------------------------------------------------");
        System.out.println(user);
        System.out.println(user.getRoles());
    }


}

// TestEntityManager
// Alternative to EntityManager for use in JPA tests. Provides a subset of EntityManager
// methods that are useful for tests as well as helper methods for common testing tasks
// such as persist/flush/find.