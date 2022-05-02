package com.example.demo.service.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.example.demo.bean.Classarrange;
import com.example.demo.bean.Lesson;
import com.example.demo.mapper.ClassarrangeMapper;
import com.example.demo.mapper.LessonMapper;
import com.example.demo.service.GeneralService;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class LessonServiceImpl implements GeneralService<Lesson> {
    private final LessonMapper lessonMapper;
    private final ClassarrangeMapper classarrangeMapper;

    @Autowired
    public LessonServiceImpl(LessonMapper lessonMapper, ClassarrangeMapper classarrangeMapper) {
        this.lessonMapper = lessonMapper;
        this.classarrangeMapper = classarrangeMapper;
    }

    @Override
    public List<String> getAllIds() {
        List<Lesson> lessonList = lessonMapper.findAll();
        return lessonList.stream().map(u -> String.valueOf(u.getLessonid())).toList();
    }

    @Override
    public Lesson getABean(String lessonid) {
        return lessonMapper.findByLessonid(Integer.parseInt(lessonid));

    }


    @Override
    public List<Lesson> getAllBeans() {
        return lessonMapper.findAll();
    }

    @Override
    public String createABean(Lesson lesson) {
        List<Classarrange> arranges = lesson.getArranges();
        classarrangeMapper.saveAll(arranges);
        lessonMapper.save(lesson);
        return "Success";
    }

    @Override
    public String createBeans(List<Lesson> beans) {
        beans.removeIf(Objects::isNull);
        for(Lesson lesson : beans){
            createABean(lesson);
        }
        return "Success";
    }

    @Override
    public String changeABean(String lessonid, Lesson lesson) {

        Lesson lesson1 = getABean(lessonid);
        if (lesson1 == null)
            return "NotFound";
        else{
            lesson.setLessonid(Integer.parseInt(lessonid));
            classarrangeMapper.saveAll(lesson.getArranges());
            lessonMapper.save(lesson);

            //需要删除不用了的arranges

            return "Success";
        }
    }

    private String deleteABean(int lessonid){
        Lesson lesson = lessonMapper.findByLessonid(lessonid);
        if (lesson != null) {
            lessonMapper.delete(lesson);
            classarrangeMapper.deleteAll(lesson.getArranges());
            return "Success";
        } else {
            return "NotFound";
        }
    }

    @Override
    public String deleteABean(String lessonid) {
        int lessonid_int = Integer.parseInt(lessonid);
        return deleteABean(lessonid_int);
    }

    @Override
    public String deleteBeans(List<?> ids) {
        List<Integer> lessonids = (List<Integer>) ids;
        for(Integer lessonid : lessonids) {
            lessonMapper.deleteByLessonid(lessonid);
        }
        return "Success";
    }

}
