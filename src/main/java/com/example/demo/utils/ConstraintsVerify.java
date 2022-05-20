package com.example.demo.utils;

import com.example.demo.bean.Lesson;
import com.example.demo.bean.User;
import com.example.demo.exceptions.MyException;

public class ConstraintsVerify {
    public static void LessonHavingDependency(Lesson lesson){
        if (lesson.getClassmates().size() != 0)
            throw new MyException("有学生已经选课！不能删除！");

    }
    public static void UserHavingDependency(User user){
        if (user.getLessonsTaken().size() != 0)
            throw new MyException("该用户有已修课程信息，不能删除！");
        if (user.getLessonsTaking().size() != 0)
            throw new MyException("该用户有已选课程信息，不能删除！");
        if (user.getLessonrequests().size() != 0)
            throw new MyException("该用户有选课申请信息，不能删除！");

    }

}
