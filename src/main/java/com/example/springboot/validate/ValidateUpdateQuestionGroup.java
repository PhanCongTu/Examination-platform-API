package com.example.springboot.validate;

import com.example.springboot.validate.impl.ValidateUpdateQuestionGroupImpl;
import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = ValidateUpdateQuestionGroupImpl.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ValidateUpdateQuestionGroup {
    String message() default "Update QuestionGroup request invalid";
    // Cái này là bắt buộc phải có để Hibernate Validator có thể hoạt động
    Class<?>[] groups() default {};
    // Cái này là bắt buộc phải có để Hibernate Validator có thể hoạt động
    Class<? extends Payload>[] payload() default {};
}
