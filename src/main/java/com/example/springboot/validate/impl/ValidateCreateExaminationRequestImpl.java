package com.example.springboot.validate.impl;

import com.example.springboot.constant.ErrorMessage;
import com.example.springboot.dto.request.CreateExaminationDTO;
import com.example.springboot.validate.ValidateCreateExaminationRequest;
import com.example.springboot.validate.ValidateUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@AllArgsConstructor
public class ValidateCreateExaminationRequestImpl implements ConstraintValidator<ValidateCreateExaminationRequest, CreateExaminationDTO> {

    public static final String EXAM_NAME = "examName";
    public static final String START_DATE = "startDate";
    public static final String END_DATE = "endDate";


    @Override
    public boolean isValid(CreateExaminationDTO value, ConstraintValidatorContext context) {
        log.info("Start validate CreateExaminationDTO");
        context.disableDefaultConstraintViolation();
        boolean checkExamName = validateExamName(value, context);
        boolean checkStartDate = validateStartDate(value, context);
        boolean checkEndDate = validateEndDate(value, context);
        return ValidateUtils.isAllTrue(List.of(
                checkExamName,
                checkStartDate,
                checkEndDate
        ));
    }

    private boolean validateExamName(CreateExaminationDTO value, ConstraintValidatorContext context) {
        if(Objects.isNull(value.getExamName()) || value.getExamName().isBlank()){
            context.buildConstraintViolationWithTemplate(ErrorMessage.COMMON_FIELD_REQUIRED.name())
                    .addPropertyNode(EXAM_NAME)
                    .addConstraintViolation();
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    private boolean validateEndDate(CreateExaminationDTO value, ConstraintValidatorContext context) {
        if(Objects.isNull(value.getEndDate())){
            context.buildConstraintViolationWithTemplate(ErrorMessage.COMMON_FIELD_REQUIRED.name())
                    .addPropertyNode(START_DATE)
                    .addConstraintViolation();
            return Boolean.FALSE;
        } else if (value.getEndDate().isBefore(value.getStartDate())) {
            context.buildConstraintViolationWithTemplate(ErrorMessage.CREATE_EXAM_DATE_INVALID.name())
                    .addPropertyNode(END_DATE)
                    .addConstraintViolation();
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    private boolean validateStartDate(CreateExaminationDTO value, ConstraintValidatorContext context) {
        if(Objects.isNull(value.getStartDate())){
            context.buildConstraintViolationWithTemplate(ErrorMessage.COMMON_FIELD_REQUIRED.name())
                    .addPropertyNode(START_DATE)
                    .addConstraintViolation();
            return Boolean.FALSE;
        } else if (value.getStartDate().isBefore(LocalDateTime.now())) {
            context.buildConstraintViolationWithTemplate(ErrorMessage.CREATE_EXAM_DATE_INVALID.name())
                    .addPropertyNode(START_DATE)
                    .addConstraintViolation();
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}
