package com.example.springboot.service;

import com.example.springboot.dto.request.CreateQuestionGroupDTO;
import org.springframework.http.ResponseEntity;

public interface QuestionGroupService {

    ResponseEntity<?> createQuestionGroup(CreateQuestionGroupDTO dto);

    ResponseEntity<?> getAllQuestionGroupOfClassroom(Long classroomId, int page, String column, int size, String sortType);
}
