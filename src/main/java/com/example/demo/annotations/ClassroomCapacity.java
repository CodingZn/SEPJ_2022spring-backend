package com.example.demo.annotations;

import com.example.demo.annotations.constraintclass.ClassroomCapacityConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ClassroomCapacityConstraintValidator.class)
public @interface ClassroomCapacity {
    String message() default "教室容量小于课程容量要求！";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
