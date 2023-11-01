package com.example.springboot.controller;

import com.example.springboot.dto.request.AddToClassroomDTO;
import com.example.springboot.dto.request.CreateClassroomDTO;
import com.example.springboot.service.ClassroomService;
import com.example.springboot.service.UserProfileService;
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
@RequestMapping("/api/v1/manage")
@Slf4j
@AllArgsConstructor
public class ManagementController {

    private static final String DEFAULT_SEARCH = "";
    private static final String DEFAULT_PAGE = "0";
    private static final String DEFAULT_COLUMN_STUDENT = "user_id";
    private static final String DEFAULT_SIZE = "12";
    private static final String DEFAULT_SORT_INCREASE = "asc";

    private ClassroomService classroomService;

    private UserProfileService userProfileService;


    @PostMapping(value = "/add-to-class", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<?> addStudentToClassroom(@Valid @RequestBody AddToClassroomDTO DTO){
        System.out.println("vao");
        return classroomService.addStudentToClassroom(DTO);
    }

    @GetMapping(value = "/students/classroom/{classroomId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<?> getAllStudentsOfClassroom(
            @PathVariable(name = "classroomId") Long classroomId,
            @RequestParam(defaultValue = DEFAULT_PAGE) int page,
            @RequestParam(defaultValue = DEFAULT_COLUMN_STUDENT) String column,
            @RequestParam(defaultValue = DEFAULT_SIZE) int size,
            @RequestParam(defaultValue = DEFAULT_SORT_INCREASE) String sortType){
        return classroomService.getAllStudentOfClassroom(classroomId, page, column, size, sortType);
    }

    @GetMapping(value = "/students", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<?> getAllActiveStudents(
            @RequestParam(defaultValue = DEFAULT_SEARCH) String search,
            @RequestParam(defaultValue = DEFAULT_PAGE) int page,
            @RequestParam(defaultValue = DEFAULT_COLUMN_STUDENT) String column,
            @RequestParam(defaultValue = DEFAULT_SIZE) int size,
            @RequestParam(defaultValue = DEFAULT_SORT_INCREASE) String sortType
    ){
        return userProfileService.getAllStudentsByStatus(search, page, column, size, sortType, true);
    }

    @GetMapping(value = "/students/unactive", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<?> getAllDisableStudents(
            @RequestParam(defaultValue = DEFAULT_SEARCH) String search,
            @RequestParam(defaultValue = DEFAULT_PAGE) int page,
            @RequestParam(defaultValue = DEFAULT_COLUMN_STUDENT) String column,
            @RequestParam(defaultValue = DEFAULT_SIZE) int size,
            @RequestParam(defaultValue = DEFAULT_SORT_INCREASE) String sortType
    ){
        return userProfileService.getAllStudentsByStatus(search, page, column, size, sortType, false);
    }
}
