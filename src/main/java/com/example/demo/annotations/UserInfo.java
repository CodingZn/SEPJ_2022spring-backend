package com.example.demo.annotations;

import com.example.demo.annotations.constraintclass.UserInfoConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserInfoConstraintValidator.class)
public @interface UserInfo {
    String message() default "学生必须指定年级、上属学院和专业；教师必须指定上属学院！";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
