package com.example.demo.utils;

import com.example.demo.bean.*;
import com.example.demo.bean.Classroom;
import com.example.demo.bean.Classtime;

public class FormVerify {
    //user,major,school,lesson,lessonrequest
    //classroom,classtime

    public static boolean password_verify(String password){
        int len = password.length();
        int kinds = (password.matches(".*\\d+.*") ? 1 :0 )
                +(password.matches(".*[a-zA-Z]+.*") ? 1 : 0)
                +(password.matches(".*[-_]+.*") ? 1 : 0);
        boolean isillegal = password.matches(".*[^[-\\w]]+.*");//匹配到不支持的字符

        return 6 <= len && len <= 32 && kinds >= 2 && !isillegal;
    }

    //user
    public static String user_formverify(User user){

        String REGEX_ID_CARD18 = "^[1-9]\\d{5}[1-2]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9Xx])$";
        String REGEX_EMAIL = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
        String REGEX_PHONE_1 = "1\\d{10}";


        boolean b, c, d;
        boolean u = user.getName().matches("[\u4e00-\u9fa5A-Za-z]+");
        boolean v = user.getIdentitynumber().matches(REGEX_ID_CARD18);
        boolean w = password_verify(user.getPassword());
        boolean x = user.getMajor().getName().matches("[\u4e00-\u9fa5A-Za-z]+");
        boolean y = user.getSchool().getName().matches("[\u4e00-\u9fa5A-Za-z]+");

        b = user.getEmail() == null ||user.getEmail().matches(REGEX_EMAIL);
        c = user.getPhonenumber() == null ||user.getPhonenumber().matches(REGEX_PHONE_1);
        d = switch (user.getUsertype()) {
            case student -> user.getUserid().matches("((0\\d)|(1\\d)|(2[0-2]))\\d{4}");//check.stuid_verify(user.getUserid());
            case teacher -> user.getUserid().matches("((0\\d)|(1\\d)|(2[0-2]))\\d{6}");//check.workid_verify(user.getUserid());
            case admin -> true;
        };
        boolean e = user.getGrade().matches("\\d{2}");
        if(!d)
            return "学工号格式错误!";
        if(!v)
            return "身份证号码错格式误!";
        if(!w)
            return "密码格式错误!";
        if(!u)
            return "姓名格式错误!";
        if(!b)
            return "邮箱格式错误!";
        if(!c)
            return "电话号码格式错误!";
        if(!e)
            return "年级格式错误!";
        if(!x)
            return "专业格式错误!";
        if(!y)
            return "学院格式错误!";
        return "Success";
    }

    //major
    public static String major_formverify(Major major){
        boolean a = major.getMajorid().matches("\\d{3}");
        boolean b = major.getName().matches("[\u4e00-\u9fa5A-Za-z]+");
        if(!a)
            return "专业ID格式错误!";
        if(!b)
            return "专业名称格式错误!";
        return "Success";
    }

    //school
    public static String school_formverify(School school){
        boolean a =school.getSchoolid().matches("\\d{2}");
        boolean b = school.getName().matches("[\u4e00-\u9fa5A-Za-z]+");
        if(!a)
            return "学院ID格式错误!";
        if(!b)
            return "学院名称格式错误!";
        return "Success";
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
        if(!a)
            return "课程ID格式错误!";
        if(!b)
            return "课程代码格式错误!";
        if(!c)
            return "课程名称格式错误!";
        if(!d)
            return "学时格式错误!";
        if(!e)
            return "课程介绍格式错误!";
        if(!f)
            return "课程容量格式错误!";
        if(!g)
            return "学期格式错误!";
        if(!h)
            return "专业限制格式错误!";
        return "Success";
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
        if(!a)
            return "上课时间名称格式错误!";
        if(!b || !c)
            return "上课时间格式错误!";
        return "Success";
    }

    //classroom
    public static String classroom_formverify(Classroom classroom){
        boolean a = classroom.getName().matches("[\u4e00-\u9fa5A-Za-z]+");
        boolean b = classroom.getCapacity() > 0;
        if(!b)
            return "教室容量格式错误!";
        if(!a)
            return "教室名称格式错误!";
        return  "Success";
    }

    //lessonRequest
    public static String lessonRequest_formverify(Lessonrequest lessonrequest){
        boolean a = lessonrequest.getSemester().matches("[A-Z]{4}\\d{6}");
        boolean b = lessonrequest.getRequestReason().matches("\\w{0,999}");
        if(!a)
            return "选课申请学期格式错误!";
        if(!b)
            return "选程申请原因格式错误!";
        return "Success";
    }


}
