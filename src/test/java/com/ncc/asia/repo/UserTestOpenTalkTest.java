package com.ncc.asia.repo;

import com.ncc.asia.test.managehost.OpenTalkIsHostedTest;
//import com.ncc.asia.test.managehost.OpenTalkIsHostedTestRepo;
import com.ncc.asia.test.managehost.UserTest;
//import com.ncc.asia.test.managehost.UserTestRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class UserTestOpenTalkTest {

//    @Autowired
//    private UserTestRepository userTestRepository;

//    @Autowired
//    private OpenTalkIsHostedTestRepo openTalkIsHostedTestRepo;

    @Autowired
    private TestEntityManager entityManager;
//
//    @Test
//    public void testCreateOpenTalkIsHostedTest () {
//        OpenTalkIsHostedTest openTalkIsHostedTest1 = new OpenTalkIsHostedTest();
//        OpenTalkIsHostedTest openTalkIsHostedTest2 = new OpenTalkIsHostedTest();
//        OpenTalkIsHostedTest openTalkIsHostedTest3 = new OpenTalkIsHostedTest();
//        OpenTalkIsHostedTest openTalkIsHostedTest4 = new OpenTalkIsHostedTest();
//        OpenTalkIsHostedTest openTalkIsHostedTest5 = new OpenTalkIsHostedTest();
//        openTalkIsHostedTest1.setName("talk1");
//        openTalkIsHostedTest2.setName("talk2");
//        openTalkIsHostedTest3.setName("talk3");
//        openTalkIsHostedTest4.setName("talk4");
//        openTalkIsHostedTest5.setName("talk5");
//        openTalkIsHostedTest1.setDate(LocalDate.now());
//        openTalkIsHostedTest2.setDate(LocalDate.now().minusYears(1));
//        openTalkIsHostedTest3.setDate(LocalDate.now().minusYears(2));
//        openTalkIsHostedTest4.setDate(LocalDate.now().minusYears(3));
//        openTalkIsHostedTest5.setDate(LocalDate.now().minusYears(4));
//        openTalkIsHostedTestRepo.saveAll(List.of(openTalkIsHostedTest1,openTalkIsHostedTest2,openTalkIsHostedTest3,
//                openTalkIsHostedTest4,openTalkIsHostedTest5));
//    }
//
//    @Test
//    public void testCreateUserTest() {
//        UserTest userTest1 = new UserTest();
//        UserTest userTest2 = new UserTest();
//        UserTest userTest3 = new UserTest();
//        UserTest userTest4 = new UserTest();
//
//        userTest1.setName("Bac");
//        userTest1.setId(1);
//        userTest2.setId(2);
//        userTest3.setId(3);
//        userTest4.setId(4);
//        userTest2.setName("Nam");
//        userTest3.setName("Tuan");
//        userTest4.setName("Duc");
//
//        OpenTalkIsHostedTest openTalkIsHostedTest1 = entityManager.find(OpenTalkIsHostedTest.class,1);
//        OpenTalkIsHostedTest openTalkIsHostedTest2 = entityManager.find(OpenTalkIsHostedTest.class,2);
//        OpenTalkIsHostedTest openTalkIsHostedTest3 = entityManager.find(OpenTalkIsHostedTest.class,3);
//        OpenTalkIsHostedTest openTalkIsHostedTest4 = entityManager.find(OpenTalkIsHostedTest.class,4);
//        OpenTalkIsHostedTest openTalkIsHostedTest5 = entityManager.find(OpenTalkIsHostedTest.class,5);
//        OpenTalkIsHostedTest openTalkIsHostedTest6 = entityManager.find(OpenTalkIsHostedTest.class,6);
//        OpenTalkIsHostedTest openTalkIsHostedTest7 = entityManager.find(OpenTalkIsHostedTest.class,7);
//        OpenTalkIsHostedTest openTalkIsHostedTest8 = entityManager.find(OpenTalkIsHostedTest.class,8);
//
//        Set<OpenTalkIsHostedTest> set1 = new HashSet<>();
//        Set<OpenTalkIsHostedTest> set2 = new HashSet<>();
//        Set<OpenTalkIsHostedTest> set3 = new HashSet<>();
//        Set<OpenTalkIsHostedTest> set4 = new HashSet<>();
//
//        set1.add(openTalkIsHostedTest1);
//        set1.add(openTalkIsHostedTest2);
//        set1.add(openTalkIsHostedTest3);
//
//        set2.add(openTalkIsHostedTest1);
//        set2.add(openTalkIsHostedTest4);
//        set2.add(openTalkIsHostedTest5);
//
//        set3.add(openTalkIsHostedTest6);
//        set3.add(openTalkIsHostedTest7);
//        set3.add(openTalkIsHostedTest8);
//
//        set4.add(openTalkIsHostedTest3);
//        set4.add(openTalkIsHostedTest4);
//        set4.add(openTalkIsHostedTest8);
//
//        userTest1.setOpenTalkIsHosted(set1);
//        userTest2.setOpenTalkIsHosted(set2);
//        userTest3.setOpenTalkIsHosted(set3);
//        userTest4.setOpenTalkIsHosted(set4);
//
////        userTestRepository.save(userTest2);
////        userTestRepository.save(userTest3);
//        userTestRepository.save(userTest4);
//
//
//    }
}