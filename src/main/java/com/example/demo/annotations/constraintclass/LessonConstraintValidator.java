package com.example.demo.annotations.constraintclass;

import com.example.demo.annotations.LessonCapacity;
import com.example.demo.bean.Lesson;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

public class LessonConstraintValidator implements ConstraintValidator<LessonCapacity, Lesson> {

    @Override
    public boolean isValid(Lesson lesson, ConstraintValidatorContext constraintValidatorContext) {
        int lessonCapacity = lesson.getCapacity();
        List<Integer> capacityList = new ArrayList<>(lesson.getArranges().stream().map(u->u.getClassroom().getCapacity()).toList());
        for (Integer c : capacityList){
            if (lessonCapacity > c)
                return false;
        }
        return true;
    }
}
