package com.example.demo.annotations;

import com.example.demo.annotations.constraintclass.LessonConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LessonConstraintValidator.class)
public @interface LessonCapacity {
    String message() default "课程容量超过教室容量限制！";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
