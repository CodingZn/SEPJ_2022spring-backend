package com.example.demo.annotations;

import com.example.demo.annotations.constraintclass.PasswordConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordConstraintValidator.class)
public @interface UserPassword {
    String message() default "密码格式错误！";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
