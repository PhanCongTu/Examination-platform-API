package com.example.springboot.service.impl;

import com.example.springboot.constant.Constants;
import com.example.springboot.constant.ErrorMessage;
import com.example.springboot.dto.request.CreateTopicDTO;
import com.example.springboot.dto.response.TopicResponseDTO;
import com.example.springboot.entity.Topic;
import com.example.springboot.entity.UserProfile;
import com.example.springboot.repository.TopicRepository;
import com.example.springboot.service.TopicService;
import com.example.springboot.util.WebUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class TopicServiceImpl implements TopicService {

    private final TopicRepository topicRepository;
    private final WebUtils webUtils;
    private static final String CODE_PREFIX = "topic_";
    private static final Integer PAGE_SIZE = 1;
    private static final String PAGE_SORT_BY = "code";

    /**
     * Create a new topic
     *
     * @param topicDTO : The DTO contains the data
     * @return : The {@link TopicResponseDTO}
     */
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


    /**
     * Change status of the topic by id
     *
     * @param topicId : the topic id
     * @param newStatus : new boolean status
     * @return : no content response
     */
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

    /**
     * Get all the enable topic
     *
     * @return the list of DTO response {@link TopicResponseDTO}
     */
    @Override
    public ResponseEntity<?> getAllEnableTopics(Integer pageNumber) {
        log.info("Start get all topic (non-Admin)");
        Pageable sortedByPriceDescNameAsc =
                PageRequest.of(pageNumber, PAGE_SIZE, Sort.by(PAGE_SORT_BY).descending());
        List<Topic> topics = topicRepository.findAllEnableTopics(sortedByPriceDescNameAsc);
        // Map topic to topic response DTO
        List<TopicResponseDTO> response =  topics.stream().map((topic)-> new TopicResponseDTO(
                topic.getId(),
                topic.getCode(),
                topic.getTopicName()
        )).collect(Collectors.toList());
        log.info("End get all topic (non-Admin)");
        return ResponseEntity.ok(response);
    }

    /**
     * Update modify information of topic
     *
     * @param topic the entity
     */
    private void modifyUpdateTopic(Topic topic) {
        UserProfile userProfile = webUtils.getCurrentLogedInUser();
        topic.setUpdateBy(userProfile.getLoginName());
        topic.setUpdateDate(Instant.now());
    }

    /**
     * Build an error response when the topic is not found
     *
     * @return the response
     */
    private ResponseEntity<LinkedHashMap<String, String>> buildTopicNotFound() {
        LinkedHashMap<String, String> response = new LinkedHashMap<>();
        response.put(Constants.ERROR_CODE_KEY, ErrorMessage.TOPIC_NOT_FOUND.getErrorCode());
        response.put(Constants.MESSAGE_KEY, ErrorMessage.TOPIC_NOT_FOUND.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
}
