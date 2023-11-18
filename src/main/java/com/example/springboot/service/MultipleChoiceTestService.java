package com.example.springboot.service;

import com.example.springboot.dto.request.CreateMultipleChoiceTestDTO;
import com.example.springboot.exception.QuestionNotFoundException;
import org.springframework.http.ResponseEntity;

public interface MultipleChoiceTestService {
    ResponseEntity<?> createMultipleChoiceTest(CreateMultipleChoiceTestDTO dto) throws QuestionNotFoundException;
}
