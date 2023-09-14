package com.example.springboot.validate.impl;

import com.example.springboot.constant.Constants;
import com.example.springboot.constant.ErrorMessage;
import com.example.springboot.dto.request.RefreshTokenRequestDTO;
import com.example.springboot.dto.request.SignUpRequestDTO;
import com.example.springboot.entity.UserProfile;
import com.example.springboot.repository.UserProfileRepository;
import com.example.springboot.validate.ValidateSignUpRequest;
import com.example.springboot.validate.ValidateUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
public class ValidateSignupRequestImpl implements ConstraintValidator<ValidateSignUpRequest, SignUpRequestDTO> {
    public static final String LOGIN_NAME = "loginName";
    public static final String PASSWORD = "password";
    public static final String DISPLAY_NAME = "displayName";
    public static final String EMAIL_ADDRESS = "emailAddress";
    public static final String IS_TEACHER = "isTeacher";

    public final UserProfileRepository userProfileRepository;
    /**
     * check validate of {@link RefreshTokenRequestDTO}
     *
     * @param value : The {@link SignUpRequestDTO} object
     * @param context : The context
     * @return :
     *  - True if all validate is true,
     *  - False if any validate is false
     */
    @Override
    public boolean isValid(SignUpRequestDTO value, ConstraintValidatorContext context) {
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

    /**
     * Check validate email address:
     *  - Do not null or empty
     *  - Check format: ^(?=.{1,64}@)[A-Za-z0-9_-]+(\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\.[A-Za-z0-9-]+)*(\.[A-Za-z]{2,})$
     *  - Check duplicate if this email address has been verified in the database
     *
     * @param value : The {@link SignUpRequestDTO} object
     * @param context : The context
     * @return :
     *  - True if all validate is true,
     *  - False if any validate is false
     */
    private boolean validateEmailAddress(SignUpRequestDTO value, ConstraintValidatorContext context) {
        if(Objects.isNull(value.getEmailAddress())){
            context.buildConstraintViolationWithTemplate(ErrorMessage.COMMON_FIELD_REQUIRED.name())
                    .addPropertyNode(EMAIL_ADDRESS)
                    .addConstraintViolation();
            return Boolean.FALSE;
        }
        if(!value.getEmailAddress().matches(Constants.EMAIL_REGEX)){
            context.buildConstraintViolationWithTemplate(ErrorMessage.SIGNUP_EMAIL_ADDRESS_INVALID_CHARACTER.name())
                    .addPropertyNode(EMAIL_ADDRESS)
                    .addConstraintViolation();
            return Boolean.FALSE;
        }
        Optional<UserProfile> userProfileOp = userProfileRepository.findOneByEmailAddressVerified(value.getEmailAddress());
        if (userProfileOp.isPresent()){
            context.buildConstraintViolationWithTemplate(ErrorMessage.SIGNUP_EMAIL_ADDRESS_DUPLICATE.name())
                    .addPropertyNode(EMAIL_ADDRESS)
                    .addConstraintViolation();
            return Boolean.FALSE;
        }
        return true;
    }

    /**
     * Check validate display name:
     *  - Do not null or empty
     *
     * @param value : The {@link SignUpRequestDTO} object
     * @param context : The context
     * @return :
     *  - True if all validate is true,
     *  - False if any validate is false
     */
    private boolean validateDisplayName(SignUpRequestDTO value, ConstraintValidatorContext context) {
        if(Objects.isNull(value.getDisplayName()) || value.getDisplayName().isBlank()){
            context.buildConstraintViolationWithTemplate(ErrorMessage.COMMON_FIELD_REQUIRED.name())
                    .addPropertyNode(DISPLAY_NAME)
                    .addConstraintViolation();
            return Boolean.FALSE;
        }
        return true;
    }

    /**
     * Check validate password:
     *  - Do not null or empty
     *  - Check format: ^[!-~]{8,20}$
     *
     * @param value : The {@link SignUpRequestDTO} object
     * @param context : The context
     * @return :
     *  - True if all validate is true,
     *  - False if any validate is false
     */
    private boolean validatePassword(SignUpRequestDTO value, ConstraintValidatorContext context) {
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

    /**
     * Check validate login name:
     *  - Do not null or empty
     *  - Check format: ^([a-zA-Z0-9._-]{4,16}$)
     *  - Check duplicate login name in the database
     *
     * @param value : The {@link SignUpRequestDTO} object
     * @param context : The context
     * @return :
     *  - True if all validate is true,
     *  - False if any validate is false
     */
    private boolean validateLoginName(SignUpRequestDTO value, ConstraintValidatorContext context) {
        if(Objects.isNull(value.getLoginName())){
            context.buildConstraintViolationWithTemplate(ErrorMessage.COMMON_FIELD_REQUIRED.name())
                    .addPropertyNode(LOGIN_NAME)
                    .addConstraintViolation();
            return Boolean.FALSE;
        }
        if(!value.getLoginName().matches(Constants.LOGIN_NAME_REGEX)){
            context.buildConstraintViolationWithTemplate(ErrorMessage.SIGNUP_LOGIN_NAME_INVALID_CHARACTER.name())
                    .addPropertyNode(LOGIN_NAME)
                    .addConstraintViolation();
            return Boolean.FALSE;
        }
        Optional<UserProfile> userProfileOp = userProfileRepository.findOneByLoginName(value.getLoginName());
        if (userProfileOp.isPresent()){
            context.buildConstraintViolationWithTemplate(ErrorMessage.SIGNUP_LOGIN_NAME_DUPLICATE.name())
                    .addPropertyNode(LOGIN_NAME)
                    .addConstraintViolation();
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}
