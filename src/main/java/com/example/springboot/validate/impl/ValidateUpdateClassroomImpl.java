package com.example.springboot.validate.impl;

import com.example.springboot.constant.ErrorMessage;
import com.example.springboot.dto.request.UpdateClassroomDTO;
import com.example.springboot.repository.ClassroomRepository;
import com.example.springboot.validate.ValidateCreateClassroom;
import com.example.springboot.validate.ValidateUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.Objects;

@Slf4j
@AllArgsConstructor
public class ValidateUpdateClassroomImpl implements ConstraintValidator<ValidateCreateClassroom, UpdateClassroomDTO> {
    private ClassroomRepository classRoomRepository;
    private static final String CLASS_NAME = "className";
    private static final String CLASS_CODE = "classCode";
    private static final String IS_PRIVATE = "is_private";
    private static final String CODE_PREFIX = "classroom_";
    @Override
    public boolean isValid(UpdateClassroomDTO value, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        boolean checkClassCode = validateClassCode(value, context);
        return ValidateUtils.isAllTrue(List.of(
                checkClassCode
        ));
    }

    private boolean validateClassCode(UpdateClassroomDTO value, ConstraintValidatorContext context) {
        if(Objects.nonNull(value.getClassCode()) && classRoomRepository.findByClassCode(CODE_PREFIX + value.getClassCode()).isPresent()){
            context.buildConstraintViolationWithTemplate(ErrorMessage.CLASS_CODE_DUPLICATE.name())
                    .addPropertyNode(CLASS_CODE)
                    .addConstraintViolation();
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}