package com.example.springboot.repository;

import com.example.springboot.entity.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Long> {

    Optional<Score> findByMultipleChoiceTestIdAndUserProfileUserID(Long testId, Long userId);
}
