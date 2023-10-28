package com.example.springboot.validate.impl;

import com.example.springboot.constant.ErrorMessage;
import com.example.springboot.dto.request.RefreshTokenDTO;
import com.example.springboot.validate.ValidateRefreshToken;
import com.example.springboot.validate.ValidateUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.Objects;

@Slf4j
@AllArgsConstructor
public class ValidateRefreshTokenImpl implements ConstraintValidator<ValidateRefreshToken, RefreshTokenDTO> {

    public static final String REFRESH_TOKEN = "refreshToken";

    /**
     * Check validate of {@link RefreshTokenDTO}
     *
     * @param value : The {@link RefreshTokenDTO} object
     * @param context : The context
     * @return :
     *  - True if all validate is true,
     *  - False if any validate is false
     */
    @Override
    public boolean isValid(RefreshTokenDTO value, ConstraintValidatorContext context) {
        log.info("Start validate RefreshTokenRequestDTO");
        context.disableDefaultConstraintViolation();
        return ValidateUtils.isAllTrue(List.of(
                checkRefreshToken(value, context)
        ));
    }

    /**
     * Check validate refresh token:
     *  - Do not null or empty
     *
     * @param value : The {@link RefreshTokenDTO} object
     * @param context : The context
     * @return :
     *  - True if all validate is true,
     *  - False if any validate is false
     */
    private boolean checkRefreshToken(RefreshTokenDTO value, ConstraintValidatorContext context) {
        if(Objects.isNull(value.getRefreshToken()) || value.getRefreshToken().isBlank()){
            context.buildConstraintViolationWithTemplate(ErrorMessage.COMMON_FIELD_REQUIRED.name())
                    .addPropertyNode(REFRESH_TOKEN)
                    .addConstraintViolation();
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}
