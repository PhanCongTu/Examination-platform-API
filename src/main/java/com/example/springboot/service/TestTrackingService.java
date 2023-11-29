package com.example.springboot.service;

import org.springframework.http.ResponseEntity;

public interface TestTrackingService {
    ResponseEntity<?> getTestingInProgress(Long testId);

    ResponseEntity<?> createTestingInProcess(Long testId);
}
