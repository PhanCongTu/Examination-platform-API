package com.example.springboot.service;

import com.example.springboot.dto.request.CreateMultipleChoiceTestDTO;
import com.example.springboot.dto.request.UpdateMultipleChoiceTestDTO;
import com.example.springboot.exception.QuestionNotFoundException;
import org.springframework.http.ResponseEntity;

public interface MultipleChoiceTestService {
    ResponseEntity<?> createMultipleChoiceTest(CreateMultipleChoiceTestDTO dto) throws QuestionNotFoundException;

    ResponseEntity<?> deleteMultipleChoiceTest(Long testId);

    ResponseEntity<?> updateMultipleChoiceTest(Long testId, UpdateMultipleChoiceTestDTO dto);

    ResponseEntity<?> getMultipleChoiceTestsOfClassroom(Long classroomId,boolean isEnded, String search, int page, String column, int size, String sortType);

    ResponseEntity<?> getMyMultipleChoiceTests(boolean isEnded, String search, int page, String column, int size, String sortType);

    ResponseEntity<?> getMultipleChoiceTest(Long testId);

    ResponseEntity<?> getMyMultipleChoiceTestsOf2WeeksAround();
}
