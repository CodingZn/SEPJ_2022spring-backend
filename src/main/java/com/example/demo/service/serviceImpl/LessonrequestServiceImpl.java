package com.example.demo.service.serviceImpl;

import com.example.demo.bean.Lessonrequest;
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
    private final UltimatecontrolMapper controlMapper;

    @Autowired
    public LessonrequestServiceImpl(LessonrequestMapper lessonrequestMapper, UltimatecontrolMapper controlMapper) {
        this.lessonrequestMapper = lessonrequestMapper;
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
        lessonrequestMapper.save(bean);
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
