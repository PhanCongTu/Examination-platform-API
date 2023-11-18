package com.example.springboot.validate.impl;

import com.example.springboot.constant.ErrorMessage;
import com.example.springboot.dto.request.AddToClassroomDTO;
import com.example.springboot.repository.ClassroomRepository;
import com.example.springboot.repository.StudentRepositoryRead;
import com.example.springboot.repository.UserProfileRepository;
import com.example.springboot.validate.ValidateAddToClassroom;
import com.example.springboot.validate.ValidateUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

@Slf4j
@AllArgsConstructor
public class ValidateAddToClassroomImpl implements ConstraintValidator<ValidateAddToClassroom, AddToClassroomDTO> {

    private final ClassroomRepository classroomRepository;
    private final StudentRepositoryRead studentRepositoryRead;
    private final String CLASSROOM_ID = "classroomId";
    private final String STUDENT_ID = "studentId";

    @Override
    public boolean isValid(AddToClassroomDTO value, ConstraintValidatorContext context) {
        log.info("Start validate add student to class");
        context.disableDefaultConstraintViolation();
        boolean checkClassroomId = validateClassroomId(value, context);
        boolean checkStudentId = validateStudentId(value, context);
        log.info("End validate add student to class");
        return ValidateUtils.isAllTrue(List.of(
                checkClassroomId,
                checkStudentId
        ));
    }

    private boolean validateStudentId(AddToClassroomDTO value, ConstraintValidatorContext context) {
        log.info("Start check student id");
        if(studentRepositoryRead.findVerifiedStudentByIdAndStatus(value.getStudentId(), true).isEmpty()){
            context.buildConstraintViolationWithTemplate(ErrorMessage.STUDENT_NOT_FOUND.name())
                    .addPropertyNode(STUDENT_ID)
                    .addConstraintViolation();
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    private boolean validateClassroomId(AddToClassroomDTO value, ConstraintValidatorContext context) {
        log.info("Start check classroom id");

        if(classroomRepository.findClassRoomByIdAndStatus(value.getClassroomId(), true).isEmpty()){
            context.buildConstraintViolationWithTemplate(ErrorMessage.CLASSROOM_NOT_FOUND.name())
                    .addPropertyNode(CLASSROOM_ID)
                    .addConstraintViolation();
            return Boolean.FALSE;
        }
        return Boolean.TRUE;

    }
}
