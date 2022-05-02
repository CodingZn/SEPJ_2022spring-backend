package com.example.demo.utils;

import com.example.demo.bean.*;
import com.example.demo.bean.trivialBeans.Classroom;
import com.example.demo.bean.trivialBeans.Classtime;

public class FormVerify {
    //user,major,school,lesson,lessonrequest
    //classroom,classtime

    //user

    public static String user_formverify(User user){
        boolean a = user.verifyform();
        boolean b = user.getGrade().matches("\\d{2}");
        return "";
    }

    //major
    public static String major_formverify(Major major){
        boolean a = major.getMajornumber().matches("\\d{3}");
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
        boolean b = lesson.getLessoncode().matches("[A-Z]{4}\\d{6}");
        boolean c = lesson.getLessonname().matches("[\u4e00-\u9fa5A-Za-z]+");
        boolean d = (lesson.getHour() > 0) && (lesson.getCredit() >= 0);
        boolean e = lesson.getIntroduction().matches("\\w{0,255}");
        boolean f = lesson.getCapacity() > 0;
        boolean g = lesson.getSemester().equals(lesson.getLessonid().substring(0,5));
        boolean h = lesson.getMajorallowed().equals("all")
                || lesson.getMajorallowed().matches("( ((0\\d)|(1\\d)|(2[0-2]))-\\d{3}, )*( ((0\\d)|(1\\d)|(2[0-2]))-\\d{3} )");
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
    public static String lessonRequest_formverify(LessonRequest lessonRequest){
        boolean a = lessonRequest.getSemester().matches("[A-Z]{4}\\d{6}");
        boolean b = lessonRequest.getRequestReason().matches("\\w{0,999}");
        return "";
    }




    /*//user
    private static final String REGEX_ID_CARD18 = "^[1-9]\\d{5}[1-2]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9Xx])$";
    private static final String REGEX_EMAIL = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
    private static final String REGEX_PHONE_1 = "1\\d{10}";

    private  boolean id_verify(String id){
        return id.matches(REGEX_ID_CARD18);
    }

    private boolean email_verify(String email){

        return (email == "") || email.matches(REGEX_EMAIL);
    }

    private boolean phone_verify(String phone){
        return (phone == "") || phone.matches(REGEX_PHONE_1);
    }

    private boolean password_verify(String password){
        int len = password.length();
        int kinds = (password.matches(".*\\d+.*") ? 1 :0 )
                +(password.matches(".*[a-zA-Z]+.*") ? 1 : 0)
                +(password.matches(".*[-_]+.*") ? 1 : 0);
        boolean isillegal = password.matches(".*[^[-\\w]]+.*");//匹配到不支持的字符

        return 6 <= len && len <= 32 && kinds >= 2 && !isillegal;
    }
    private boolean name_verify(String name){
        return name.matches("[\u4e00-\u9fa5A-Za-z]+");//中英文

    }
    private boolean workid_verify(String number){
        return number.matches("((0\\d)|(1\\d)|(2[0-2]))\\d{6}");//前两位00~22，共八位
    }
    private boolean stuid_verify(String number){
        return number.matches("((0\\d)|(1\\d)|(2[0-2]))\\d{4}");//前两位00~22，共六位
    }
    private boolean school_verify(String school){
        return school.matches("[\u4e00-\u9fa5A-Za-z]+");//中英文
    }
    private boolean major_verify(String major){
        return major.matches("[\u4e00-\u9fa5A-Za-z]+");//中英文
    }
*/
}
