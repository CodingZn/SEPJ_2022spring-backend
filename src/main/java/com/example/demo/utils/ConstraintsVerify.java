package com.example.demo.utils;

import com.example.demo.bean.Lesson;
import com.example.demo.bean.User;

public class ConstraintsVerify {
    public static boolean LessonHavingDependency(Lesson lesson){
        if (lesson.getClassmates().size() != 0)
            return true;
        return false;
    }
    public static boolean UserHavingDependency(User user){
        if (user.getLessonsTaken().size() != 0)
            return true;
        if (user.getLessonsTaking().size() != 0)
            return true;
        if (user.getLessonrequests().size() != 0)
            return true;
        return false;
    }

}
