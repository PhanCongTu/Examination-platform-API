package com.example.springboot.service.impl;

import com.example.springboot.dto.request.CreateObjectiveTestDTO;
import com.example.springboot.repository.ObjectiveTestRepository;
import com.example.springboot.repository.ClassRoomRepository;
import com.example.springboot.repository.UserProfileRepository;
import com.example.springboot.service.ObjectiveTestService;
import com.example.springboot.util.WebUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class ObjectiveTestServiceImpl implements ObjectiveTestService {

    private final ObjectiveTestRepository objectiveTestRepository;
    private final UserProfileRepository userProfileRepository;
    private final ClassRoomRepository classRoomRepository;
    private final WebUtils webUtils;

    @Override
    public ResponseEntity<?> createExamination(CreateObjectiveTestDTO value) {
        return null;
    }

}
