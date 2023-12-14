package com.example.springboot.validate.impl;

import com.example.springboot.constant.Constants;
import com.example.springboot.constant.ErrorMessage;
import com.example.springboot.dto.request.UpdateQuestionGroupDTO;
import com.example.springboot.repository.ClassroomRepository;
import com.example.springboot.repository.QuestionGroupRepository;
import com.example.springboot.validate.ValidateUpdateQuestionGroup;
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
public class ValidateUpdateQuestionGroupImpl implements ConstraintValidator<ValidateUpdateQuestionGroup, UpdateQuestionGroupDTO> {

    private final QuestionGroupRepository questionGroupRepository;
    private final ClassroomRepository classroomRepository;
    private static final String CODE = "code";
    private static final String NAME = "name";
    private static final String CLASSROOM_ID = "classroomId";
    private static final String CODE_PREFIX = "group_";
    private static final String DESCRIPTION = "description";

    @Override
    public boolean isValid(UpdateQuestionGroupDTO value, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        boolean checkCode = validateCode(value, context);
        return ValidateUtils.isAllTrue(List.of(
                checkCode
        ));
    }

    private boolean validateCode(UpdateQuestionGroupDTO value, ConstraintValidatorContext context) {
        log.info("Start validate question group code");
        String code = value.getCode();
        if(StringUtils.isNoneBlank(code)){
            code = StringUtils.deleteWhitespace(code);
            code = code.substring(0, Math.min(code.length(), Constants.CODE_MAX_LENGTH));
            value.setCode(code);
        }
//        if(questionGroupRepository.findByCode(code).isPresent()){
//            context.buildConstraintViolationWithTemplate(ErrorMessage.QUESTION_GROUP_CODE_DUPLICATE.name())
//                    .addPropertyNode(CODE)
//                    .addConstraintViolation();
//            return Boolean.FALSE;
//        }
        log.info("End validate question group code");
        return Boolean.TRUE;
    }
}
