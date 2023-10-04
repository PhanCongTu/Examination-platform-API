package com.example.springboot.validate.impl;

import com.example.springboot.constant.ErrorMessage;
import com.example.springboot.dto.request.CreateTopicDTO;
import com.example.springboot.repository.TopicRepository;
import com.example.springboot.validate.ValidateCreateTopic;
import com.example.springboot.validate.ValidateUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.Objects;

@Slf4j
@AllArgsConstructor
public class ValidateCreateTopicImpl implements ConstraintValidator<ValidateCreateTopic, CreateTopicDTO> {

    private TopicRepository topicRepository;
    private static final String TOPIC_NAME = "topicName";
    private static final String CODE = "code";

    @Override
    public boolean isValid(CreateTopicDTO value, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        boolean checkTopicName = validateTopicName(value, context);
        boolean checkCode = validateCode(value, context);
        return ValidateUtils.isAllTrue(List.of(
                checkTopicName,
                checkCode
        ));
    }

    private boolean validateCode(CreateTopicDTO value, ConstraintValidatorContext context) {
        if(Objects.isNull(value.getCode()) || value.getCode().isBlank()){
            context.buildConstraintViolationWithTemplate(ErrorMessage.COMMON_FIELD_REQUIRED.name())
                    .addPropertyNode(CODE)
                    .addConstraintViolation();
            return Boolean.FALSE;
        }
        if(topicRepository.findByCode(value.getCode()).isPresent()){
            context.buildConstraintViolationWithTemplate(ErrorMessage.CREATE_TOPIC_CODE_DUPLICATE.name())
                    .addPropertyNode(CODE)
                    .addConstraintViolation();
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    private boolean validateTopicName(CreateTopicDTO value, ConstraintValidatorContext context) {
        if(Objects.isNull(value.getTopicName()) || value.getTopicName().isBlank()){
            context.buildConstraintViolationWithTemplate(ErrorMessage.COMMON_FIELD_REQUIRED.name())
                    .addPropertyNode(TOPIC_NAME)
                    .addConstraintViolation();
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }


}
