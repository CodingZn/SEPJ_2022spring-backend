package com.example.demo.controller;

import com.alibaba.fastjson.JSONArray;
import com.example.demo.bean.trivialBeans.Classtime;
import com.example.demo.mapper.straightMappers.ClasstimeMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.demo.bean.JWTUtils.AdminAuthority;
import static com.example.demo.bean.JWTUtils.TeacherAuthority;

public class ClasstimeController extends BasicController<Classtime> {

    private final ClasstimeMapper classtimeMapper;

    public ClasstimeController(ClasstimeMapper classtimeMapper) {
        this.classtimeMapper = classtimeMapper;
    }

    /*查--获取一个上课时间对象*/
    @Override
    Map<String, Object> getABean_impl(String authority, String id, String name) {
        Map<String, Object> map = new HashMap<>();
        switch (authority) {
            case AdminAuthority, TeacherAuthority -> {
                Classtime classtime = classtimeMapper.findByName(name);

                map.put("classtime", classtime);
                map.put("result", "Success");
            }
            default -> {
                map.put("result", "NoAuth");
            }
        }
        return map;
    }

    @Override
    @RequestMapping(value = "/classtime/{name}", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getABean(@PathVariable("name") String name,
                                                        @RequestHeader(value = "Authentication") String authentication) {
        return super.getABean(name, authentication);
    }

    /*查--获取所有上课时间段*/
    @Override
    Map<String, Object> getAllBeans_impl(String authority) {
        Map<String, Object> map = new HashMap<>();
        switch (authority) {
            case AdminAuthority, TeacherAuthority -> {
                List<Classtime> classtimeList = classtimeMapper.findAll();
                List<String> classtimeNameList = classtimeList.stream().map(Classtime::getName).toList();

                map.put("names", classtimeNameList);
            }
            default -> {
                map.put("result", "NoAuth");
            }
        }
        return map;
    }

    @Override
    @RequestMapping(value = "/classtimes", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getAllBeans(@RequestHeader(value = "Authentication") String authentication) {
        return super.getAllBeans(authentication);
    }

    /*增--初始化上课时间*/
    @Override
    Map<String, Object> createBeans_impl(String authority, JSONArray jsonArray) {

        Map<String, Object> map = new HashMap<>();
        switch (authority) {
            case AdminAuthority -> {

                List<Classtime> classtimeList = jsonArray.toJavaList(Classtime.class);

                classtimeMapper.saveAll(classtimeList);

                map.put("result", "Success");
            }
            default -> {
                map.put("result", "NoAuth");
            }
        }
        return map;
    }

    @Override
    @RequestMapping(value = "/classtimes", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> createBeans(@RequestHeader(value = "Authentication") String authentication,
                                                           @RequestBody JSONArray jsonArray) {
        return super.createBeans(authentication, jsonArray);
    }


    /*删--删除所有上课时间对象*/
    @Override
    Map<String, Object> delBeans_impl(String authority) {
        Map<String, Object> map = new HashMap<>();
        switch (authority) {
            case AdminAuthority -> {
                classtimeMapper.deleteAll();
                map.put("result", "Success");
            }
            default -> {
                map.put("result", "NoAuth");
            }
        }
        return map;
    }

    @Override
    @RequestMapping(value = "/classrooms", method = RequestMethod.DELETE)
    public ResponseEntity<Map<String, Object>> delBeans(@RequestHeader(value = "Authentication") String authentication) {
        return super.delBeans(authentication);
    }

    /***************未使用的抽象方法******************/


    @Override
    Map<String, Object> delBean_impl(String authority, String keyword, String name) {
        return null;
    }

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
    Map<String, Object> createABean_impl(String authority, String id, Classtime bean, String name) {
        return null;
    }

    @Override
    Map<String, Object> rewriteABean_impl(String authority, String id, Classtime bean) {
        return null;
    }

    @Override
    Map<String, Object> modifyABean_impl(String authority, String id, Classtime bean) {
        return null;
    }
}
