package com.example.demo.annotations.constraintclass;

import com.example.demo.annotations.LessonCapacity;
import com.example.demo.bean.Lesson;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

public class LessonCapacityConstraintValidator implements ConstraintValidator<LessonCapacity, Lesson> {

    @Override
    public boolean isValid(Lesson lesson, ConstraintValidatorContext constraintValidatorContext) {
        int lessonCapacity = lesson.getCapacity();
        List<Integer> capacityList = new ArrayList<>(lesson.getClassarrange().stream().map(u->u.getClassroom().getCapacity()).toList());
        for (Integer c : capacityList){
            if (c != null && lessonCapacity > c)
                return false;
        }
        if (lesson.getClassmates() != null && lessonCapacity < lesson.getClassmates().size()) return false;
        return true;
    }
}
