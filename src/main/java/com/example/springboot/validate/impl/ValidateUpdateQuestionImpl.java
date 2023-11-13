package com.example.springboot.validate.impl;

import com.example.springboot.constant.ErrorMessage;
import com.example.springboot.dto.request.CreateQuestionDTO;
import com.example.springboot.dto.request.UpdateQuestionDTO;
import com.example.springboot.validate.ValidateUpdateQuestion;
import com.example.springboot.validate.ValidateUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
@AllArgsConstructor
public class ValidateUpdateQuestionImpl implements ConstraintValidator<ValidateUpdateQuestion, UpdateQuestionDTO> {
    private final String CONTENT = "content";
    private final String FIRSTANSWER = "firstAnswer";
    private final String SECONDANSWER = "secondAnswer";
    private final String THIRDANSWER = "thirdAnswer";
    private final String FOURTHANSWER = "fourthAnswer";

    @Override
    public boolean isValid(UpdateQuestionDTO value, ConstraintValidatorContext context) {
        log.info("Validate CreateQuestionDTO: start");
        context.disableDefaultConstraintViolation();
        boolean checkContent = validateContent(value, context);
        boolean checkAnswers = validateAnswers(value, context);

        log.info("Validate CreateQuestionDTO: end");
        return ValidateUtils.isAllTrue(List.of(
                checkContent,
                checkAnswers
        ));
    }

    private boolean validateAnswers(UpdateQuestionDTO value, ConstraintValidatorContext context) {
        boolean checkFirstAnswer = validateAnswer(value.getFirstAnswer(), context, FIRSTANSWER);
        boolean checkSecondAnswer = validateAnswer(value.getSecondAnswer(), context, SECONDANSWER);
        boolean checkThirdAnswer = validateAnswer(value.getThirdAnswer(), context, THIRDANSWER);
        boolean checkFourthAnswer = validateAnswer(value.getFourthAnswer(), context, FOURTHANSWER);

        boolean isAllValidAnswer = ValidateUtils.isAllTrue(List.of(
                checkFirstAnswer,
                checkSecondAnswer,
                checkThirdAnswer,
                checkFourthAnswer
        ));
        boolean checkOnlyOneCorrect = false;
        if (isAllValidAnswer) {
            // The request must have one true answer
            checkOnlyOneCorrect = validateOnlyOneCorrect(value, context);
        }
        return (isAllValidAnswer && checkOnlyOneCorrect);
    }

    private boolean validateOnlyOneCorrect(UpdateQuestionDTO value, ConstraintValidatorContext context) {
        log.info("Validate Only One Correct: start");
        List<Boolean> checkAnswers = new ArrayList<>();
        if (Objects.nonNull(value.getFirstAnswer())) {
            checkAnswers.add(Objects.requireNonNullElse(value.getFirstAnswer().getIsCorrect(), false));
        }
        if (Objects.nonNull(value.getSecondAnswer())) {
            checkAnswers.add(Objects.requireNonNullElse(value.getSecondAnswer().getIsCorrect(), false));
        }
        if (Objects.nonNull(value.getThirdAnswer())) {
            checkAnswers.add(Objects.requireNonNullElse(value.getThirdAnswer().getIsCorrect(), false));
        }
        if (Objects.nonNull(value.getFourthAnswer())) {
            checkAnswers.add(Objects.requireNonNullElse(value.getFourthAnswer().getIsCorrect(), false));
        }
        if (!checkAnswers.isEmpty()) {
            boolean isOnlyOneTrueAnswer = ValidateUtils.isOnlyOneTrue(checkAnswers);
            if (!isOnlyOneTrueAnswer) {
                context.buildConstraintViolationWithTemplate(ErrorMessage.QUESTION_CREATE_MUST_HAVE_ONE_TRUE_ANSWER.name())
                        .addPropertyNode(CONTENT)
                        .addConstraintViolation();
                return Boolean.FALSE;
            }
        }
        log.info("Validate Only One Correct: end");
        return Boolean.TRUE;
    }

    private boolean validateAnswer(CreateQuestionDTO.Answer value, ConstraintValidatorContext context, String answerName) {
        log.info(String.format("Validate %s of question: start", answerName));
        if (Objects.nonNull(value)) {
            if (StringUtils.isBlank(value.getAnswerContent())) {
                context.buildConstraintViolationWithTemplate(ErrorMessage.QUESTION_CREATE_ANSWER_CONTENT_REQUIRED.name())
                        .addPropertyNode(answerName)
                        .addConstraintViolation();
                return Boolean.FALSE;
            }
            if (Objects.isNull(value.getIsCorrect())) {
                value.setIsCorrect(Boolean.FALSE);
            }
        }
        log.info(String.format("Validate %s of question: end", answerName));
        return Boolean.TRUE;
    }

    private boolean validateContent(UpdateQuestionDTO value, ConstraintValidatorContext context) {
        if (Objects.nonNull(value.getContent()) && value.getContent().isBlank()) {
            context.buildConstraintViolationWithTemplate(ErrorMessage.QUESTION_CREATE_ANSWER_CONTENT_REQUIRED.name())
                    .addPropertyNode(CONTENT)
                    .addConstraintViolation();
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}
