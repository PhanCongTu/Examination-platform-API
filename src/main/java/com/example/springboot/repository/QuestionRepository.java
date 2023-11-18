package com.example.springboot.repository;

import com.example.springboot.entity.Question;
import com.example.springboot.entity.QuestionGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    @Query(value = "select * FROM question where id = :id and is_enable = :isEnable", nativeQuery = true)
    Optional<Question> findByIdAndStatus(Long id, Boolean isEnable);

    @Query(value = "select * FROM question where " +
            "question_group_id = :questionGroupId " +
            "and is_enable = :isActiveQuestion " +
            "and (content like :searchText)", nativeQuery = true)
    Page<Question> getQuestionsOfQuestionGroupByQuestionGroupId(Long questionGroupId,String searchText, boolean isActiveQuestion, Pageable pageable);

    @Query(value = "SELECT * FROM question where \n" +
            "\tquestion_group_id = :questionGroupId \n" +
            "    and is_enable = true \n" +
            "    ORDER BY RAND() LIMIT :numberOfQuestion", nativeQuery = true)
    List<Question> findRandomQuestionByQuestionGroupId(Long questionGroupId, Long numberOfQuestion);
}
