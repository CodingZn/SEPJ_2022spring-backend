package com.example.demo.service.serviceImpl;

import com.example.demo.bean.School;
import com.example.demo.service.GeneralService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SchoolServiceImpl implements GeneralService<School> {

    @Override
    public List<String> getAllIds() {
        return null;
    }

    @Override
    public School getABean(String id) {
        return null;
    }

    @Override
    public List<School> getAllBeans() {
        return null;
    }

    @Override
    public String createABean(String id, School bean) {
        return null;
    }

    @Override
    public String createBeans(List<School> beans) {
        return null;
    }

    @Override
    public String changeABean(String id, School bean) {
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