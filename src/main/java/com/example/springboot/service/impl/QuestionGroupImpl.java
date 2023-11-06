package com.example.springboot.service.impl;

import com.example.springboot.dto.request.CreateQuestionGroupDTO;
import com.example.springboot.dto.request.UpdateQuestionGroupDTO;
import com.example.springboot.dto.response.QuestionGroupResponse;
import com.example.springboot.entity.ClassRoom;
import com.example.springboot.entity.QuestionGroup;
import com.example.springboot.entity.UserProfile;
import com.example.springboot.repository.ClassroomRepository;
import com.example.springboot.repository.QuestionGroupRepository;
import com.example.springboot.service.QuestionGroupService;
import com.example.springboot.util.CustomBuilder;
import com.example.springboot.util.PageUtils;
import com.example.springboot.util.WebUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class QuestionGroupImpl implements QuestionGroupService {
    private final QuestionGroupRepository questionGroupRepository;
    private final ClassroomRepository classroomRepository;
    private final WebUtils webUtils;
    private static final String CODE_PREFIX = "group_";
    @Override
    public ResponseEntity<?> createQuestionGroup(CreateQuestionGroupDTO dto) {
        log.info("Start create Question Group");
        UserProfile userProfile = webUtils.getCurrentLogedInUser();
        Optional<ClassRoom> classRoom =  classroomRepository.findById(dto.getClassroomId());

        QuestionGroup questionGroup = new QuestionGroup();
        questionGroup.setName(dto.getName());
        questionGroup.setCode(CODE_PREFIX + dto.getCode());
        questionGroup.setCreatedBy(userProfile.getLoginName());
        questionGroup.setClassRoom(classRoom.get());

        questionGroup = questionGroupRepository.save(questionGroup);
        QuestionGroupResponse response = CustomBuilder.builtQuestionGroupResponse(questionGroup);

        log.info("End create Question Group");
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> getAllQuestionGroupOfClassroom(Long classroomId, int page, String column, int size, String sortType, Boolean isEnable) {
        log.info("Start get all Question Group of classroom");
        Pageable pageable = PageUtils.createPageable(page, size, sortType, column);
        Optional<ClassRoom> classRoom =  classroomRepository.findById(classroomId);
        if (classRoom.isEmpty()){
            return CustomBuilder.buildClassroomNotFoundResponseEntity();
        }
        Page<QuestionGroup> questionGroups = questionGroupRepository
                .findQuestionGroupsOfClassroomByClassroomId(classroomId, isEnable, pageable);

        Page<QuestionGroupResponse> response = questionGroups.map(CustomBuilder::builtQuestionGroupResponse);
        log.info("End get all Question Group of classroom");
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> switchQuestionGroupStatus(Long questionGroupId, boolean newStatus) {
        log.info("Start switch Question Group status to " + newStatus);

        Optional<QuestionGroup> questionGroup = questionGroupRepository.findById(questionGroupId);

        if (questionGroup.isEmpty()){
            return CustomBuilder.buildQuestionGroupNotFoundResponseEntity();
        }

        questionGroup.get().setIsEnable(newStatus);
        modifyUpdateQuestionGroup(questionGroup.get());
        questionGroupRepository.save(questionGroup.get());
        log.info("End switch Question Group status to " + newStatus);
        return ResponseEntity.noContent().build();
    }

    private void modifyUpdateQuestionGroup(QuestionGroup questionGroup) {
        UserProfile userProfile = webUtils.getCurrentLogedInUser();
        questionGroup.setUpdateBy(userProfile.getLoginName());
        questionGroup.setUpdateDate(Instant.now());
    }

    @Override
    public ResponseEntity<?> updateQuestionGroup(Long questionGroupId, UpdateQuestionGroupDTO dto) {
        log.info("Start update Question Group");
        Optional<QuestionGroup> questionGroup = questionGroupRepository.findById(questionGroupId);
        if (questionGroup.isEmpty()){
            return CustomBuilder.buildQuestionGroupNotFoundResponseEntity();
        }
        if (StringUtils.isNoneBlank(dto.getName())){
            questionGroup.get().setName(dto.getName());
            modifyUpdateQuestionGroup(questionGroup.get());
        }
        if (StringUtils.isNoneBlank(dto.getCode())){
            questionGroup.get().setCode(CODE_PREFIX + dto.getCode());
            modifyUpdateQuestionGroup(questionGroup.get());
        }
        questionGroupRepository.save(questionGroup.get());
        log.info("End update Question Group");
        return ResponseEntity.noContent().build();
    }
}
