package com.example.springboot.validate;

import com.example.springboot.validate.impl.ValidateRefreshTokenRequestImpl;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidateRefreshTokenRequestImpl.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ValidateRefreshTokenRequest {
    // trường message là bắt buộc, khai báo nội dung sẽ trả về khi k hợp lệ
    String message() default "Refresh token request invalid";
    // Cái này là bắt buộc phải có để Hibernate Validator có thể hoạt động
    Class<?>[] groups() default {};
    // Cái này là bắt buộc phải có để Hibernate Validator có thể hoạt động
    Class<? extends Payload>[] payload() default {};
}