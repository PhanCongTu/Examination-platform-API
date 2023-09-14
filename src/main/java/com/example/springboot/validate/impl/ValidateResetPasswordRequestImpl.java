package com.example.springboot.validate.impl;

import com.example.springboot.constant.Constants;
import com.example.springboot.constant.ErrorMessage;
import com.example.springboot.dto.request.ResetPasswordDTO;
import com.example.springboot.validate.ValidateResetPasswordRequest;
import com.example.springboot.validate.ValidateUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.Objects;

@Slf4j
@AllArgsConstructor
public class ValidateResetPasswordRequestImpl implements ConstraintValidator<ValidateResetPasswordRequest, ResetPasswordDTO> {

    public static final String PASSWORD = "password";
    public static final String CODE = "code";

    @Override
    public boolean isValid(ResetPasswordDTO value, ConstraintValidatorContext context) {
        log.info("Start validate SignUpRequestDTO");
        context.disableDefaultConstraintViolation();
        boolean checkPassword = validatePassword(value, context);
        boolean checkResetPasswordCode = validateResetPasswordCode(value, context);
        return ValidateUtils.isAllTrue(List.of(
                checkPassword,
                checkResetPasswordCode
        ));
    }

    private boolean validateResetPasswordCode(ResetPasswordDTO value, ConstraintValidatorContext context) {
        if(Objects.isNull(value.getCode()) || value.getCode().isBlank()){
            context.buildConstraintViolationWithTemplate(ErrorMessage.COMMON_FIELD_REQUIRED.name())
                    .addPropertyNode(CODE)
                    .addConstraintViolation();
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    private boolean validatePassword(ResetPasswordDTO value, ConstraintValidatorContext context) {
        if(Objects.isNull(value.getPassword())){
            context.buildConstraintViolationWithTemplate(ErrorMessage.COMMON_FIELD_REQUIRED.name())
                    .addPropertyNode(PASSWORD)
                    .addConstraintViolation();
            return Boolean.FALSE;
        }
        if(!value.getPassword().matches(Constants.PASSWORD_REGEX)){
            context.buildConstraintViolationWithTemplate(ErrorMessage.SIGNUP_PASSWORD_INVALID_CHARACTER.name())
                    .addPropertyNode(PASSWORD)
                    .addConstraintViolation();
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}
