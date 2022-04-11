package com.example.demo.service;

import com.example.demo.bean.Lesson;
import com.example.demo.bean.UserBean;
import com.example.demo.bean.Major;

import java.util.List;

public interface UserService {

    void createAdmin();

    UserBean login(String schoolnumber, String password);

    List<String> getAllSchoolnumbers();

    UserBean getAUser(String schoolnumber);

    String createAUser(UserBean userBean);

    String rewriteUser(UserBean userBean);

    String deleteUser(String schoolnumber);


    String getANewMajornumber();

    List<String> getAllMajornumbers();

    Major getAMajor(int majornumber);

    Major getAMajor(String majornumber_str);

    String createAMajor(String majornumber_str, Major major);

    String rewriteAMajor(String majornumber_str, Major major);

    String deleteMajor(int majornumber);

    String deleteMajor(String majornumber_str);


    String getANewLessonid();

    List<String> getAllLessonid();

    List<String> getAllLessonid(String teacherName, boolean showAll);

    List<String> getAllLessonid(boolean showAll);

    Lesson getALesson(int lessonid);

    Lesson getALesson(String lessonid_str);

    String createALesson(String lesson_str, Lesson lesson);

    String rewriteALesson(String lessonid_str, Lesson lesson);

    String deleteLesson(int lessonid);

    String deleteLesson(String lessonid_str);

    String deleteLesson(String lessonid_str, String name);
}