package com.example.springboot.service.impl;

import com.example.springboot.constant.Constants;
import com.example.springboot.constant.ErrorMessage;
import com.example.springboot.dto.request.CreateExaminationDTO;
import com.example.springboot.entity.Examination;
import com.example.springboot.entity.Topic;
import com.example.springboot.entity.UserProfile;
import com.example.springboot.exception.UserNotFoundException;
import com.example.springboot.repository.ExaminationRepository;
import com.example.springboot.repository.TopicRepository;
import com.example.springboot.repository.UserProfileRepository;
import com.example.springboot.service.ExaminationService;
import com.example.springboot.util.WebUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class ExaminationServiceImpl implements ExaminationService {

    private final ExaminationRepository examinationRepository;
    private final UserProfileRepository userProfileRepository;
    private final TopicRepository topicRepository;
    private final WebUtils webUtils;
    @Override
    public ResponseEntity<?> createExamination(CreateExaminationDTO value) {
        // Get current logged in user
        UserProfile userProfile = webUtils.getCurrentLogedInUser();

        Optional<Topic> topic = topicRepository.findById(value.getTopicId());
        if (topic.isEmpty()){
            LinkedHashMap<String, String> response = new LinkedHashMap<>();
            response.put(Constants.ERROR_CODE_KEY, ErrorMessage.CREATE_EXAM_TOPIC_ID_INVALID.getErrorCode());
            response.put(Constants.MESSAGE_KEY, ErrorMessage.CREATE_EXAM_TOPIC_ID_INVALID.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        }
        Examination examination = Examination.builder()
                .examName(value.getExamName())
                .startDate(value.getStartDate())
                .endDate(value.getEndDate())
                .topic(topic.get())
                .build();
        examination.setCreatedBy(userProfile.getLoginName());
        examinationRepository.save(examination);
        return ResponseEntity.noContent().build();
    }
}
