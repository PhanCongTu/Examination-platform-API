package com.example.springboot.controller;

import com.example.springboot.dto.request.CreateQuestionDTO;
import com.example.springboot.dto.request.GetScoreOfStudentDTO;
import com.example.springboot.dto.request.SubmitMCTestDTO;
import com.example.springboot.service.ScoreService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping("/api/v1/score")
@Slf4j
@AllArgsConstructor
public class ScoreController {
    private static final String DEFAULT_SEARCH = "";
    private static final String DEFAULT_PAGE = "0";
    private static final String DEFAULT_COLUMN = "id";
    private static final String DEFAULT_SIZE = "12";
    private static final String DEFAULT_SORT_INCREASE = "asc";
    private final ScoreService scoreService;

    @PostMapping(value = "/submit-test", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> submitTest(@Valid @RequestBody SubmitMCTestDTO DTO){
        return scoreService.submitTest(DTO);
    }
    @GetMapping(value = "/multiple-choice-test/{testId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<?> getAllStudentScoreOfTest(
            @PathVariable(name = "testId") Long testId,
            @RequestParam(defaultValue = DEFAULT_SEARCH) String search,
            @RequestParam(defaultValue = DEFAULT_PAGE) int page,
            @RequestParam(defaultValue = DEFAULT_COLUMN) String column,
            @RequestParam(defaultValue = DEFAULT_SIZE) int size,
            @RequestParam(defaultValue = DEFAULT_SORT_INCREASE) String sortType){
        return scoreService.getAllStudentScoreOfTest(testId, search, page, column, size, sortType);
    }

    @GetMapping(value = "/student", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<?> getScoreOfStudent(@Valid @RequestBody GetScoreOfStudentDTO dto){
        return scoreService.getScoreOfStudent(dto.getStudentId(), dto.getMultipleChoiceTestId());
    }
}
