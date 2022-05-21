package com.example.demo.annotations;

import com.example.demo.annotations.constraintclass.LessonCapacityConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LessonCapacityConstraintValidator.class)
public @interface LessonCapacity {
    String message() default "课程容量不能超过教室容量，也不能小于学生数量！";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
