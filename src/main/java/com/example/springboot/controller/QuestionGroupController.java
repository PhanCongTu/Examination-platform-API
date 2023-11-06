package com.example.springboot.controller;

import com.example.springboot.dto.request.CreateClassroomDTO;
import com.example.springboot.dto.request.CreateQuestionGroupDTO;
import com.example.springboot.dto.request.UpdateClassroomDTO;
import com.example.springboot.dto.request.UpdateQuestionGroupDTO;
import com.example.springboot.service.QuestionGroupService;
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
@RequestMapping("/api/v1/question-group")
@Slf4j
@AllArgsConstructor
public class QuestionGroupController {

    private static final String DEFAULT_SEARCH = "";
    private static final String DEFAULT_PAGE = "0";
    private static final String DEFAULT_COLUMN = "id";
    private static final String DEFAULT_SIZE = "12";
    private static final String DEFAULT_SORT_INCREASE = "asc";

    private final QuestionGroupService questionGroupService;

    @GetMapping(value = "/classroom/{classroomId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<?> getAllActiveQuestionGroupOfClassroom(
            @PathVariable(name = "classroomId") Long classroomId,
            @RequestParam(defaultValue = DEFAULT_PAGE) int page,
            @RequestParam(defaultValue = DEFAULT_COLUMN) String column,
            @RequestParam(defaultValue = DEFAULT_SIZE) int size,
            @RequestParam(defaultValue = DEFAULT_SORT_INCREASE) String sortType){
        return questionGroupService.getAllQuestionGroupOfClassroom(classroomId, page, column, size, sortType, true);
    }

    @GetMapping(value = "/inactive/classroom/{classroomId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<?> getAllInactiveQuestionGroupOfClassroom(
            @PathVariable(name = "classroomId") Long classroomId,
            @RequestParam(defaultValue = DEFAULT_PAGE) int page,
            @RequestParam(defaultValue = DEFAULT_COLUMN) String column,
            @RequestParam(defaultValue = DEFAULT_SIZE) int size,
            @RequestParam(defaultValue = DEFAULT_SORT_INCREASE) String sortType){
        return questionGroupService.getAllQuestionGroupOfClassroom(classroomId, page, column, size, sortType, false);
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<?> createQuestionGroup(@Valid @RequestBody CreateQuestionGroupDTO DTO){
        return questionGroupService.createQuestionGroup(DTO);
    }

    @DeleteMapping(value = "/delete/{questionGroupId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<?> deleteQuestionGroup(@PathVariable(name = "questionGroupId") Long questionGroupId){
        return questionGroupService.switchQuestionGroupStatus(questionGroupId, false);
    }

    @PutMapping(value = "/active/{questionGroupId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<?> activeQuestionGroup(@PathVariable(name = "questionGroupId") Long questionGroupId){
        return questionGroupService.switchQuestionGroupStatus(questionGroupId, true);
    }

    @PutMapping(value = "/update/{questionGroupId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateQuestionGroup(@PathVariable(name = "questionGroupId") Long questionGroupId,
                                                 @Valid @RequestBody UpdateQuestionGroupDTO DTO){
        return questionGroupService.updateQuestionGroup(questionGroupId, DTO);
    }
}
