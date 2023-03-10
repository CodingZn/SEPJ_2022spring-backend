package com.example.demo.service.serviceImpl;

import com.example.demo.bean.Lesson;
import com.example.demo.bean.Lessonrequest;
import com.example.demo.bean.User;
import com.example.demo.mapper.LessonMapper;
import com.example.demo.mapper.LessonrequestMapper;
import com.example.demo.mapper.straightMappers.UltimatecontrolMapper;
import com.example.demo.service.GeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static com.example.demo.bean.specialBean.Ultimatectrl.KEY_SEMESTER_CONTROL;

@Service
public class LessonrequestServiceImpl implements GeneralService<Lessonrequest> {

    private final LessonrequestMapper lessonrequestMapper;
    private final LessonMapper lessonMapper;
    private final UltimatecontrolMapper controlMapper;

    @Autowired
    public LessonrequestServiceImpl(LessonrequestMapper lessonrequestMapper, LessonMapper lessonMapper, UltimatecontrolMapper controlMapper) {
        this.lessonrequestMapper = lessonrequestMapper;
        this.lessonMapper = lessonMapper;
        this.controlMapper = controlMapper;
    }

    @Override
    public List<String> getAllIds() {
        List<Lessonrequest> beans = lessonrequestMapper.findAll();
        return beans.stream().map(u -> String.valueOf(u.getLessonrequestid())).toList();
    }

    @Override
    public Lessonrequest getABean(String id) {
        return lessonrequestMapper.findByLessonrequestid(Integer.parseInt(id));
    }

    @Override
    public List<Lessonrequest> getAllBeans() {
        return lessonrequestMapper.findAll();
    }

    @Override
    public String createABean(Lessonrequest bean) {
        bean.setSemester(controlMapper.findByName(KEY_SEMESTER_CONTROL).getValue());
        bean.setStatus(Lessonrequest.Status.pending);
        if (bean.getApplicant().getUsertype() == User.Type.student) {
            String result = checkLessonrequest(bean);
            if (!result.equals("Success"))
                return result;
        }
        lessonrequestMapper.save(bean);
        return "Success";
    }

    //????????????????????????????????????????????????Success?????????
    private String checkLessonrequest (Lessonrequest lessonrequest){
        User student = lessonrequest.getApplicant();
        Lesson lesson = lessonMapper.findByLessonid(lessonrequest.getLesson().getLessonid());
        if (lesson == null)
            return "???????????????";
        if (!Objects.equals(lesson.getSemester(), lessonrequest.getSemester())){
            return "?????????????????????????????????";
        }
        if (lesson.getCapacity() > lesson.getClassmates().size()){
            return "??????????????????????????????????????????????????????";
        }
        if (student.getLessonsTaken().stream().map(Lesson::getLessoncode).toList().contains(lesson.getLessoncode()))
            return "????????????????????????????????????";
        if (student.getLessonsTaking().stream().map(Lesson::getLessoncode).toList().contains(lesson.getLessoncode()))
            return "????????????????????????????????????????????????";

        return "Success";
    }

    @Override
    public String createBeans(List<Lessonrequest> beans) {
        beans.removeIf(Objects::isNull);
        for(Lessonrequest bean : beans){
            createABean(bean);
        }
        return "Success";
    }

    @Override
    public String changeABean(String id, Lessonrequest bean) {
        Lessonrequest bean1 = getABean(id);
        if (bean1 == null)
            return "NotFound";
        else{
            bean.setLessonrequestid(Integer.parseInt(id));

            lessonrequestMapper.save(bean);
            return "Success";
        }
    }

    private String deleteABean(int id){
        Lessonrequest bean1 = lessonrequestMapper.findByLessonrequestid(id);
        if (bean1 != null) {
            lessonrequestMapper.delete(bean1);
            return "Success";
        } else {
            return "NotFound";
        }
    }

    @Override
    public String deleteABean(String id) {
        int id_int = Integer.parseInt(id);
        return deleteABean(id_int);
    }

    @Override
    public String deleteBeans(List<?> ids) {
        List<Integer> beanids = (List<Integer>) ids;
        for(Integer beanid : beanids) {
            lessonrequestMapper.deleteByLessonrequestid(beanid);
        }
        return "Success";
    }
}
