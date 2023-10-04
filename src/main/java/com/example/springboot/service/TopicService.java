package com.example.springboot.service;

import com.example.springboot.dto.request.CreateTopicDTO;
import org.springframework.http.ResponseEntity;

public interface TopicService {
    ResponseEntity<?> createTopic(CreateTopicDTO topicDTO);
}
