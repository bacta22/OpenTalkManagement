package com.ncc.asia.test.managehost;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

//public interface UserTestRepository extends JpaRepository<UserTest,Integer> {
//
//    @Query("SELECT DISTINCT u FROM UserTest u " +
//            "JOIN u.openTalkIsHosted o " +
//            "WHERE YEAR(o.date) < :year")
//    List<UserTest> findByYear (@RequestParam("year") int year);
//
//}
