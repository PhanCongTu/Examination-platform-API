package com.example.springboot.service.impl;

import com.example.springboot.constant.Constants;
import com.example.springboot.constant.ErrorMessage;
import com.example.springboot.dto.request.CreateQuestionGroupDTO;
import com.example.springboot.dto.request.UpdateQuestionGroupDTO;
import com.example.springboot.dto.response.QuestionGroupResponse;
import com.example.springboot.entity.Classroom;
import com.example.springboot.entity.QuestionGroup;
import com.example.springboot.entity.UserProfile;
import com.example.springboot.repository.ClassroomRepository;
import com.example.springboot.repository.QuestionGroupRepository;
import com.example.springboot.repository.QuestionRepository;
import com.example.springboot.service.QuestionGroupService;
import com.example.springboot.util.CustomBuilder;
import com.example.springboot.util.PageUtils;
import com.example.springboot.util.WebUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class QuestionGroupServiceImpl implements QuestionGroupService {
    private final QuestionGroupRepository questionGroupRepository;
    private final QuestionRepository questionRepository;
    private final ClassroomRepository classroomRepository;
    private final WebUtils webUtils;
    private static final String CODE_PREFIX = "group_";
    @Override
    public ResponseEntity<?> createQuestionGroup(CreateQuestionGroupDTO dto) {
        log.info("Start create Question Group");
        UserProfile userProfile = webUtils.getCurrentLogedInUser();
        Optional<Classroom> classRoom =  classroomRepository.findById(dto.getClassroomId());

        QuestionGroup questionGroup = new QuestionGroup();
        questionGroup.setName(dto.getName());
        questionGroup.setCode(CODE_PREFIX + dto.getCode().trim());
        questionGroup.setCreatedBy(userProfile.getLoginName());
        questionGroup.setClassRoom(classRoom.get());

        questionGroup = questionGroupRepository.save(questionGroup);
        QuestionGroupResponse response = CustomBuilder.buildQuestionGroupResponse(questionGroup);

        log.info("End create Question Group");
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> getAllQuestionGroupOfClassroom(Long classroomId, String search, int page, String column, int size, String sortType, Boolean isEnable) {
        log.info("Start get all Question Group of classroom");
        Pageable pageable = PageUtils.createPageable(page, size, sortType, column);
        Optional<Classroom> classRoom =  classroomRepository.findById(classroomId);
        if (classRoom.isEmpty()){
            return CustomBuilder.buildClassroomNotFoundResponseEntity();
        }
        String searchText = "%" + search + "%";
        Page<QuestionGroup> questionGroups = questionGroupRepository
                .findQuestionGroupsOfClassroomByClassroomId(classroomId, searchText, isEnable, pageable);

        Page<QuestionGroupResponse> response = questionGroups.map(CustomBuilder::buildQuestionGroupResponse);
        for(QuestionGroupResponse group : response) {
            Long groupID = group.getId();
            Long totalQuestionInGr = questionRepository.countQuestionsByQuestionGroupId(groupID);
            group.setTotalQuestion(totalQuestionInGr);
        }
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
        Optional<QuestionGroup> questionGroupOp = questionGroupRepository.findById(questionGroupId);
        if (questionGroupOp.isEmpty()){
            return CustomBuilder.buildQuestionGroupNotFoundResponseEntity();
        }
        QuestionGroup questionGroup = questionGroupOp.get();
        if (StringUtils.isNoneBlank(dto.getName())){
            questionGroup.setName(dto.getName());
            modifyUpdateQuestionGroup(questionGroup);
        }
        if (StringUtils.isNoneBlank(dto.getCode())){
            Optional<QuestionGroup> questionGroupEx = questionGroupRepository.findByCode(dto.getCode().trim());
            if (questionGroupEx.isPresent() && questionGroupEx.get().getId() != questionGroupId){
                LinkedHashMap<String, String> response = new LinkedHashMap<>();
                response.put(Constants.ERROR_CODE_KEY, ErrorMessage.QUESTION_GROUP_CODE_DUPLICATE.getErrorCode());
                response.put(Constants.MESSAGE_KEY, ErrorMessage.QUESTION_GROUP_CODE_DUPLICATE.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(response);
            }
            questionGroup.setCode(dto.getCode());
            modifyUpdateQuestionGroup(questionGroup);
        }
        questionGroup = questionGroupRepository.save(questionGroup);
        QuestionGroupResponse response = CustomBuilder.buildQuestionGroupResponse(questionGroup);
        log.info("End update Question Group");
        return ResponseEntity.ok(response);
    }
}
