package com.example.springboot.validate.impl;

import com.example.springboot.constant.Constants;
import com.example.springboot.constant.ErrorMessage;
import com.example.springboot.dto.request.CreateQuestionDTO;
import com.example.springboot.dto.request.UpdateUserProfileDTO;
import com.example.springboot.entity.UserProfile;
import com.example.springboot.repository.UserProfileRepository;
import com.example.springboot.validate.ValidateUpdateUserProfile;
import com.example.springboot.validate.ValidateUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
public class ValidateUpdateUserProfileImpl implements ConstraintValidator<ValidateUpdateUserProfile, UpdateUserProfileDTO> {
    private final UserProfileRepository userProfileRepository;
    private final String DISPLAY_NAME = "displayName";
    private final String EMAIL_ADDRESS = "emailAddress";
    @Override
    public boolean isValid(UpdateUserProfileDTO value, ConstraintValidatorContext context) {
        log.info("Validate UpdateUserProfileDTO: start");
        context.disableDefaultConstraintViolation();
        boolean checkDisplayName = validateDisplayName(value, context);
        boolean checkEmailAddress = validateEmailAddress(value, context);

        log.info("Validate UpdateUserProfileDTO: end");
        return ValidateUtils.isAllTrue(List.of(
                checkDisplayName,
                checkEmailAddress
        ));
    }

    private boolean validateEmailAddress(UpdateUserProfileDTO value, ConstraintValidatorContext context) {
        log.info(String.format("Validate emailAddress: start"));
        if (StringUtils.isNoneBlank(value.getEmailAddress())) {
            if (StringUtils.isBlank(value.getEmailAddress())) {
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
                context.buildConstraintViolationWithTemplate(ErrorMessage.UPDATE_EMAIL_ADDRESS_DUPLICATE.name())
                        .addPropertyNode(EMAIL_ADDRESS)
                        .addConstraintViolation();
                return Boolean.FALSE;
            }
        }
        log.info(String.format("Validate emailAddress: End"));
        return Boolean.TRUE;
    }

    private boolean validateDisplayName(UpdateUserProfileDTO value, ConstraintValidatorContext context) {
        log.info(String.format("Validate displayName: start"));
        if (StringUtils.isNoneBlank(value.getDisplayName())) {
            if (StringUtils.isBlank(value.getDisplayName())) {
                context.buildConstraintViolationWithTemplate(ErrorMessage.COMMON_FIELD_REQUIRED.name())
                        .addPropertyNode(DISPLAY_NAME)
                        .addConstraintViolation();
                return Boolean.FALSE;
            }
        }
        log.info(String.format("Validate displayName: End"));
        return Boolean.TRUE;
    }
}
