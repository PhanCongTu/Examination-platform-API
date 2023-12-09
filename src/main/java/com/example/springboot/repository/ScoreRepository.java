package com.example.springboot.repository;

import com.example.springboot.dto.response.MyScoreResponse;
import com.example.springboot.dto.response.StudentScoreResponse;
import com.example.springboot.entity.Score;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Long> {

    Optional<Score> findByMultipleChoiceTestIdAndUserProfileUserID(Long testId, Long userId);

    @Query("select new com.example.springboot.dto.response.StudentScoreResponse( s.id, s.totalCore, s.createdDate, s.isLate, u.userID, u.displayName, u.loginName) " +
            "FROM Score s inner join UserProfile u on  s.userProfile.userID = u.userID \n" +
            "where s.multipleChoiceTest.id = :testId and (u.displayName like :searchText or u.loginName like :searchText)")
    Page<StudentScoreResponse> findAllScoreOfMultipleChoiceTest(Long testId, String searchText, Pageable pageable);

    @Query("select new com.example.springboot.dto.response.MyScoreResponse(s.id, s.totalCore, s.isLate, s.submittedDate, mc.id, mc.testName, c.id, c.className, c.classCode) " +
            "FROM Score s inner join MultipleChoiceTest mc on s.multipleChoiceTest.id = mc.id " +
            "inner join Classroom c on mc.classRoom.id = c.id " +
            "where mc.testName like :searchText and s.userProfile.userID = :studentId and s.submittedDate > :dateFrom and s.submittedDate < :dateTo")
    Page<MyScoreResponse> findAllMyScores(Long studentId,String searchText, Long dateFrom,Long dateTo , Pageable pageable);
}
