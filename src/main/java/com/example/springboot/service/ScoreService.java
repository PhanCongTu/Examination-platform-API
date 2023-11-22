package com.example.springboot.service;

import com.example.springboot.dto.request.SubmitMCTestDTO;
import org.springframework.http.ResponseEntity;

public interface ScoreService {
    ResponseEntity<?> submitTest(SubmitMCTestDTO dto);

    ResponseEntity<?> getAllStudentScoreOfTest(Long testId, String search, int page, String column, int size, String sortType);

}
