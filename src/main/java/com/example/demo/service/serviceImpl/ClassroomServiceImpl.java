package com.example.demo.service.serviceImpl;

import com.example.demo.bean.Classroom;
import com.example.demo.mapper.ClassroomMapper;
import com.example.demo.service.GeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ClassroomServiceImpl implements GeneralService<Classroom> {
    private final ClassroomMapper classroomMapper;

    @Autowired
    public ClassroomServiceImpl(ClassroomMapper classroomMapper) {
        this.classroomMapper = classroomMapper;
    }

    @Override
    public List<String> getAllIds() {
        List<Classroom> beans = classroomMapper.findAll();
        return beans.stream().map(u -> String.valueOf(u.getClassroomid())).toList();
    }

    @Override
    public Classroom getABean(String id) {
        return classroomMapper.findByClassroomid(Integer.parseInt(id));
    }

    @Override
    public List<Classroom> getAllBeans() {
        return classroomMapper.findAll();
    }

    @Override
    public String createABean(Classroom bean) {
        classroomMapper.save(bean);
        return "Success";
    }

    @Override
    public String createBeans(List<Classroom> beans) {
        beans.removeIf(Objects::isNull);
        for(Classroom bean : beans){
            createABean(bean);
        }
        return "Success";
    }

    @Override
    public String changeABean(String id, Classroom bean) {
        Classroom bean1 = getABean(id);
        if (bean1 == null)
            return "NotFound";
        else{
            bean.setClassroomid(Integer.parseInt(id));

            classroomMapper.save(bean);
            return "Success";
        }
    }

    private String deleteABean(int id){
        Classroom bean1 = classroomMapper.findByClassroomid(id);
        if (bean1 != null) {
            classroomMapper.delete(bean1);
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
            classroomMapper.deleteByClassroomid(beanid);
        }
        return "Success";
    }
}
