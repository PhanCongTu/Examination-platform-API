package com.example.springboot.repository;

import com.example.springboot.entity.MultipleChoiceTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MultipleChoiceTestRepository extends JpaRepository<MultipleChoiceTest,Long> {

    @Query(value = "select * FROM multiple_choice_test \n" +
            "\twhere class_room_id = :classroomId \n" +
            "\t\tand start_date <= :unixTimeNow \n" +
            "\t\tand test_name like :searchText", nativeQuery = true)
    Page<MultipleChoiceTest> findStatedMultipleChoiceTestOfClassroomByClassroomId(Long classroomId,Long unixTimeNow, String searchText, Pageable pageable);

    @Query(value = "select * FROM multiple_choice_test \n" +
            "\twhere class_room_id = :classroomId \n" +
            "\t\tand start_date > :unixTimeNow \n" +
            "\t\tand test_name like :searchText", nativeQuery = true)
    Page<MultipleChoiceTest> findNotStatedMultipleChoiceTestOfClassroomByClassroomId(Long classroomId,Long unixTimeNow, String searchText, Pageable pageable);
}
