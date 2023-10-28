package com.example.springboot.controller;

import com.example.springboot.dto.request.UpdateClassroomDTO;
import com.example.springboot.dto.request.CreateClassroomDTO;
import com.example.springboot.service.ClassroomService;
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
import java.util.Objects;

@Validated
@RestController
@RequestMapping("/api/v1/classroom")
@Slf4j
@AllArgsConstructor
public class ClassroomController {
    private static final String DEFAULT_SEARCH = "";
    private static final String DEFAULT_PAGE = "0";
    private static final String DEFAULT_COLUMN = "id";
    private static final String DEFAULT_SIZE = "12";
    private static final String DEFAULT_SORT_INCREASE = "asc";

    private ClassroomService classroomService;

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createClassroom(@Valid @RequestBody CreateClassroomDTO DTO){
        return classroomService.createClassroom(DTO);
    }
    @DeleteMapping(value = "/delete/{classroomId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteClassroom(@PathVariable(name = "classroomId") Long classroomId){
        return classroomService.switchClassroomStatus(classroomId, false);
    }
    @PutMapping(value = "/active/{classroomId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> activeClassroom(@PathVariable(name = "classroomId") Long classroomId){
        return classroomService.switchClassroomStatus(classroomId, true);
    }
    @PutMapping(value = "/update/{classroomId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateClassroom(@PathVariable(name = "classroomId") Long classroomId,
                                             @RequestBody UpdateClassroomDTO DTO){
        return classroomService.updateClassroom(classroomId, DTO);
    }
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllEnableClassroom(
            @RequestParam(defaultValue = DEFAULT_SEARCH) String search,
            @RequestParam(defaultValue = DEFAULT_PAGE) int page,
            @RequestParam(defaultValue = DEFAULT_COLUMN) String column,
            @RequestParam(defaultValue = DEFAULT_SIZE) int size,
            @RequestParam(defaultValue = DEFAULT_SORT_INCREASE) String sortType
    ){
        return classroomService.getAllClassroomsByStatus(search, page, column, size, sortType, true);
    }
    @GetMapping(value = "/disable", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllDisableClassroom(
            @RequestParam(defaultValue = DEFAULT_SEARCH) String search,
            @RequestParam(defaultValue = DEFAULT_PAGE) int page,
            @RequestParam(defaultValue = DEFAULT_COLUMN) String column,
            @RequestParam(defaultValue = DEFAULT_SIZE) int size,
            @RequestParam(defaultValue = DEFAULT_SORT_INCREASE) String sortType
    ){
        return classroomService.getAllClassroomsByStatus(search, page, column, size, sortType, false);
    }
}