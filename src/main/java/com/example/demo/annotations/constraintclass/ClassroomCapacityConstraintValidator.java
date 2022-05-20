package com.example.demo.annotations.constraintclass;

import com.example.demo.annotations.ClassroomCapacity;
import com.example.demo.bean.Classarrange;
import com.example.demo.bean.Classroom;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class ClassroomCapacityConstraintValidator implements ConstraintValidator<ClassroomCapacity, Classroom> {
    @Override
    public boolean isValid(Classroom classroom, ConstraintValidatorContext constraintValidatorContext) {
        int capacity = classroom.getCapacity();
        List<Classarrange> classarrangeList = classroom.getClassarranges();
        for (Classarrange classarrange : classarrangeList){
            if (classarrange.getUplesson() != null){
                if (classarrange.getUplesson().getCapacity() > capacity)
                    return false;
            }
        }
        return true;
    }
}
