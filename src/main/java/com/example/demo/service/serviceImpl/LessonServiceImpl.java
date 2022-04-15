package com.example.demo.service.serviceImpl;

import com.example.demo.bean.Lesson;
import com.example.demo.mapper.LessonMapper;
import com.example.demo.service.GeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class LessonServiceImpl implements GeneralService<Lesson> {
    private final LessonMapper lessonMapper;
    private final DependValueVerify dependValueVerify;

    @Autowired
    public LessonServiceImpl(LessonMapper lessonMapper, DependValueVerify dependValueVerify) {
        this.lessonMapper = lessonMapper;
        this.dependValueVerify = dependValueVerify;
    }

    @Override
    public String getANewId() {
        List<Lesson> lessonList = lessonMapper.findAll();

        Lesson maxlesson;
        if (lessonList.stream().max(Comparator.comparing(Lesson::getLessonid)).isPresent()){
            maxlesson = lessonList.stream().max(Comparator.comparing(Lesson::getLessonid)).get();
            System.out.println("getMaxMajornumber=");
            System.out.println(maxlesson.getLessonid());

            return String.valueOf(maxlesson.getLessonid() + 1);
        }
        else
            return "1";

    }

    @Override
    public List<String> getAllIds() {
        List<Lesson> lessonList = lessonMapper.findAll();

        List<String> a = lessonList.stream().map( u -> String.valueOf(u.getLessonid())).toList();

        System.out.println("a"+a);

        return a;
    }

    @Override
    public Lesson getABean(String lessonid_str) {
        Lesson lesson;
        try {
            int lessonid = Integer.parseInt(lessonid_str);
            lesson = lessonMapper.findByLessonid(lessonid);
            return lesson;
        } catch (NumberFormatException e) {//传入lessonid格式不对
            return null;
        }
    }


    @Override
    public List<Lesson> getAllBeans() {
        return lessonMapper.findAll();
    }

    @Override
    public String createABean(String lessonid_str, Lesson lesson) {
        Lesson lesson1 = getABean(lessonid_str);
        if (lesson1 == null){
            lesson.setLessonid(Integer.parseInt(lessonid_str));
            if ((lesson.getLessonname().equals("") || lesson.getSchool().equals("")))
                return "FormError";
            else if (!dependValueVerify.lessonDependCheck(lesson))
                return "DependError";
            else{
                lessonMapper.save(lesson);
                return "Success";
            }
        }
        else return "Conflict";
    }

    @Override
    public String changeABean(String lessonid_str, Lesson lesson) {

        Lesson lesson1 = getABean(lessonid_str);
        if (lesson1 == null)
            return "NotFound";
        else{
            lesson.setLessonid(Integer.parseInt(lessonid_str));
            if ((lesson.getLessonname().equals("") || lesson.getSchool().equals("")))
                return "FormError";
            else if (!dependValueVerify.lessonDependCheck(lesson))
                return "DependError";
            else{
                lessonMapper.save(lesson);
                return "Success";
            }
        }
    }

    @Override
    public String deleteABean(String lessonid_str) {
        int lessonid;
        try {
            lessonid = Integer.parseInt(lessonid_str);
        } catch (NumberFormatException e) {//传入 lessonid_str 格式不对
            return "NotFound";
        }
        Lesson lesson = lessonMapper.findByLessonid(lessonid);
        if (lesson != null) {
            lessonMapper.delete(lesson);
            return "Success";
        } else {
            return "NotFound";
        }
    }

}
