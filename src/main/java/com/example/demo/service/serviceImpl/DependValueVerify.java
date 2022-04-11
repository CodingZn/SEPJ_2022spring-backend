package com.example.demo.service.serviceImpl;

import com.example.demo.bean.Lesson;
import com.example.demo.bean.UserBean;
import com.example.demo.mapper.LessonMapper;
import com.example.demo.mapper.MajorMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.mapper.straightMappers.ClassroomMapper;


public class DependValueVerify {
    private final MajorMapper majorMapper;
    private final UserMapper userMapper;
    private final LessonMapper lessonMapper;
    private final ClassroomMapper classroomMapper;


    public DependValueVerify(MajorMapper majorMapper, UserMapper userMapper, LessonMapper lessonMapper, ClassroomMapper classroomMapper) {
        this.majorMapper = majorMapper;
        this.userMapper = userMapper;
        this.lessonMapper = lessonMapper;
        this.classroomMapper = classroomMapper;
    }

    //Lesson check
    public boolean lessonDependCheck(Lesson lesson){
        return lessonPeriodVerify(lesson.getPeriod())&&
                lessonPlaceVerify(lesson.getPlace()) &&
                lessonSchoolVerify(lesson.getSchool()) &&
                lessonTeacherVerify(lesson.getTeacher());
    }

    private  boolean lessonSchoolVerify(String school){
        return majorMapper.findBySchool(school) != null;
    }

    private boolean lessonTeacherVerify(String teacher){
        return userMapper.findByUsertypeAndName("teacher", teacher) != null;
    }

    private  boolean lessonPlaceVerify(String place){
        return classroomMapper.findByNameAndStatus(place, "censored") != null;
    }

    private boolean lessonPeriodVerify(String period){
        final String REGEX_LESSON_PERIOD = "([一二三四五六日]([1-9]|1[0-4]),)*([一二三四五六日]([1-9]|1[0-4]))+";

        return period.matches(REGEX_LESSON_PERIOD);
    }

    //User check
    public boolean userDependCheck(UserBean userBean){
        return userMajorVerify(userBean.getMajor()) &&
                userSchoolVerify(userBean.getSchool());
    }

    private boolean userMajorVerify(String major){
        return majorMapper.findByName(major) != null;
    }

    private boolean userSchoolVerify(String school){
        return majorMapper.findBySchool(school) != null;
    }

}
