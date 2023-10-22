package com.example.springboot.validate.impl;

import com.example.springboot.constant.ErrorMessage;
import com.example.springboot.dto.request.CreateObjectiveTestDTO;
import com.example.springboot.validate.ValidateCreateObjectiveTest;
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
public class ValidateCreateObjectiveTestRequestImpl implements ConstraintValidator<ValidateCreateObjectiveTest, CreateObjectiveTestDTO> {

    public static final String OBJECTIVE_TEST_NAME = "objectiveTestName";
    public static final String CLASS_ROOM_ID = "classRoomId";
    public static final String TESTING_TIME = "testingTime";
    public static final String START_DATE = "startDate";
    public static final String END_DATE = "endDate";


    @Override
    public boolean isValid(CreateObjectiveTestDTO value, ConstraintValidatorContext context) {
        log.info("Start validate CreateObjectiveTestDTO");
        context.disableDefaultConstraintViolation();
        boolean checkObjectiveTestName = validateObjectiveTestName(value, context);
        boolean checkClassRoomId = validateClassRoomId(value, context);
        boolean checkTestingTime = validateTestingTime(value, context);
        boolean checkStartDate = validateStartDate(value, context);
        boolean checkEndDate = validateEndDate(value, context);
        return ValidateUtils.isAllTrue(List.of(
                checkObjectiveTestName,
                checkClassRoomId,
                checkStartDate,
                checkEndDate,
                checkTestingTime
        ));
    }

    private boolean validateTestingTime(CreateObjectiveTestDTO value, ConstraintValidatorContext context) {
        if(Objects.isNull(value.getTestingTime())){
            context.buildConstraintViolationWithTemplate(ErrorMessage.COMMON_FIELD_REQUIRED.name())
                    .addPropertyNode(TESTING_TIME)
                    .addConstraintViolation();
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    private boolean validateClassRoomId(CreateObjectiveTestDTO value, ConstraintValidatorContext context) {
        if(Objects.isNull(value.getClassRoomId())){
            context.buildConstraintViolationWithTemplate(ErrorMessage.COMMON_FIELD_REQUIRED.name())
                    .addPropertyNode(CLASS_ROOM_ID)
                    .addConstraintViolation();
            return Boolean.FALSE;
        }
        // Tim class room
        return Boolean.TRUE;
    }

    private boolean validateObjectiveTestName(CreateObjectiveTestDTO value, ConstraintValidatorContext context) {
        if(Objects.isNull(value.getObjectiveTestName()) || value.getObjectiveTestName().isBlank()){
            context.buildConstraintViolationWithTemplate(ErrorMessage.COMMON_FIELD_REQUIRED.name())
                    .addPropertyNode(OBJECTIVE_TEST_NAME)
                    .addConstraintViolation();
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    private boolean validateEndDate(CreateObjectiveTestDTO value, ConstraintValidatorContext context) {
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

    private boolean validateStartDate(CreateObjectiveTestDTO value, ConstraintValidatorContext context) {
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
