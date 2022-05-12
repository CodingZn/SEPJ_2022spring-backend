package com.example.demo.service.serviceImpl;

import com.example.demo.bean.Classarrange;
import com.example.demo.bean.Lesson;
import com.example.demo.mapper.ClassarrangeMapper;
import com.example.demo.mapper.LessonMapper;
import com.example.demo.service.GeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class LessonServiceImpl implements GeneralService<Lesson> {
    private final LessonMapper lessonMapper;
    private final ClassarrangeMapper arrangeMapper;

    @Autowired
    public LessonServiceImpl(LessonMapper lessonMapper, ClassarrangeMapper arrangeMapper) {
        this.lessonMapper = lessonMapper;
        this.arrangeMapper = arrangeMapper;
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
        List<Classarrange> arranges = lesson.getArranges();//此处的arrange无id
        lessonMapper.save(lesson);
        for (Classarrange arrange : arranges){
            Classarrange arrange0 = arrangeMapper.findByClassroomAndClasstime(arrange.getClassroom(),arrange.getClasstime());
            arrange.setId(arrange0.getId());
            arrange.setUplesson(lesson);
            arrangeMapper.save(arrange);
        }
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
            List<Classarrange> arranges1 = lesson1.getArranges();
            for (Classarrange arrange : arranges1){
                arrange.setUplesson(null);
                arrangeMapper.save(arrange);
            }
            List<Classarrange> arranges = lesson.getArranges();
            for (Classarrange arrange : arranges){
                arrange.setUplesson(lesson);
                arrangeMapper.save(arrange);
            }
            lessonMapper.save(lesson);
            return "Success";
        }
    }

    private String deleteABean(int lessonid){
        Lesson lesson = lessonMapper.findByLessonid(lessonid);
        if (lesson != null) {
            List<Classarrange> arranges = lesson.getArranges();
            for (Classarrange arrange : arranges){
                Classarrange arrange0 = arrangeMapper.findByClassroomAndClasstime(arrange.getClassroom(),arrange.getClasstime());
                arrange.setId(arrange0.getId());
                arrange.setUplesson(null);
                arrangeMapper.save(arrange);
            }
            lessonMapper.delete(lesson);
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
