package com.example.springboot.repository;

import com.example.springboot.entity.MultipleChoiceTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MultipleChoiceTestRepository extends JpaRepository<MultipleChoiceTest,Long> {
}
