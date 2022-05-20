package com.example.demo.service.serviceImpl;

import com.example.demo.bean.Classarrange;
import com.example.demo.bean.Classroom;
import com.example.demo.bean.Classtime;
import com.example.demo.exceptions.MyException;
import com.example.demo.mapper.ClassarrangeMapper;
import com.example.demo.mapper.ClassroomMapper;
import com.example.demo.mapper.ClasstimeMapper;
import com.example.demo.service.GeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ClassroomServiceImpl implements GeneralService<Classroom> {
    private final ClassroomMapper classroomMapper;
    private final ClasstimeMapper classtimeMapper;
    private final ClassarrangeMapper classarrangeMapper;

    @Autowired
    public ClassroomServiceImpl(ClassroomMapper classroomMapper, ClasstimeMapper classtimeMapper, ClassarrangeMapper classarrangeMapper) {
        this.classroomMapper = classroomMapper;
        this.classtimeMapper = classtimeMapper;
        this.classarrangeMapper = classarrangeMapper;
        updateArranges();
    }

    private void createArranges(Classroom newClassroom){
        List<Classtime> classtimeList = classtimeMapper.findAll();
        for (Classtime classtime : classtimeList){
            Classarrange classarrange = new Classarrange();
            classarrange.setClassroom(newClassroom);
            classarrange.setClasstime(classtime);
            classarrangeMapper.save(classarrange);
        }
    }

    private void deleteArranges(Classroom oldClassroom){
        List<Classtime> classtimeList = classtimeMapper.findAll();
        for (Classtime classtime : classtimeList){
            Classarrange classarrange = classarrangeMapper.findByClassroomAndClasstime(oldClassroom, classtime);
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
        List<Classarrange> classarrangeList = new ArrayList<>();
        List<Classtime> classtimeList = classtimeMapper.findAll();
        for (Classtime classtime : classtimeList){
            Classarrange classarrange = new Classarrange();
            classarrange.setClassroom(bean);
            classarrange.setClasstime(classtime);
            classarrangeList.add(classarrange);
        }
        bean.setClassarranges(classarrangeList);
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
            List<Classarrange> arranges= classarrangeMapper.findAllByClassroom(bean1);
            for(Classarrange classarrange : arranges){
                if (classarrange.getUplesson()!=null)
                    throw new MyException("有依赖此教室的课程！不能删除！");
            }
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
