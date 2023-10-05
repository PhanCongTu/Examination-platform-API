package com.example.springboot.service.impl;

import com.example.springboot.constant.Constants;
import com.example.springboot.constant.ErrorMessage;
import com.example.springboot.dto.request.CreateTopicDTO;
import com.example.springboot.dto.response.TopicResponseDTO;
import com.example.springboot.entity.Topic;
import com.example.springboot.entity.UserProfile;
import com.example.springboot.exception.UserNotFoundException;
import com.example.springboot.repository.TopicRepository;
import com.example.springboot.repository.UserProfileRepository;
import com.example.springboot.service.TopicService;
import com.example.springboot.util.WebUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class TopicServiceImpl implements TopicService {
    private TopicRepository topicRepository;
    private UserProfileRepository userProfileRepository;
    private WebUtils webUtils;
    private static final String CODE_PREFIX = "topic_";
    @Override
    public ResponseEntity<?> createTopic(CreateTopicDTO topicDTO) {
        log.info("Start create topic");
        UserProfile userProfile = webUtils.getCurrentLogedInUser();
        Topic topic = new Topic();
        topic.setTopicName(topicDTO.getTopicName());
        topic.setCode(CODE_PREFIX + topicDTO.getCode());
        topic.setCreatedBy(userProfile.getLoginName());
        Topic savedTopic = topicRepository.save(topic);
        TopicResponseDTO response = TopicResponseDTO.builder()
                .id(savedTopic.getId())
                .topicName(savedTopic.getTopicName())
                .code(savedTopic.getCode())
                .build();;
        log.info("End create topic");
        return ResponseEntity.ok(response);
    }



    @Override
    public ResponseEntity<?> switchTopicStatus(Long topicId, Boolean newStatus) {
        log.info("Start switch topic status to " + newStatus);
        Optional<Topic> value = topicRepository.findById(topicId);
        if (value.isEmpty()){
            return buildTopicNotFound();
        }
        Topic topic = value.get();
        topic.setIsEnable(newStatus);
        modifyUpdateTopic(topic);
        topicRepository.save(topic);
        log.info("End switch topic status to " + newStatus);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<?> getAllTopics() {
        log.info("Start get all topic (non-Admin)");
        List<Object> topics = topicRepository.findAllTopics();
        if (topics.isEmpty()){
            log.error("NULLLLLLLLLLLL");
            return ResponseEntity.badRequest().build();
        }
        log.error(topics.toString());
        log.info("End get all topic (non-Admin)");
        return ResponseEntity.ok(topics);
    }

    private void modifyUpdateTopic(Topic topic) {
        UserProfile userProfile = webUtils.getCurrentLogedInUser();
        topic.setUpdateBy(userProfile.getLoginName());
        topic.setUpdateDate(Instant.now());
    }

    private ResponseEntity<LinkedHashMap<String, String>> buildTopicNotFound() {
        LinkedHashMap<String, String> response = new LinkedHashMap<>();
        response.put(Constants.ERROR_CODE_KEY, ErrorMessage.TOPIC_NOT_FOUND.getErrorCode());
        response.put(Constants.MESSAGE_KEY, ErrorMessage.TOPIC_NOT_FOUND.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
}
