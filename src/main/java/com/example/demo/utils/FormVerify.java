package com.example.demo.utils;

import com.example.demo.bean.*;
import com.example.demo.bean.Classroom;
import com.example.demo.bean.Classtime;

public class FormVerify {
    //user,major,school,lesson,lessonrequest
    //classroom,classtime

    //user

    public static String user_formverify(User user){

        UserFormVerify check = new UserFormVerify();
        boolean a, b, c, d;
        a = check.name_verify(user.getName())
                && check.id_verify(user.getIdentitynumber())
                && check.password_verify(user.getPassword())
                && check.major_verify(user.getMajor().getName())
                && check.school_verify(user.getSchool().getName());
        b = user.getEmail() == null || check.email_verify(user.getEmail());
        c = user.getPhonenumber() == null || check.phone_verify(user.getPhonenumber());

        d = switch (user.getUsertype()) {
            case student -> check.stuid_verify(user.getUserid());
            case teacher -> check.workid_verify(user.getUserid());
            case admin -> true;
        };


        boolean e = user.getGrade().matches("\\d{2}");
        return "";
    }

    //major
    public static String major_formverify(Major major){
        boolean a = major.getMajorid().matches("\\d{3}");
        boolean b = major.getName().matches("[\u4e00-\u9fa5A-Za-z]+");
        return "";
    }

    //school
    public static String school_formverify(School school){
        boolean a =school.getSchoolid().matches("\\d{2}");
        boolean b = school.getName().matches("[\u4e00-\u9fa5A-Za-z]+");
        return "";
    }

    //lesson
    public static String lesson_formverify(Lesson lesson){
        boolean a = lesson.getLessonnumber().matches("[A-Z]{4}\\d{6}\\.\\d{2}");
        boolean b = lesson.getLessoncode().equals(lesson.getLessonnumber().substring(0,10));
        boolean c = lesson.getLessonname().matches("[\u4e00-\u9fa5A-Za-z]+");
        boolean d = (lesson.getHour() > 0) && (lesson.getCredit() >= 0);
        boolean e = lesson.getIntroduction().matches("\\w{0,255}");
        boolean f = lesson.getCapacity() > 0;
        boolean g = lesson.getSemester().matches("(19\\d{2})|(20[0-1]\\d)|(202[0-2])[AB]");
        boolean h = lesson.getMajorallowed().equals("all")
                || lesson.getMajorallowed().matches("(((0\\d)|(1\\d)|(2[0-2]))-\\d{3},)*(((0\\d)|(1\\d)|(2[0-2]))-\\d{3})");
        return "";
    }

    //classtime
    public static String classtime_formverify(Classtime classtime){
        final String REGEX_LESSON_PERIOD = "([一二三四五六日]([1-9]|1[0-4]),)*([一二三四五六日]([1-9]|1[0-4]))+";
        boolean a = classtime.getName().matches(REGEX_LESSON_PERIOD);
        boolean b = classtime.getTime().matches("([01]\\d)|(2[0-3]):[0-5]\\d-([01]\\d)|(2[0-3]):[0-5]\\d");

        String[]  timearray = classtime.getTime().split("-");
        String[] time1 = timearray[0].split(":");
        String[] time2 = timearray[1].split(":");

        boolean c = (Integer.parseInt(time1[0])*60+Integer.parseInt(time1[1])) < (Integer.parseInt(time2[0])*60+Integer.parseInt(time2[1]));
        return "";
    }

    //classroom
    public static String classroom_formverify(Classroom classroom){
        boolean a = classroom.getName().matches("[\u4e00-\u9fa5A-Za-z]+");
        boolean b = classroom.getCapacity() > 0;
        return  "";
    }

    //lessonRequest
    public static String lessonRequest_formverify(Lessonrequest lessonrequest){
        boolean a = lessonrequest.getSemester().matches("[A-Z]{4}\\d{6}");
        boolean b = lessonrequest.getRequestReason().matches("\\w{0,999}");
        return "";
    }


}
