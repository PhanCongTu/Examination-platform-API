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

    @Query(value = "select * FROM question_group where id = :id and is_enable = :isEnable", nativeQuery = true)
    Optional<QuestionGroup> findByIdAndStatus(Long id, Boolean isEnable);

    Optional<QuestionGroup> findByCode(String code);

    @Query(value = "select * FROM question_group where " +
            "class_room_id = :classroomId " +
            "and is_enable = :isEnable " +
            "and (question_group.name like :searchText or question_group.code like :searchText)", nativeQuery = true)
    Page<QuestionGroup> findQuestionGroupsOfClassroomByClassroomId(Long classroomId,String searchText, Boolean isEnable, Pageable pageable);
}
