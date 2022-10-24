package com.ncc.asia.repository;

import com.ncc.asia.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    // Find by name
    User findByUsernameIs(String name);

    // Find user by some filer
    @Query("SELECT DISTINCT u FROM User u " +
            "WHERE (:enabled IS NULL OR u.enabled = :enabled) " +
            "AND (:branch IS NULL OR u.companyBranch.name LIKE CONCAT('%',:branch,'%')) " +
            "AND (:username IS NULL OR u.username LIKE CONCAT('%',:username,'%'))")
    Page<User> findUserBySomeFilter (@Param("enabled") Boolean enabled,
                                     @Param("branch") String branch,
                                     @Param("username") String username,
                                     Pageable pageable);

    // Find user joined open talk sort by ascending the number of joined open talk
    @Query("SELECT DISTINCT u FROM User u " +
            "WHERE (:enabled IS NULL OR u.enabled = :enabled) " +
            "AND (:branch IS NULL OR u.companyBranch.name LIKE CONCAT('%',:branch,'%')) " +
            "AND (:username IS NULL OR u.username LIKE CONCAT('%',:username,'%'))")
    Page<User> findUserBySortByNoOfOpenTalk (@Param("enabled") Boolean enabled,
                                     @Param("branch") String branch,
                                     @Param("username") String username,
                                     Pageable pageable);

    // Find user have not hosted open talk in this year
    @Query("SELECT DISTINCT u FROM User u " +
            "JOIN u.openTalkIsHosted o " +
            "WHERE YEAR(o.date) < :year " +
            "AND u.enabled = true")
    List<User> findByByHaveNotHostedThisYear (@Param("year") Integer year);

    // Find user by company branch and enabled status
    @Query("SELECT u FROM User u WHERE u.enabled = true " +
            "AND u.companyBranch.name = ?1")
    List<User> findByEnabledAndBranch(String branch);

    // Find users who belong to the branch that takes place open talk, for send email
    @Query("SELECT u FROM User u " +
            "WHERE u.enabled = true " +
            "AND u.companyBranch.name IN ?1")
    List<User> findUserForSendEmail (Set<String> branchesName);



    // Test
    List<User> findByEnabled (Boolean enabled); // Many result => return List<User>, return User => exception

    @Query("UPDATE User u SET u.enabled = ?2 WHERE u.id = ?1")
    @Modifying
    void updateEnabledStatus (Integer id, boolean enabled);

    @Query("SELECT u FROM User u WHERE u.username = :username")
    User findUserByUserName(@Param("username") String username);

    @Query("select count(o) from User u join u.openTalkIsHosted o where u.id = ?1")
    long countOpenTalkIsHosted (int id);

    @Query("SELECT DISTINCT u FROM User u " +
            "JOIN u.openTalkIsHosted o " +
            "WHERE YEAR(o.date) < :year")
    Page<User> findUserByHaveNotHosted (@Param("year") Integer year, Pageable pageable);
}
