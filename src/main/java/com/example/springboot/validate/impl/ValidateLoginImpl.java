package com.example.springboot.validate.impl;

import com.example.springboot.constant.ErrorMessage;
import com.example.springboot.dto.request.LoginRequestDTO;
import com.example.springboot.validate.ValidateLogin;
import com.example.springboot.validate.ValidateUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.Objects;

@Slf4j
@AllArgsConstructor
public class ValidateLoginImpl implements ConstraintValidator<ValidateLogin, LoginRequestDTO> {
    public static final String LOGIN_NAME = "loginName";
    public static final String PASSWORD = "password";

    /**
     *  Check validate of {@link LoginRequestDTO}
     *
     * @param loginRequestDTO The {@link LoginRequestDTO} object
     * @param constraintValidatorContext The context
     * @return :
     *  - True if all validate is true,
     *  - False if any validate is false
     */
    @Override
    public boolean isValid(LoginRequestDTO loginRequestDTO, ConstraintValidatorContext constraintValidatorContext) {
        log.info("Start validate LoginRequestDTO");
        constraintValidatorContext.disableDefaultConstraintViolation();
        boolean checkLoginName = validateLoginName(loginRequestDTO, constraintValidatorContext);
        boolean checkPassword = validatePassword(loginRequestDTO, constraintValidatorContext);
        return ValidateUtils.isAllTrue(List.of(
                checkLoginName,
                checkPassword
        ));
    }

    /**
     * Check validate password:
     *  -   Do not null or empty
     *
     * @param value The {@link LoginRequestDTO} object
     * @param context The context
     * @return :
     *  - True if all validate is true,
     *  - False if any validate is false
     */
    private boolean validatePassword(LoginRequestDTO value, ConstraintValidatorContext context) {
        if(Objects.isNull(value.getPassword()) || value.getPassword().isBlank()){
            context.buildConstraintViolationWithTemplate(ErrorMessage.COMMON_FIELD_REQUIRED.name())
                    .addPropertyNode(PASSWORD)
                    .addConstraintViolation();
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /**
     * Check validate login name:
     *  - Do not null or empty
     *
     * @param value : The {@link LoginRequestDTO} object
     * @param context : The context
     * @return :
     *  - True if all validate is true,
     *  - False if any validate is false
     */
    private boolean validateLoginName(LoginRequestDTO value, ConstraintValidatorContext context) {
        if(Objects.isNull(value.getLoginName()) || value.getLoginName().isBlank()){
            context.buildConstraintViolationWithTemplate(ErrorMessage.COMMON_FIELD_REQUIRED.name())
                    .addPropertyNode(LOGIN_NAME)
                    .addConstraintViolation();
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}
