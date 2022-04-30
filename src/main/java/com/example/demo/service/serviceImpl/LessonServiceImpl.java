package com.example.demo.service.serviceImpl;

import com.example.demo.bean.Lesson;
import com.example.demo.mapper.LessonMapper;
import com.example.demo.service.GeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public List<String> getAllIds() {
        List<Lesson> lessonList = lessonMapper.findAll();

        List<String> a = lessonList.stream().map( u -> String.valueOf(u.getLessonid())).toList();

        System.out.println("a"+a);

        return a;
    }

    @Override
    public Lesson getABean(String lessonid) {
        Lesson lesson;
        lesson = lessonMapper.findByLessonid(lessonid);
        return lesson;

    }


    @Override
    public List<Lesson> getAllBeans() {
        return lessonMapper.findAll();
    }

    @Override
    public String createABean(String lessonid, Lesson lesson) {
        Lesson lesson1 = getABean(lessonid);
        if (lesson1 == null){
            lesson.setLessonid(lessonid);
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
    public String changeABean(String lessonid, Lesson lesson) {

        Lesson lesson1 = getABean(lessonid);
        if (lesson1 == null)
            return "NotFound";
        else{
            lesson.setLessonid(lessonid);
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
    public String deleteABean(String lessonid) {
        Lesson lesson = lessonMapper.findByLessonid(lessonid);
        if (lesson != null) {
            lessonMapper.delete(lesson);
            return "Success";
        } else {
            return "NotFound";
        }
    }

}
