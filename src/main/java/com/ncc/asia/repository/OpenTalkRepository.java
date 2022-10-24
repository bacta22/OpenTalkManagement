package com.ncc.asia.repository;

import com.ncc.asia.dto.opentalk.IOpenTalkCount;
import com.ncc.asia.dto.opentalk.OpenTalkCount;
import com.ncc.asia.entity.OpenTalk;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Repository
public interface OpenTalkRepository extends JpaRepository<OpenTalk, Integer> {

    // Get Open Talk already finished
    @Query("SELECT DISTINCT o FROM OpenTalk o " +
            "JOIN o.users u " +
            "JOIN o.companyBranch b " +
            "WHERE o.date < :now " + //(cast(:fromDate as date) is null )
            "AND (:branch IS NULL OR b.name LIKE CONCAT('%',:branch,'%')) " +
            "AND (:username IS NULL OR u.username LIKE CONCAT('%',:username,'%')) " +
            "AND (CAST(:startDate AS LocalDate) IS NULL OR CAST(o.date AS LocalDate) >= :startDate) " +
            "AND (CAST(:endDate AS LocalDate) IS NULL OR CAST(o.date AS LocalDate) <= :endDate)")
    Page<OpenTalk> findByAlreadyFinishedOpenTalk(@Param("branch") String branch,
                                                 @Param("username") String username,
                                                 @Param("now") LocalDateTime now,
                                                 @Param("startDate") LocalDate startDate,
                                                 @Param("endDate") LocalDate endDate,
                                                 Pageable pageable);

    // Get Open Talk is coming
    @Query("SELECT DISTINCT o FROM OpenTalk o " +
            "JOIN o.users u " +
            "JOIN o.companyBranch b " +
            "WHERE o.date > :now AND o.date <= :estimateDate " +
            "AND (:branch IS NULL OR b.name LIKE CONCAT('%',:branch,'%')) " +
            "AND (:username IS NULL OR u.username LIKE CONCAT('%',:username,'%')) " +
            "AND (CAST(:startDate AS LocalDate) IS NULL OR CAST(o.date AS LocalDate) >= :startDate) " +
            "AND (CAST(:endDate AS LocalDate) IS NULL OR CAST(o.date AS LocalDate) <= :endDate)")
    Page<OpenTalk> findByIsComingOpenTalk(@Param("branch") String branch,
                                          @Param("username") String username,
                                          @Param("now") LocalDateTime now,
                                          @Param("estimateDate") LocalDateTime estimateDate,
                                          @Param("startDate") LocalDate startDate,
                                          @Param("endDate") LocalDate endDate,
                                          Pageable pageable);

    // Get Open Talk for user id
    @Query("SELECT DISTINCT o FROM OpenTalk o " +
            "JOIN o.users u " +
            "WHERE u.id = :id " +
            "AND (CAST(:startDate AS LocalDate) IS NULL OR CAST(o.date AS LocalDate) >= :startDate) " +
            "AND (CAST(:endDate AS LocalDate) IS NULL OR CAST(o.date AS LocalDate) <= :endDate)")
    Page<OpenTalk> findByUserId(@Param("id") Integer id,
                                @Param("startDate") LocalDate startDate,
                                @Param("endDate") LocalDate endDate,
                                Pageable pageable);

    // Find open talk already joined for user, sort by register date desc
    @Query("SELECT DISTINCT o FROM OpenTalk o " +
            "JOIN o.users u " +
            "WHERE u.id = :id ")
    Page<OpenTalk> findByAlreadyJoinedOpenTalkForUser (@Param("id") Integer id, Pageable pageable);

    // Find open talk by date and branch for send email
    @Query("SELECT DISTINCT o FROM OpenTalk o " +
            "JOIN o.companyBranch b " +
            "WHERE CAST(o.date AS LocalDate) = ?1 " +
            "AND b.name LIKE CONCAT('%',?2,'%')")
    OpenTalk findOpenTalkForSendEmail (LocalDate date, String branch);

    // Find open talk, group by year, count the number of open talk. Use Class Constructor
    @Query("SELECT new com.ncc.asia.dto.opentalk.OpenTalkCount(YEAR(o.date), COUNT(o.date)) " +
            "FROM OpenTalk AS o GROUP BY YEAR(o.date) ORDER BY YEAR(o.date) DESC")
    List<OpenTalkCount> countTotalOpenTalkByYearClass();

    // Find open talk, group by year, count the number of open talk. Use Spring Data Projection
    @Query("SELECT YEAR(o.date) AS year, COUNT(o.date) AS numberOfOpenTalk " +
            "FROM OpenTalk AS o GROUP BY YEAR(o.date) ORDER BY YEAR(o.date) ASC")
    List<IOpenTalkCount> countTotalOpenTalkByYearInterface();

    // Test
    List<OpenTalk> findByDateAfter(LocalDateTime afterDate);

    @Query("SELECT o FROM OpenTalk o JOIN o.users u WHERE u.username = :username")
    List<OpenTalk> findAllByUserName(@Param("username") String username);

    @Query("SELECT o FROM OpenTalk o JOIN o.companyBranch b WHERE b.name = :name")
    Set<OpenTalk> findAllByCompanyBranch(@Param("name") String branchName);

    List<OpenTalk> findByUsers_Username(String userName); // findByUserNameEmployee

    List<OpenTalk> findByUsers_Id(int id);

    @Query("SELECT o FROM OpenTalk o JOIN o.companyBranch b WHERE b.name LIKE CONCAT('%',:name,'%')")
    Set<OpenTalk> findAllByCompanyBranchLike(@Param("name") String branchName);

    @Query("SELECT o FROM OpenTalk o WHERE YEAR(o.date) = :year")
    Set<OpenTalk> findOpenTalksByYear (@Param("year") Integer year);
}

