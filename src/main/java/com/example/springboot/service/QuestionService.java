package com.example.springboot.service;

import com.example.springboot.dto.request.CreateQuestionDTO;
import com.example.springboot.dto.request.UpdateQuestionDTO;
import org.springframework.http.ResponseEntity;

public interface QuestionService {
    ResponseEntity<?> createQuestion(CreateQuestionDTO dto);

    ResponseEntity<?> updateQuestion(Long questionId, UpdateQuestionDTO dto);

    ResponseEntity<?> switchQuestionStatus(Long questionId, boolean newStatus);

    ResponseEntity<?> getAllQuestionOfQuestionGroup(Long questionGroupId,String search, int page, String column, int size, String sortType, boolean isActiveQuestion);
}
