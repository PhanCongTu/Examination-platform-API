package com.example.springboot.validate.impl;

import com.example.springboot.constant.Constants;
import com.example.springboot.constant.ErrorMessage;
import com.example.springboot.dto.request.CreateClassroomDTO;
import com.example.springboot.repository.ClassroomRepository;
import com.example.springboot.validate.ValidateCreateClassroom;
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
public class ValidateCreateClassroomImpl implements ConstraintValidator<ValidateCreateClassroom, CreateClassroomDTO> {

    private ClassroomRepository classRoomRepository;
    private static final String CLASS_NAME = "className";
    private static final String CLASS_CODE = "classCode";
    private static final String IS_PRIVATE = "is_private";
    private static final String CODE_PREFIX = "classroom_";

    @Override
    public boolean isValid(CreateClassroomDTO value, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        boolean checkClassName = validateClassName(value, context);
        boolean checkClassCode = validateClassCode(value, context);
        boolean checkIsPrivate = validateIsPrivate(value, context);
        return ValidateUtils.isAllTrue(List.of(
                checkClassName,
                checkClassCode,
                checkIsPrivate
        ));
    }

    private boolean validateIsPrivate(CreateClassroomDTO value, ConstraintValidatorContext context) {
        if(Objects.isNull(value.getIsPrivate())){
            value.setIsPrivate(true);
        }
        return Boolean.TRUE;
    }

    private boolean validateClassCode(CreateClassroomDTO value, ConstraintValidatorContext context) {
        String classCode = value.getClassCode();
        if(Objects.isNull(classCode) || classCode.isBlank()){
            context.buildConstraintViolationWithTemplate(ErrorMessage.COMMON_FIELD_REQUIRED.name())
                    .addPropertyNode(CLASS_CODE)
                    .addConstraintViolation();
            return Boolean.FALSE;
        }


        if(StringUtils.isNoneBlank(classCode)){
            classCode = StringUtils.deleteWhitespace(classCode);
            classCode = classCode.substring(0, Math.min(classCode.length(), Constants.CODE_MAX_LENGTH));
            value.setClassCode(classCode);
        }

        if(classRoomRepository.findByClassCode(CODE_PREFIX + classCode).isPresent()){
            context.buildConstraintViolationWithTemplate(ErrorMessage.CLASS_CODE_DUPLICATE.name())
                    .addPropertyNode(CLASS_CODE)
                    .addConstraintViolation();
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    private boolean validateClassName(CreateClassroomDTO value, ConstraintValidatorContext context) {
        if(Objects.isNull(value.getClassName()) || value.getClassName().isBlank()){
            context.buildConstraintViolationWithTemplate(ErrorMessage.COMMON_FIELD_REQUIRED.name())
                    .addPropertyNode(CLASS_NAME)
                    .addConstraintViolation();
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }


}