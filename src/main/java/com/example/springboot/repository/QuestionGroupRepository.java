package com.example.springboot.repository;

import com.example.springboot.entity.QuestionGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionGroupRepository extends JpaRepository<QuestionGroup, Long> {

    Optional<QuestionGroup> findByCode(String code);

    @Query(value = "select * FROM question_group where class_room_id = :classroomId ", nativeQuery = true)
    Page<QuestionGroup> findQuestionGroupsOfClassroomByClassroomId(Long classroomId, Pageable pageable);
}
