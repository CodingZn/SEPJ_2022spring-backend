package com.example.demo.service.serviceImpl;

import com.example.demo.bean.School;
import com.example.demo.mapper.SchoolMapper;
import com.example.demo.service.GeneralService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class SchoolServiceImpl implements GeneralService<School> {
    private final SchoolMapper schoolMapper;

    public SchoolServiceImpl(SchoolMapper schoolMapper) {
        this.schoolMapper = schoolMapper;
    }

    @Override
    public List<String> getAllIds() {
        List<School> majorList = schoolMapper.findAll();
        return majorList.stream().map(School::getSchoolid).toList();
    }

    @Override
    public School getABean(String id) {
        return schoolMapper.findBySchoolid(id);
    }

    @Override
    public List<School> getAllBeans() {
        return schoolMapper.findAll();
    }

    @Override
    public String createABean(School bean) {
        schoolMapper.save(bean);
        return "Success";
    }

    @Override
    public String createBeans(List<School> beans) {
        beans.removeIf(Objects::isNull);
        for(School bean : beans){
            createABean(bean);
        }
        return "Success";
    }

    @Override
    public String changeABean(String id, School bean) {//只根据主键修改，不创建
        School bean1 = getABean(id);
        if (bean1 == null) {
            return "NotFound";
        }
        bean.setSchoolid(bean1.getSchoolid());
        schoolMapper.save(bean);
        return "Success";
    }

    @Override
    public String deleteABean(String id) {

        School bean = schoolMapper.findBySchoolid(id);
        if (bean != null) {
            schoolMapper.delete(bean);
            return "Success";
        } else {
            return "NotFound";
        }
    }

    @Override
    public String deleteBeans(List<?> ids) {
        List<String> idList = (List<String>) ids;
        for(String id : idList) {
            schoolMapper.deleteById(id);
        }
        return "Success";
    }
}
