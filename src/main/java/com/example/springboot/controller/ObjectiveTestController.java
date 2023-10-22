package com.example.springboot.controller;

import com.example.springboot.dto.request.CreateObjectiveTestDTO;
import com.example.springboot.service.ObjectiveTestService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Validated
@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/api/v1/objective-test")
public class ObjectiveTestController {

    private ObjectiveTestService objectiveTestService;

    @PostMapping(value = "/create")
    public ResponseEntity<?> createObjectiveTest(@Valid @RequestBody CreateObjectiveTestDTO value) {
        return objectiveTestService.createExamination(value);
    }
}
