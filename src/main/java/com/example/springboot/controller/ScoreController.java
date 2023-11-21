package com.example.springboot.controller;

import com.example.springboot.dto.request.CreateQuestionDTO;
import com.example.springboot.dto.request.SubmitMCTestDTO;
import com.example.springboot.service.ScoreService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping("/api/v1/score")
@Slf4j
@AllArgsConstructor
public class ScoreController {

    private final ScoreService scoreService;

    @PostMapping(value = "/submit-test", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> submitTest(@Valid @RequestBody SubmitMCTestDTO DTO){
        return scoreService.submitTest(DTO);
    }
}
