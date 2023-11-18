package com.example.springboot.validate;

import com.example.springboot.validate.impl.ValidateCreateMultipleChoiceTestImpl;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = ValidateCreateMultipleChoiceTestImpl.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ValidateCreateMultipleChoiceTest {
    // trường message là bắt buộc, khai báo nội dung sẽ trả về khi k hợp lệ
    String message() default "Create MultipleChoiceTest request invalid";
    // Cái này là bắt buộc phải có để Hibernate Validator có thể hoạt động
    Class<?>[] groups() default {};
    // Cái này là bắt buộc phải có để Hibernate Validator có thể hoạt động
    Class<? extends Payload>[] payload() default {};
}
