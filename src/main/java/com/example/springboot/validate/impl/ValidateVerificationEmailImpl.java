package com.example.springboot.validate.impl;

import com.example.springboot.constant.ErrorMessage;
import com.example.springboot.dto.request.VerificationEmailDTO;
import com.example.springboot.validate.ValidateUtils;
import com.example.springboot.validate.ValidateVerificationEmail;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

@Slf4j
@AllArgsConstructor
public class ValidateVerificationEmailImpl implements ConstraintValidator<ValidateVerificationEmail, VerificationEmailDTO> {

    private final String CODE = "code";
    @Override
    public boolean isValid(VerificationEmailDTO verificationEmailDTO, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        Boolean checkCode = validateCode(verificationEmailDTO, context);

        return ValidateUtils.isAllTrue(List.of(
                checkCode
        ));
    }

    private Boolean validateCode(VerificationEmailDTO verificationEmailDTO, ConstraintValidatorContext context) {
        if (verificationEmailDTO.getCode() == null || verificationEmailDTO.getCode().isEmpty()){
            context.buildConstraintViolationWithTemplate(ErrorMessage.COMMON_FIELD_REQUIRED.name())
                    .addPropertyNode(CODE)
                    .addConstraintViolation();
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}
