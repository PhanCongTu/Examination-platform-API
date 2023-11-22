package com.example.springboot.repository;

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
}
