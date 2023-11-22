package com.example.springboot.validate.impl;

import com.example.springboot.constant.ErrorMessage;
import com.example.springboot.dto.request.GetScoreOfStudentDTO;
import com.example.springboot.validate.ValidateGetScoreOfStudent;
import com.example.springboot.validate.ValidateUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.Objects;

@Slf4j
@AllArgsConstructor
public class ValidateGetScoreOfStudentImpl implements ConstraintValidator<ValidateGetScoreOfStudent, GetScoreOfStudentDTO> {

    private final String STUDENT_ID = "studentId";
    private final String TEST_ID = "multipleChoiceTestId";
    @Override
    public boolean isValid(GetScoreOfStudentDTO value, ConstraintValidatorContext context) {

        context.disableDefaultConstraintViolation();
        boolean checkStudentId = validateStudentId(value, context);
        boolean checkTestId = validateTestId(value, context);
        return ValidateUtils.isAllTrue(List.of(
                checkStudentId,
                checkTestId
        ));
    }

    private boolean validateTestId(GetScoreOfStudentDTO value, ConstraintValidatorContext context) {
        if(Objects.isNull(value.getMultipleChoiceTestId())){
            context.buildConstraintViolationWithTemplate(ErrorMessage.COMMON_FIELD_REQUIRED.name())
                    .addPropertyNode(TEST_ID)
                    .addConstraintViolation();
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    private boolean validateStudentId(GetScoreOfStudentDTO value, ConstraintValidatorContext context) {
        if(Objects.isNull(value.getStudentId())){
            context.buildConstraintViolationWithTemplate(ErrorMessage.COMMON_FIELD_REQUIRED.name())
                    .addPropertyNode(STUDENT_ID)
                    .addConstraintViolation();
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}
