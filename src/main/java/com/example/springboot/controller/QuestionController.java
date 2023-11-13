package com.example.springboot.controller;

import com.example.springboot.dto.request.CreateQuestionDTO;
import com.example.springboot.dto.request.UpdateQuestionDTO;
import com.example.springboot.dto.request.UpdateQuestionGroupDTO;
import com.example.springboot.service.QuestionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping("/api/v1/question")
@Slf4j
@AllArgsConstructor
public class QuestionController {
    private static final String DEFAULT_SEARCH = "";
    private static final String DEFAULT_PAGE = "0";
    private static final String DEFAULT_COLUMN = "id";
    private static final String DEFAULT_SIZE = "12";
    private static final String DEFAULT_SORT_INCREASE = "asc";

    private QuestionService questionService;
    @GetMapping(value = "/question-group/{questionGroupId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<?> getAllActiveQuestionOfQuestionGroup(
            @PathVariable(name = "questionGroupId") Long questionGroupId,
            @RequestParam(defaultValue = DEFAULT_SEARCH) String search,
            @RequestParam(defaultValue = DEFAULT_PAGE) int page,
            @RequestParam(defaultValue = DEFAULT_COLUMN) String column,
            @RequestParam(defaultValue = DEFAULT_SIZE) int size,
            @RequestParam(defaultValue = DEFAULT_SORT_INCREASE) String sortType){
        return questionService.getAllQuestionOfQuestionGroup(questionGroupId, search, page, column, size, sortType, true);
    }
    @GetMapping(value = "/inactive/question-group/{questionGroupId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<?> getAllInactiveQuestionOfQuestionGroup(
            @PathVariable(name = "questionGroupId") Long questionGroupId,
            @RequestParam(defaultValue = DEFAULT_SEARCH) String search,
            @RequestParam(defaultValue = DEFAULT_PAGE) int page,
            @RequestParam(defaultValue = DEFAULT_COLUMN) String column,
            @RequestParam(defaultValue = DEFAULT_SIZE) int size,
            @RequestParam(defaultValue = DEFAULT_SORT_INCREASE) String sortType){
        return questionService.getAllQuestionOfQuestionGroup(questionGroupId, search, page, column, size, sortType, false);
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<?> createQuestion(@Valid @RequestBody CreateQuestionDTO DTO){
        return questionService.createQuestion(DTO);
    }
    @PutMapping(value = "/update/{questionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<?> updateQuestion(@PathVariable(name = "questionId") Long questionId,
                                                 @Valid @RequestBody UpdateQuestionDTO DTO){
        return questionService.updateQuestion(questionId, DTO);
    }
    @DeleteMapping(value = "/delete/{questionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<?> deleteQuestion(@PathVariable(name = "questionId") Long questionId){
        return questionService.switchQuestionStatus(questionId, false);
    }

    @PutMapping(value = "/active/{questionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<?> activeClassroom(@PathVariable(name = "questionId") Long questionId){
        return questionService.switchQuestionStatus(questionId, true);
    }
}
