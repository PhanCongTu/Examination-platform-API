package com.example.springboot.controller;

import com.example.springboot.dto.request.CreateTopicDTO;
import com.example.springboot.dto.request.SignUpRequestDTO;
import com.example.springboot.repository.UserProfileRepository;
import com.example.springboot.service.TopicService;
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
@RequestMapping("/api/v1/topic")
@Slf4j
@AllArgsConstructor
public class TopicController {
    private TopicService topicService;

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createTopic(@Valid @RequestBody CreateTopicDTO topicDTO){
        log.info("vao");
        return topicService.createTopic(topicDTO);
    }
}
