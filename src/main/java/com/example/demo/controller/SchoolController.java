package com.example.demo.controller;

import com.example.demo.bean.School;
import com.example.demo.service.GeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("http://localhost:3000")
public class SchoolController extends BasicController<School> {

    private final GeneralService<School> schoolService;

    @Autowired
    public SchoolController(GeneralService<School> schoolService) {
        this.schoolService = schoolService;
    }


    /* 该类中所有的方法都来自继承 */


    @Override
    String getIds() {
        return "schoolids";
    }

    @Override
    String getBean() {
        return "school";
    }

    @Override
    String getBeans() {
        return "schools";
    }

    @Override
    Map<String, Object> getAllIds_impl(String authority, String userid) {
        return null;
    }

    @Override
    Map<String, Object> getABean_impl(String authority, String userid, String key) {
        return null;
    }

    @Override
    Map<String, Object> getAllBeans_impl(String authority, String userid) {
        return null;
    }

    @Override
    Map<String, Object> createABean_impl(String authority, String userid, String key, School bean) {
        return null;
    }

    @Override
    Map<String, Object> createBeans_impl(String authority, String userid, List<School> beans) {
        return null;
    }

    @Override
    Map<String, Object> rewriteABean_impl(String authority, String userid, String key, School bean) {
        return null;
    }

    @Override
    Map<String, Object> modifyABean_impl(String authority, String userid, String key, School bean) {
        return null;
    }

    @Override
    Map<String, Object> delBean_impl(String authority, String userid, String key) {
        return null;
    }

    @Override
    Map<String, Object> delBeans_impl(String authority, String userid, List<?> ids) {
        return null;
    }
}
