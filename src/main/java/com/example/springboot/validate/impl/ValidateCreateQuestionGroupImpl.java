package com.example.springboot.validate.impl;

import com.example.springboot.constant.Constants;
import com.example.springboot.constant.ErrorMessage;
import com.example.springboot.dto.request.CreateQuestionGroupDTO;
import com.example.springboot.repository.ClassroomRepository;
import com.example.springboot.repository.QuestionGroupRepository;
import com.example.springboot.validate.ValidateCreateQuestionGroup;
import com.example.springboot.validate.ValidateUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.Objects;

@Slf4j
@AllArgsConstructor
public class ValidateCreateQuestionGroupImpl implements ConstraintValidator<ValidateCreateQuestionGroup, CreateQuestionGroupDTO> {

    private final QuestionGroupRepository questionGroupRepository;
    private final ClassroomRepository classroomRepository;
    private static final String CODE = "code";
    private static final String NAME = "name";
    private static final String CLASSROOM_ID = "classroomId";
    private static final String CODE_PREFIX = "group_";
    @Override
    public boolean isValid(CreateQuestionGroupDTO value, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        boolean checkCode = validateQuestionGroupCode(value, context);
        boolean checkName = validateQuestionGroupName(value, context);
        boolean checkClassroomId = validateQuestionGroupClassroomId(value, context);
        return ValidateUtils.isAllTrue(List.of(
                checkCode,
                checkName,
                checkClassroomId
        ));
    }

    private boolean validateQuestionGroupClassroomId(CreateQuestionGroupDTO value, ConstraintValidatorContext context) {
        log.info("Start validate classroomId when creating question group");
        if(Objects.isNull(value.getClassroomId())){
            context.buildConstraintViolationWithTemplate(ErrorMessage.COMMON_FIELD_REQUIRED.name())
                    .addPropertyNode(CLASSROOM_ID)
                    .addConstraintViolation();
            return Boolean.FALSE;
        }
        if(classroomRepository.findById(value.getClassroomId()).isEmpty()){
            context.buildConstraintViolationWithTemplate(ErrorMessage.CLASSROOM_NOT_FOUND.name())
                    .addPropertyNode(CLASSROOM_ID)
                    .addConstraintViolation();
            return Boolean.FALSE;
        }
        log.info("End validate classroomId when creating question group");
        return Boolean.TRUE;
    }

    private boolean validateQuestionGroupName(CreateQuestionGroupDTO value, ConstraintValidatorContext context) {
        log.info("Start validate name when creating question group");
        if(Objects.isNull(value.getCode()) || value.getCode().isBlank()){
            context.buildConstraintViolationWithTemplate(ErrorMessage.COMMON_FIELD_REQUIRED.name())
                    .addPropertyNode(NAME)
                    .addConstraintViolation();
            return Boolean.FALSE;
        }
        log.info("End validate name when creating question group");
        return Boolean.TRUE;
    }

    private boolean validateQuestionGroupCode(CreateQuestionGroupDTO value, ConstraintValidatorContext context) {
        log.info("Start validate code when creating question group");
        String code = value.getCode();
        if(Objects.isNull(code) || code.isBlank()){
            context.buildConstraintViolationWithTemplate(ErrorMessage.COMMON_FIELD_REQUIRED.name())
                    .addPropertyNode(CODE)
                    .addConstraintViolation();
            return Boolean.FALSE;
        }

        if(StringUtils.isNoneBlank(code)){
            code = StringUtils.deleteWhitespace(code);
            code = code.substring(0, Math.min(code.length(), Constants.CODE_MAX_LENGTH));
            value.setCode(code);
        }

        if(questionGroupRepository.findByCode(CODE_PREFIX + code).isPresent()){
            context.buildConstraintViolationWithTemplate(ErrorMessage.QUESTION_GROUP_CODE_DUPLICATE.name())
                    .addPropertyNode(CODE)
                    .addConstraintViolation();
            return Boolean.FALSE;
        }
        log.info("End validate code when creating question group");
        return Boolean.TRUE;
    }
}
