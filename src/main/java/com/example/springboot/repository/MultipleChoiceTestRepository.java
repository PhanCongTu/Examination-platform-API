package com.example.springboot.repository;

import com.example.springboot.dto.response.MyMultipleChoiceTestResponse;
import com.example.springboot.entity.MultipleChoiceTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MultipleChoiceTestRepository extends JpaRepository<MultipleChoiceTest,Long> {

    @Query(value = "select * FROM multiple_choice_test \n" +
            "\twhere class_room_id = :classroomId \n" +
            "\t\tand end_date <= :unixTimeNow \n" +
            "\t\tand test_name like :searchText", nativeQuery = true)
    Page<MultipleChoiceTest> findEndedMultipleChoiceTestOfClassroomByClassroomId(Long classroomId,Long unixTimeNow, String searchText, Pageable pageable);

    @Query(value = "select * FROM multiple_choice_test \n" +
            "\twhere class_room_id = :classroomId \n" +
            "\t\tand end_date > :unixTimeNow \n" +
            "\t\tand test_name like :searchText", nativeQuery = true)
    Page<MultipleChoiceTest> findNotEndedMultipleChoiceTestOfClassroomByClassroomId(Long classroomId,Long unixTimeNow, String searchText, Pageable pageable);

    @Query("select new com.example.springboot.dto.response.MyMultipleChoiceTestResponse(mct.id, mct.createdBy , mct.startDate , mct.endDate, mct.testName,mct.description, mct.testingTime, \n" +
            "\tmct.classRoom.id , cr.className , cr.classCode , cr.description)\n" +
            "\tFROM MultipleChoiceTest mct left join Classroom cr on mct.classRoom.id = cr.id\n" +
            "\twhere mct.classRoom.id IN (\n" +
            "\t\tSELECT crr.classRoom.id FROM ClassroomRegistration crr \n" +
            "\t\t\twhere crr.userProfile.userID = :myId \n" +
            "\t\t\tand crr.isEnable = true\n" +
            "    ) and mct.endDate <= :unixTimeNow and mct.testName like :searchText")
    Page<MyMultipleChoiceTestResponse> findMyEndedMultipleChoiceTests(Long myId, Long unixTimeNow, String searchText, Pageable pageable);

    @Query("select new com.example.springboot.dto.response.MyMultipleChoiceTestResponse(mct.id, mct.createdBy , mct.startDate , mct.endDate, mct.testName,mct.description, mct.testingTime, \n" +
            "\tmct.classRoom.id , cr.className , cr.classCode , cr.description)\n" +
            "\tFROM MultipleChoiceTest mct left join Classroom cr on mct.classRoom.id = cr.id\n" +
            "\twhere mct.classRoom.id IN (\n" +
            "\t\tSELECT crr.classRoom.id FROM ClassroomRegistration crr \n" +
            "\t\t\twhere crr.userProfile.userID = :myId \n" +
            "\t\t\tand crr.isEnable = true\n" +
            "    ) and mct.endDate > :unixTimeNow and mct.testName like :searchText")
    Page<MyMultipleChoiceTestResponse> findMyNotEndedMultipleChoiceTests(Long myId, Long unixTimeNow, String searchText, Pageable pageable);

    @Query("select new com.example.springboot.dto.response.MyMultipleChoiceTestResponse(mct.id, mct.createdBy , mct.startDate , mct.endDate, mct.testName,mct.description, mct.testingTime, \n" +
            "\tmct.classRoom.id , cr.className , cr.classCode , cr.description)\n" +
            "\tFROM MultipleChoiceTest mct left join Classroom cr on mct.classRoom.id = cr.id\n" +
            "\twhere mct.classRoom.id IN (\n" +
            "\t\tSELECT crr.classRoom.id FROM ClassroomRegistration crr \n" +
            "\t\t\twhere crr.userProfile.userID = :userId \n" +
            "\t\t\tand crr.isEnable = true\n" +
            "    ) and mct.startDate > :unixTime2WeeksAgo and mct.startDate <= :unixTime2WeeksLater and mct.testName like :searchText")
    List<MyMultipleChoiceTestResponse> find2WeeksAroundMCTest(Long userId, Long unixTime2WeeksAgo , Long unixTime2WeeksLater,String searchText, Pageable pageable);
    @Query("select new com.example.springboot.dto.response.MyMultipleChoiceTestResponse(mct.id, mct.createdBy , mct.startDate , mct.endDate, mct.testName,mct.description, mct.testingTime, \n" +
            "\tmct.classRoom.id , cr.className , cr.classCode , cr.description)\n" +
            "\tFROM MultipleChoiceTest mct left join Classroom cr on mct.classRoom.id = cr.id\n" +
            "\twhere mct.classRoom.id IN (\n" +
            "\t\tSELECT crr.classRoom.id FROM ClassroomRegistration crr \n" +
            "\t\t\twhere crr.userProfile.userID = :userId \n" +
            "\t\t\tand crr.isEnable = true\n" +
            "    ) and ( mct.startDate >= :startDay and mct.startDate <= :endDay ) and mct.testName like :searchText")
    Page<MyMultipleChoiceTestResponse> findMCTestByDay(Long userId,Long startDay, Long endDay,String searchText, Pageable pageable);

    @Query("select new com.example.springboot.dto.response.MyMultipleChoiceTestResponse(mct.id, mct.createdBy , mct.startDate , mct.endDate, mct.testName,mct.description, mct.testingTime, \n" +
            "\tmct.classRoom.id , cr.className , cr.classCode , cr.description)\n" +
            "\tFROM MultipleChoiceTest mct left join Classroom cr on mct.classRoom.id = cr.id\n" +
            "\twhere mct.id = :testId and mct.isEnable = true " +
            "\t and mct.classRoom.id IN (\n" +
            "\t\tSELECT crr.classRoom.id FROM ClassroomRegistration crr \n" +
            "\t\t\twhere crr.userProfile.userID = :studentId \n" +
            "\t\t\tand crr.isEnable = true)")
    MyMultipleChoiceTestResponse findMultipleChoiceTestInformation(Long testId, Long studentId);
}
