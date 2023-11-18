package com.example.springboot.service;

import com.example.springboot.dto.request.CreateQuestionDTO;
import com.example.springboot.dto.request.UpdateQuestionDTO;
import com.example.springboot.entity.Question;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface QuestionService {
    List<Question> getRandomQuestionsByQuestionGroup(Long questionGroupId, Long numberOfQuestion);

    ResponseEntity<?> createQuestion(CreateQuestionDTO dto);

    ResponseEntity<?> updateQuestion(Long questionId, UpdateQuestionDTO dto);

    ResponseEntity<?> switchQuestionStatus(Long questionId, boolean newStatus);

    ResponseEntity<?> getAllQuestionOfQuestionGroup(Long questionGroupId,String search, int page, String column, int size, String sortType, boolean isActiveQuestion);
}
