package com.example.springboot.repository;

import com.example.springboot.entity.SubmittedQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubmittedQuestionRepository extends JpaRepository<SubmittedQuestion, Long> {
}
