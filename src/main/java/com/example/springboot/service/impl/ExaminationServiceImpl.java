package com.example.springboot.service.impl;

import com.example.springboot.dto.request.CreateExaminationDTO;
import com.example.springboot.entity.Examination;
import com.example.springboot.entity.UserProfile;
import com.example.springboot.exception.UserNotFoundException;
import com.example.springboot.repository.ExaminationRepository;
import com.example.springboot.repository.UserProfileRepository;
import com.example.springboot.service.ExaminationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class ExaminationServiceImpl implements ExaminationService {

    private ExaminationRepository examinationRepository;
    private UserProfileRepository userProfileRepository;
    @Override
    public ResponseEntity<?> createExamination(CreateExaminationDTO value) {
        // Get current logged in user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserProfile userProfile = userProfileRepository.findOneByLoginName(auth.getName()).orElseThrow(
                UserNotFoundException::new
        );
        Examination examination = Examination.builder()
                .examName(value.getExamName())
                .startDate(value.getStartDate())
                .endDate(value.getEndDate())
                .build();
        examination.setCreatedBy(userProfile.getLoginName());
        examinationRepository.save(examination);
        return ResponseEntity.noContent().build();
    }
}
