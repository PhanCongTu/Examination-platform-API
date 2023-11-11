package com.example.springboot.service;

import com.example.springboot.dto.request.CreateQuestionDTO;
import org.springframework.http.ResponseEntity;

public interface QuestionService {
    ResponseEntity<?> createQuestion(CreateQuestionDTO dto);
}
