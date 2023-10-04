package com.example.springboot.service.impl;

import com.example.springboot.dto.request.CreateTopicDTO;
import com.example.springboot.entity.Topic;
import com.example.springboot.entity.UserProfile;
import com.example.springboot.exception.UserNotFoundException;
import com.example.springboot.repository.TopicRepository;
import com.example.springboot.repository.UserProfileRepository;
import com.example.springboot.service.TopicService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class TopicServiceImpl implements TopicService {
    private TopicRepository topicRepository;
    private UserProfileRepository userProfileRepository;
    @Override
    public ResponseEntity<?> createTopic(CreateTopicDTO topicDTO) {
        log.info("Start create topic");
        // Get current logged in user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserProfile userProfile = userProfileRepository.findOneByLoginName(auth.getName()).orElseThrow(
                UserNotFoundException::new
        );
        Topic topic = new Topic();
        topic.setTopicName(topicDTO.getTopicName());
        topic.setCode(topicDTO.getCode());
        topic.setCreatedBy(userProfile.getLoginName());
        topicRepository.save(topic);
        log.info("End create topic");
        return ResponseEntity.noContent().build();
    }
}
