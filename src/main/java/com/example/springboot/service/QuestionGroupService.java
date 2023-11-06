package com.example.springboot.service;

import com.example.springboot.dto.request.CreateQuestionGroupDTO;
import com.example.springboot.dto.request.UpdateQuestionGroupDTO;
import org.springframework.http.ResponseEntity;

public interface QuestionGroupService {

    ResponseEntity<?> createQuestionGroup(CreateQuestionGroupDTO dto);

    ResponseEntity<?> getAllQuestionGroupOfClassroom(Long classroomId, int page, String column, int size, String sortType, Boolean isEnable);

    ResponseEntity<?> switchQuestionGroupStatus(Long questionGroupId, boolean newStatus);

    ResponseEntity<?> updateQuestionGroup(Long questionGroupId, UpdateQuestionGroupDTO dto);
}
