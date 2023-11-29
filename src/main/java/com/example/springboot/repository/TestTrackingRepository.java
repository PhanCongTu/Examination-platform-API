package com.example.springboot.repository;

import com.example.springboot.entity.MultipleChoiceTest;
import com.example.springboot.entity.TestTracking;
import com.example.springboot.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TestTrackingRepository extends JpaRepository<TestTracking,Long> {

    Optional<TestTracking> findByMultipleChoiceTestIdAndUserProfileUserID(Long testId, Long studentId);


}
