package com.example.springboot.service;

import com.example.springboot.dto.request.CreateExaminationDTO;
import org.springframework.http.ResponseEntity;

public interface ExaminationService {
    ResponseEntity<?> createExamination(CreateExaminationDTO value);
}
