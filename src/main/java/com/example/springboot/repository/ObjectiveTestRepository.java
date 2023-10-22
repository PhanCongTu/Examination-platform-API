package com.example.springboot.repository;

import com.example.springboot.entity.ObjectiveTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ObjectiveTestRepository extends JpaRepository<ObjectiveTest, Long> {
}
