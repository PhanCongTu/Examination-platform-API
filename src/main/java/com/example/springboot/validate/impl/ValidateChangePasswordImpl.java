package com.example.springboot.validate.impl;

import com.example.springboot.constant.Constants;
import com.example.springboot.constant.ErrorMessage;
import com.example.springboot.dto.request.ChangePasswordDTO;
import com.example.springboot.validate.ValidateChangePassword;
import com.example.springboot.validate.ValidateUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.Objects;

@Slf4j
@AllArgsConstructor
public class ValidateChangePasswordImpl implements ConstraintValidator<ValidateChangePassword, ChangePasswordDTO> {

    public static final String OLD_PASSWORD = "oldPassword";
    public static final String NEW_PASSWORD = "newPassword";
    @Override
    public boolean isValid(ChangePasswordDTO value, ConstraintValidatorContext context) {
        log.info("Start validate ChangePasswordDTO");
        context.disableDefaultConstraintViolation();
        boolean checkOldPassword = validateOldPassword(value, context);
        boolean checkNewPassword = validateNewPassword(value, context);
        return ValidateUtils.isAllTrue(List.of(
                checkOldPassword,
                checkNewPassword
        ));
    }

    private boolean validateNewPassword(ChangePasswordDTO value, ConstraintValidatorContext context) {
        if(Objects.isNull(value.getNewPassword())){
            context.buildConstraintViolationWithTemplate(ErrorMessage.COMMON_FIELD_REQUIRED.name())
                    .addPropertyNode(NEW_PASSWORD)
                    .addConstraintViolation();
            return Boolean.FALSE;
        }
        if(!value.getNewPassword().matches(Constants.PASSWORD_REGEX)){
            context.buildConstraintViolationWithTemplate(ErrorMessage.SIGNUP_PASSWORD_INVALID_CHARACTER.name())
                    .addPropertyNode(NEW_PASSWORD)
                    .addConstraintViolation();
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    private boolean validateOldPassword(ChangePasswordDTO value, ConstraintValidatorContext context) {
        if(Objects.isNull(value.getOldPassword()) || value.getOldPassword().isBlank()){
            context.buildConstraintViolationWithTemplate(ErrorMessage.COMMON_FIELD_REQUIRED.name())
                    .addPropertyNode(OLD_PASSWORD)
                    .addConstraintViolation();
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}
