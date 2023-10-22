package com.example.springboot.service;

import com.example.springboot.dto.request.CreateObjectiveTestDTO;
import org.springframework.http.ResponseEntity;

public interface ObjectiveTestService {
    ResponseEntity<?> createExamination(CreateObjectiveTestDTO value);
}
