package com.example.demo.service.serviceImpl;

import com.example.demo.bean.Classarrange;
import com.example.demo.bean.Classroom;
import com.example.demo.bean.Classtime;
import com.example.demo.mapper.ClassarrangeMapper;
import com.example.demo.mapper.ClassroomMapper;
import com.example.demo.mapper.ClasstimeMapper;
import com.example.demo.service.GeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ClasstimeServiceImpl implements GeneralService<Classtime> {
    private final ClasstimeMapper classtimeMapper;
    private final ClassroomMapper classroomMapper;
    private final ClassarrangeMapper classarrangeMapper;

    @Autowired
    public ClasstimeServiceImpl(ClasstimeMapper classtimeMapper, ClassroomMapper classroomMapper, ClassarrangeMapper classarrangeMapper) {
        this.classtimeMapper = classtimeMapper;
        this.classroomMapper = classroomMapper;
        this.classarrangeMapper = classarrangeMapper;
        updateArranges();
    }

    private void createArranges(Classtime classtime){
        List<Classroom> classroomList = classroomMapper.findAll();
        for (Classroom classroom : classroomList){
            Classarrange classarrange = new Classarrange();
            classarrange.setClassroom(classroom);
            classarrange.setClasstime(classtime);
            classarrangeMapper.save(classarrange);
        }
    }

    private void deleteArranges(Classtime classtime){
        List<Classroom> classroomList = classroomMapper.findAll();
        for (Classroom classroom : classroomList){
            Classarrange classarrange = classarrangeMapper.findByClassroomAndClasstime(classroom, classtime);
            classarrangeMapper.delete(classarrange);
        }
    }

    private void updateArranges(){
        List<Classtime> classtimeList = classtimeMapper.findAll();
        List<Classroom> classroomList = classroomMapper.findAll();
        for(Classroom classroom : classroomList){
            for(Classtime classtime:classtimeList){
                Classarrange arrange = classarrangeMapper.findByClassroomAndClasstime(classroom,classtime);
                if (arrange == null){
                    arrange = new Classarrange();
                    arrange.setClassroom(classroom);
                    arrange.setClasstime(classtime);
                    classarrangeMapper.save(arrange);
                }
            }
        }
    }

    @Override
    public List<String> getAllIds() {
        List<Classtime> beans = classtimeMapper.findAll();
        return beans.stream().map(u -> String.valueOf(u.getClasstimeid())).toList();
    }

    @Override
    public Classtime getABean(String id) {
        return classtimeMapper.findByClasstimeid(Integer.parseInt(id));
    }

    @Override
    public List<Classtime> getAllBeans() {
        return classtimeMapper.findAll();
    }

    @Override
    public String createABean(Classtime bean) {
        classtimeMapper.save(bean);
        createArranges(bean);
        return "Success";
    }

    @Override
    public String createBeans(List<Classtime> beans) {
        beans.removeIf(Objects::isNull);
        for(Classtime bean : beans){
            createABean(bean);
        }
        return "Success";
    }

    @Override
    public String changeABean(String id, Classtime bean) {
        Classtime bean1 = getABean(id);
        if (bean1 == null)
            return "NotFound";
        else{
            bean.setClasstimeid(Integer.parseInt(id));

            classtimeMapper.save(bean);
            return "Success";
        }
    }

    private String deleteABean(int id){
        Classtime bean1 = classtimeMapper.findByClasstimeid(id);
        if (bean1 != null) {
            deleteArranges(bean1);
            classtimeMapper.delete(bean1);
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
            classtimeMapper.deleteByClasstimeid(beanid);
        }
        return "Success";
    }
}
