package com.example.springboot.controller;

import com.example.springboot.constant.Constants;
import com.example.springboot.constant.ErrorMessage;
import com.example.springboot.dto.request.CreateMultipleChoiceTestDTO;
import com.example.springboot.exception.NotEnoughQuestionException;
import com.example.springboot.exception.QuestionGroupNotFoundException;
import com.example.springboot.exception.QuestionNotFoundException;
import com.example.springboot.service.TestTrackingService;
import com.example.springboot.util.CustomBuilder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.LinkedHashMap;

@Validated
@RestController
@RequestMapping("/api/v1/test-tracking")
@Slf4j
@AllArgsConstructor
@PreAuthorize("hasRole('STUDENT')")
public class TestTrackingController {
    private final TestTrackingService testTrackingService;

    @GetMapping(value = "/my/{testId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getMultipleChoiceTest(
            @PathVariable(name = "testId") Long testId){
        return testTrackingService.
                getTestingInProgress(testId);
    }
    @PostMapping(value = "/my/create/{testId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createTestingInProcess(@PathVariable(name = "testId") Long testId){
            return testTrackingService.createTestingInProcess(testId);
    }
}
