package com.example.demo.service.serviceImpl;

import com.example.demo.bean.LessonRequest;
import com.example.demo.service.GeneralService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LessonreqServiceImpl implements GeneralService<LessonRequest> {

    @Override
    public List<String> getAllIds() {
        return null;
    }

    @Override
    public LessonRequest getABean(String id) {
        return null;
    }

    @Override
    public List<LessonRequest> getAllBeans() {
        return null;
    }

    @Override
    public String createABean(String id, LessonRequest bean) {
        return null;
    }

    @Override
    public String createBeans(List<LessonRequest> beans) {
        return null;
    }

    @Override
    public String changeABean(String id, LessonRequest bean) {
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
