package com.example.demo.controller;

import com.alibaba.fastjson.JSONArray;
import com.example.demo.bean.School;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

public class SchoolController extends BasicController<School> {

    @Override
    String getId() {
        return null;
    }

    @Override
    String getIds() {
        return null;
    }

    @Override
    String getBean() {
        return null;
    }

    @Override
    Map<String, Object> getAllIds_impl(String authority, String name) {
        return null;
    }

    @Override
    Map<String, Object> getABean_impl(String authority, String id, String name) {
        return null;
    }

    @Override
    Map<String, Object> getAllBeans_impl(String authority) {
        return null;
    }

    @Override
    Map<String, Object> createABean_impl(String authority, String id, School bean, String name) {
        return null;
    }

    @Override
    Map<String, Object> createBeans_impl(String authority, JSONArray jsonArray) {
        return null;
    }

    @Override
    Map<String, Object> rewriteABean_impl(String authority, String id, School bean) {
        return null;
    }

    @Override
    Map<String, Object> modifyABean_impl(String authority, String id, School bean) {
        return null;
    }

    @Override
    Map<String, Object> delBean_impl(String authority, String keyword, String name) {
        return null;
    }

    @Override
    Map<String, Object> delBeans_impl(String authority) {
        return null;
    }
}
