package com.example.demo.service.serviceImpl;

import com.example.demo.bean.Classarrange;
import com.example.demo.bean.Lesson;
import com.example.demo.mapper.ClassarrangeMapper;
import com.example.demo.mapper.LessonMapper;
import com.example.demo.service.GeneralService;
import com.example.demo.utils.BeanTools;
import com.example.demo.utils.ConstraintsVerify;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
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
        //设置课程安排，同时检查冲突
        List<Classarrange> arranges0 = lesson.getClassarrange();//此处的arrange无id
        List<Classarrange> arranges1 = new ArrayList<>();
        for (Classarrange arrange0 : arranges0){
            Classarrange arrange1 = arrangeMapper.findByClassroomAndClasstime(arrange0.getClassroom(),arrange0.getClasstime());
            if(arrange1.getUplesson() != null){
                return "Conflict";
            }
            arrange1.setUplesson(lesson);
            arranges1.add(arrange1);
        }
        lesson.setClassarrange(arranges1);

        lesson.setLessonnumber(lesson.getLessoncode() + "." + lesson.getLessonnumber());
        if (lessonMapper.findByLessonnumberAndSemester(lesson.getLessonnumber(), lesson.getSemester()) != null)
            return "Conflict";

        List<Lesson> lessons_same_code1 = modifySametypeLessons(lesson);
        lessonMapper.save(lesson);
        lessonMapper.saveAll(lessons_same_code1);

        return "Success";
    }

    @NotNull
    private List<Lesson> modifySametypeLessons(Lesson lesson) {
        //同步修改同类课程的通用属性
        List<Lesson> lessons_same_code = lessonMapper.findAllByLessoncode(lesson.getLessoncode());
        lessons_same_code.remove(lesson);
        List<Lesson> lessons_same_code1 = new ArrayList<>();
        String[] geneproperty = {"lessonname", "school", "hour", "credit", "semester"};
        List<String> changeableList = new ArrayList<>(Arrays.asList(geneproperty));
        for (Lesson les:lessons_same_code) {
            lessons_same_code1.add(BeanTools.modify(les, lesson, changeableList));
        }
        return lessons_same_code1;
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
            List<Classarrange> arranges0 = lesson.getClassarrange();
            List<Classarrange> arranges1 = new ArrayList<>();
            for (Classarrange arrange0 : arranges0){
                Classarrange arrange1 = arrangeMapper.findByClassroomAndClasstime(arrange0.getClassroom(),arrange0.getClasstime());
                if(arrange1.getUplesson() != null && arrange1.getUplesson().getLessonid() != lesson.getLessonid()){
                    return "Conflict";
                }
                arrange1.setUplesson(lesson);
                arranges1.add(arrange1);

            }
            lesson.setClassarrange(arranges1);
            lessonMapper.saveAndFlush(lesson);

            return "Success";
        }
    }

    private String deleteABean(int lessonid){
        Lesson lesson = lessonMapper.findByLessonid(lessonid);
        if (lesson != null) {
            if (ConstraintsVerify.LessonHavingDependency(lesson))
                return "DependError";
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
