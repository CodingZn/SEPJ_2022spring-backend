package com.example.demo.service;

import com.example.demo.bean.Classarrange;
import com.example.demo.bean.Classtime;
import com.example.demo.bean.Lesson;
import com.example.demo.bean.User;
import com.example.demo.mapper.LessonMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.mapper.straightMappers.UltimatecontrolMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.example.demo.bean.specialBean.Ultimatectrl.*;

@Service
public class LessonConductService {
    private final UserMapper userMapper;
    private final LessonMapper lessonMapper;
    private final UltimatecontrolMapper controls;

    @Autowired
    public LessonConductService(UserMapper userMapper, LessonMapper lessonMapper, UltimatecontrolMapper controls){
        this.userMapper = userMapper;
        this.lessonMapper = lessonMapper;
        this.controls = controls;
    }

    //此方法返回 "Success" 或提示信息
    public String selectALesson(String userid, String lessonid) {
        User user = userMapper.findByUserid(userid);
        Lesson lesson = lessonMapper.findByLessonid(Integer.parseInt(lessonid));
        String now_semester = controls.findByName(SEMESTER_CONTROL).getStatus();
        String classcontrol = controls.findByName(CLASS_CONTROL).getStatus();
        if(user == null)
            return "无此用户！";
        if(lesson == null || lesson.getStatus() == Lesson.Status.pending)
            return "课程不存在！";
        if(classcontrol.equals(CLASS_CONTROL_DISABLED))
            return "选课未开放！";
        if(!Objects.equals(lesson.getSemester(), now_semester))
            return "本学期不开放此课程！";
        if(checkTakingConstraint(user, lesson))
            return "不能重复选择课程代码相同的课程！";
        if(checkTakenConstraint(user, lesson))
            return "不能选择已经修过的课程！";
        if(checkTimeArrangeConstraint(user, lesson))
            return "该课与其他课程存在时间冲突！";
        if(checkMajorConstraint(user, lesson))
            return "您所在的年级专业不可选此课程！";

        switch (classcontrol){
            case CLASS_CONTROL_FIRST -> {

            }
            case CLASS_CONTROL_SECOND -> {
                if(checkCapacityConstraint(user, lesson))
                    return "课程容量已满，请关注课程余量！";
            }
            default -> {
                return "选课未开放！";
            }
        }
        lesson.getClassmates().add(user);
        lessonMapper.save(lesson);
        return "Success";

    }

    //此方法返回 "Success" 或提示信息
    public String quitALesson(String userid, String lessonid){
        User user = userMapper.findByUserid(userid);
        Lesson lesson = lessonMapper.findByLessonid(Integer.parseInt(lessonid));
        String now_semester = controls.findByName(SEMESTER_CONTROL).getStatus();
        String classcontrol = controls.findByName(CLASS_CONTROL).getStatus();
        if(user == null)
            return "无此用户！";
        if(lesson == null || lesson.getStatus() == Lesson.Status.pending)
            return "课程不存在！";
        if(classcontrol.equals(CLASS_CONTROL_DISABLED))
            return "退课未开放！";
        if(!Objects.equals(lesson.getSemester(), now_semester))
            return "本学期不开放此课程！";
        if(!user.getLessonsTaking().contains(lesson))
            return "您没有选上此课程！";
        return "Success";
    }

    public void autoSelectALesson(User user, Lesson lesson){
        lesson.getClassmates().add(user);
        lesson.setCapacity(lesson.getCapacity()+1);
        lessonMapper.save(lesson);
    }

    private boolean checkMajorConstraint(User user, Lesson lesson){
        String user_majorid = user.getMajor().getMajorid();
        String user_grade = user.getGrade();
        String lesson_majorallowed = lesson.getMajorallowed();
        String user_info = user_grade + "-" + user_majorid;
        return lesson_majorallowed.contains(user_info);
    }

    private boolean checkCapacityConstraint(User user, Lesson lesson){
        return (lesson.getClassmates().size() < lesson.getCapacity());
    }

    private boolean checkTakenConstraint(User user, Lesson lesson){
        String lessoncode = lesson.getLessoncode();
        return (!user.getLessonsTaken().stream().map(Lesson::getLessoncode).toList().contains(lessoncode));
    }

    private boolean checkTakingConstraint(User user, Lesson lesson){
        String lessoncode = lesson.getLessoncode();
        return (!user.getLessonsTaking().stream().map(Lesson::getLessoncode).toList().contains(lessoncode));
    }

    private boolean checkTimeArrangeConstraint(User user, Lesson lesson){
        List<Classtime> classtimes_occupied = new ArrayList<>();
        List<Classtime> classtimes_required = new ArrayList<>();
        List<Lesson> lessonsTaking = user.getLessonsTaking();
        for (Lesson lesson_taking : lessonsTaking) {
            classtimes_occupied.addAll(lesson_taking.getArranges().stream().map(Classarrange::getClasstime).toList());
        }
        classtimes_required = lesson.getArranges().stream().map(Classarrange::getClasstime).toList();
        return Collections.disjoint(classtimes_occupied, classtimes_required);
    }

}
