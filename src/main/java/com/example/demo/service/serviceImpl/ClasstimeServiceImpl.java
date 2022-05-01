package com.example.demo.service.serviceImpl;

import com.example.demo.bean.Classtime;
import com.example.demo.mapper.straightMappers.ClasstimeMapper;
import com.example.demo.service.GeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClasstimeServiceImpl implements GeneralService<Classtime> {
    private final ClasstimeMapper classtimeMapper;

    @Autowired
    public ClasstimeServiceImpl(ClasstimeMapper classtimeMapper) {
        this.classtimeMapper = classtimeMapper;
    }

    @Override
    public List<String> getAllIds() {
        return null;
    }

    @Override
    public Classtime getABean(String id) {
        return null;
    }

    @Override
    public List<Classtime> getAllBeans() {
        return null;
    }

    @Override
    public String createABean(String id, Classtime bean) {
        return null;
    }

    @Override
    public String createBeans(List<Classtime> beans) {
        return null;
    }

    @Override
    public String changeABean(String id, Classtime bean) {
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
