package com.example.springboot.validate.impl;

import com.example.springboot.constant.Constants;
import com.example.springboot.constant.ErrorMessage;
import com.example.springboot.entity.UserProfile;
import com.example.springboot.repository.UserRepository;
import com.example.springboot.validate.ValidateSignupVM;
import com.example.springboot.validate.ValidateUtils;
import com.example.springboot.dto.view_model.SignupVM;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
public class ValidateSignupVMImpl implements ConstraintValidator<ValidateSignupVM, SignupVM> {
    public static final String LOGIN_NAME = "loginName";
    public static final String PASSWORD = "password";
    public static final String DISPLAY_NAME = "displayName";
    public static final String EMAIL_ADDRESS = "emailAddress";

    public final UserRepository userRepository;
    /**
     * check validate model SignupVM
     * @param value object to validate
     * @param context context in which the constraint is evaluated
     *
     * @return the result validate
     */
    @Override
    public boolean isValid(SignupVM value, ConstraintValidatorContext context) {
        log.info("Start validate SignupVM");
        context.disableDefaultConstraintViolation();
        boolean checkLoginName = validateLoginName(value, context);
        boolean checkPassword = validatePassword(value, context);
        boolean checkDisplayName = validateDisplayName(value, context);
        boolean checkEmailAddress = validateEmailAddress(value, context);
        return ValidateUtils.isAllTrue(List.of(
                checkLoginName,
                checkPassword,
                checkDisplayName,
                checkEmailAddress
        ));
    }

    private boolean validateEmailAddress(SignupVM value, ConstraintValidatorContext context) {
        if(Objects.isNull(value.getEmailAddress()) || value.getEmailAddress().isEmpty()){
            context.buildConstraintViolationWithTemplate(ErrorMessage.COMMON_FIELD_REQUIRED.name())
                    .addPropertyNode(EMAIL_ADDRESS)
                    .addConstraintViolation();
            return false;
        }
        if(!value.getEmailAddress().matches(Constants.EMAIL_REGEX)){
            context.buildConstraintViolationWithTemplate(ErrorMessage.SIGNUP_EMAIL_ADDRESS_INVALID_CHARACTER.name())
                    .addPropertyNode(EMAIL_ADDRESS)
                    .addConstraintViolation();
            return false;
        }
        Optional<UserProfile> userProfileOp = userRepository.findOneByEmailAddressVerified(value.getEmailAddress());
        if (userProfileOp.isPresent()){
            context.buildConstraintViolationWithTemplate(ErrorMessage.SIGNUP_EMAIL_ADDRESS_DUPLICATE.name())
                    .addPropertyNode(EMAIL_ADDRESS)
                    .addConstraintViolation();
            return false;
        }
        return true;
    }

    private boolean validateDisplayName(SignupVM value, ConstraintValidatorContext context) {
        if(Objects.isNull(value.getDisplayName()) || value.getDisplayName().isEmpty()){
            context.buildConstraintViolationWithTemplate(ErrorMessage.COMMON_FIELD_REQUIRED.name())
                    .addPropertyNode(DISPLAY_NAME)
                    .addConstraintViolation();
            return false;
        }
        return true;
    }

    private boolean validatePassword(SignupVM value, ConstraintValidatorContext context) {
        if(Objects.isNull(value.getPassword()) || value.getPassword().isEmpty()){
            context.buildConstraintViolationWithTemplate(ErrorMessage.COMMON_FIELD_REQUIRED.name())
                    .addPropertyNode(PASSWORD)
                    .addConstraintViolation();
            return false;
        }
        if(!value.getPassword().matches(Constants.PASSWORD_REGEX)){
            context.buildConstraintViolationWithTemplate(ErrorMessage.SIGNUP_PASSWORD_INVALID_CHARACTER.name())
                    .addPropertyNode(PASSWORD)
                    .addConstraintViolation();
            return false;
        }
        return true;
    }

    private boolean validateLoginName(SignupVM value, ConstraintValidatorContext context) {
        if(Objects.isNull(value.getLoginName()) || value.getLoginName().isEmpty()){
            context.buildConstraintViolationWithTemplate(ErrorMessage.COMMON_FIELD_REQUIRED.name())
                    .addPropertyNode(LOGIN_NAME)
                    .addConstraintViolation();
            return false;
        }
        if(!value.getLoginName().matches(Constants.LOGIN_NAME_REGEX)){
            context.buildConstraintViolationWithTemplate(ErrorMessage.SIGNUP_LOGIN_NAME_INVALID_CHARACTER.name())
                    .addPropertyNode(LOGIN_NAME)
                    .addConstraintViolation();
            return false;
        }
        Optional<UserProfile> userProfileOp = userRepository.findOneByLoginName(value.getLoginName());
        if (userProfileOp.isPresent()){
            context.buildConstraintViolationWithTemplate(ErrorMessage.SIGNUP_LOGIN_NAME_DUPLICATE.name())
                    .addPropertyNode(LOGIN_NAME)
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
