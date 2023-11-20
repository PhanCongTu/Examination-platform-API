package com.example.springboot.repository;

import com.example.springboot.dto.response.MyMultipleChoiceTestResponse;
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

    @Query("select new com.example.springboot.dto.response.MyMultipleChoiceTestResponse(mct.id, mct.createdBy , mct.startDate , mct.endDate, mct.testName, mct.testingTime, \n" +
            "\tmct.classRoom.id , cr.className , cr.classCode )\n" +
            "\tFROM MultipleChoiceTest mct left join Classroom cr on mct.classRoom.id = cr.id\n" +
            "\twhere mct.classRoom.id IN (\n" +
            "\t\tSELECT crr.classRoom.id FROM ClassroomRegistration crr \n" +
            "\t\t\twhere crr.userProfile.userID = :myId \n" +
            "\t\t\tand crr.isEnable = true\n" +
            "    ) and mct.startDate <= :unixTimeNow and mct.testName like :searchText")
    Page<MyMultipleChoiceTestResponse> findMyStatedMultipleChoiceTest(Long myId, Long unixTimeNow, String searchText, Pageable pageable);

    @Query("select new com.example.springboot.dto.response.MyMultipleChoiceTestResponse(mct.id, mct.createdBy , mct.startDate , mct.endDate, mct.testName, mct.testingTime, \n" +
            "\tmct.classRoom.id , cr.className , cr.classCode )\n" +
            "\tFROM MultipleChoiceTest mct left join Classroom cr on mct.classRoom.id = cr.id\n" +
            "\twhere mct.classRoom.id IN (\n" +
            "\t\tSELECT crr.classRoom.id FROM ClassroomRegistration crr \n" +
            "\t\t\twhere crr.userProfile.userID = :myId \n" +
            "\t\t\tand crr.isEnable = true\n" +
            "    ) and mct.startDate > :unixTimeNow and mct.testName like :searchText")
    Page<MyMultipleChoiceTestResponse> findMyNotStatedMultipleChoiceTest(Long myId, Long unixTimeNow, String searchText, Pageable pageable);
}
