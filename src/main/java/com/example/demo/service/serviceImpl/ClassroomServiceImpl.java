package com.example.demo.service.serviceImpl;

import com.example.demo.bean.Classroom;
import com.example.demo.mapper.ClassroomMapper;
import com.example.demo.service.GeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassroomServiceImpl implements GeneralService<Classroom> {
    private final ClassroomMapper classroomMapper;

    @Autowired
    public ClassroomServiceImpl(ClassroomMapper classroomMapper) {
        this.classroomMapper = classroomMapper;
    }

    @Override
    public List<String> getAllIds() {
        return null;
    }

    @Override
    public Classroom getABean(String id) {
        return null;
    }

    @Override
    public List<Classroom> getAllBeans() {
        return null;
    }

    @Override
    public String createABean(String id, Classroom bean) {
        return null;
    }

    @Override
    public String createBeans(List<Classroom> beans) {
        return null;
    }

    @Override
    public String changeABean(String id, Classroom bean) {
        return null;
    }

    @Override
    public String deleteABean(String id) {
        return null;
    }

    @Override
    public String deleteBeans(List<?> ids) {
        return null;
    }
}
