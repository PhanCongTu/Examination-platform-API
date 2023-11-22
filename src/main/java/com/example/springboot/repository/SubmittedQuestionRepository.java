package com.example.springboot.repository;

import com.example.springboot.entity.SubmittedQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubmittedQuestionRepository extends JpaRepository<SubmittedQuestion, Long> {

    List<SubmittedQuestion> findAllByScoreId(Long scoreId);
}
