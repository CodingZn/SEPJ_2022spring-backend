package com.example.demo.controller;

import com.alibaba.fastjson.JSONArray;
import com.example.demo.bean.Classroom;
import com.example.demo.service.GeneralService;
import com.example.demo.utils.BeanTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.example.demo.utils.JWTUtils.*;

@RestController
@CrossOrigin("http://localhost:3000")
public class ClassroomController extends BasicController<Classroom> {

    private final GeneralService<Classroom> classroomService;

    @Autowired
    public ClassroomController(GeneralService<Classroom> classroomService) {
        this.classroomService = classroomService;
    }


    /* 该类中所有的方法都来自继承 */


    @Override
    String getIds() {
        return "classroomids";
    }

    @Override
    String getBean() {
        return "classroom";
    }

    @Override
    String getBeans() {
        return "classrooms";
    }

    /* 1-查--getAllIds--获取所有id*/
    @Override
    Map<String, Object> getAllIds_impl(String authority, String userid) {
        Map<String, Object> map = new HashMap<>();
        switch (authority){
            case AdminAuthority, TeacherAuthority, StudentAuthority ->{
                map.put("result", "Success");
                map.put(getIds() , classroomService.getAllIds());
            }
            default -> {
                map.put("result", "NoAuth");
            }
        }
        return map;
    }

    @Override
    @RequestMapping(value="/classrooms/classroomids", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getAllIds(@RequestHeader(value="Authentication") String authentication) {
        return super.getAllIds(authentication);
    }

    /* 2-查--getABean--获取一个实体*/
    @Override
    Map<String, Object> getABean_impl(String authority, String userid, String key) {
        Map<String, Object> map = new HashMap<>();

        switch (authority){
            case AdminAuthority, TeacherAuthority, StudentAuthority ->{
                if (classroomService.getABean(key) != null){
                    map.put("result", "Success");
                    map.put(getBean() , classroomService.getABean(key));
                }
                else{
                    map.put("result", "NotFound");
                }
            }
            default -> {
                map.put("result", "NoAuth");
            }
        }
        return map;
    }

    @Override
    @RequestMapping(value="/classroom/{classroomid}", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getABean(@RequestHeader(value="Authentication") String authentication,
                                                        @PathVariable("classroomid") String key) {
        return super.getABean(authentication, key);
    }

    /* 3-查--getAllBeans--获取全部实体*/
    @Override
    Map<String, Object> getAllBeans_impl(String authority, String userid) {
        Map<String, Object> map = new HashMap<>();

        switch (authority) {
            case AdminAuthority, TeacherAuthority, StudentAuthority -> {
                map.put(getBeans(), classroomService.getAllBeans());
                map.put("result", "Success");
            }
            default -> {
                map.put("result", "NoAuth");
            }
        }
        return map;
    }

    @Override
    @RequestMapping(value = "/classrooms", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getAllBeans(@RequestHeader(value="Authentication") String authentication) {
        return super.getAllBeans(authentication);
    }

    /* 4-增--createABean--新增一个实体*/
    @Override
    Map<String, Object> createABean_impl(String authority, String userid, String key, Classroom bean) {
        Map<String, Object> map = new HashMap<>();
        switch (authority){
            case AdminAuthority->{
                map.put("result", classroomService.createABean(key, bean));
            }
            default -> {
                map.put("result", "NoAuth");
            }
        }
        return map;
    }

    @Override
    @RequestMapping(value="/classroom/{classroomid}", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> createABean(@RequestHeader(value = "Authentication") String authentication,
                                                           @PathVariable("classroomid") String key,
                                                           @RequestBody Classroom bean){
        return super.createABean(authentication, key, bean);

    }

    /* 5-增--createBeans--新增多个实体*/
    @Override
    Map<String, Object> createBeans_impl(String authority, String userid, List<Classroom> beans) {
        Map<String, Object> map = new HashMap<>();
        switch (authority) {
            case AdminAuthority -> {
                map.put("result", classroomService.createBeans(beans));
            }
            default -> {
                map.put("result", "NoAuth");
            }
        }
        return map;
    }

    @Override
    @RequestMapping(value = "/classrooms", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> createBeans(@RequestHeader(value = "Authentication") String authentication,
                                                           @RequestBody JSONArray jsonArray, Class<Classroom> clazz) {
        return super.createBeans(authentication, jsonArray, clazz);
    }


    /* 6-改--rewriteABean--重写一个实体,put*/
    @Override
    Map<String, Object> rewriteABean_impl(String authority, String userid, String key, Classroom bean) {
        Map<String, Object> map = new HashMap<>();
        switch (authority){
            case AdminAuthority->{
                map.put("result", classroomService.changeABean(key, bean));
                return map;
            }
            default -> {
                map.put("result", "NoAuth");
            }
        }
        return map;
    }

    @Override
    @RequestMapping(value="/classroom/{classroomid}", method = RequestMethod.PUT)
    public ResponseEntity<Map<String, Object>> rewriteABean(@RequestHeader(value = "Authentication") String authentication,
                                                            @PathVariable("classroomid") String key,
                                                            @RequestBody Classroom bean) {
        return super.rewriteABean(authentication, key, bean);
    }

    /* 7-改--modifyABean--修改一个实体,patch*/
    @Override
    Map<String, Object> modifyABean_impl(String authority, String userid, String key, Classroom bean) {
        Map<String, Object> map = new HashMap<>();
        switch (authority){
            case AdminAuthority->{
                Classroom bean_ori = classroomService.getABean(key);
                if (bean_ori == null){
                    map.put("result", "NotFound");
                    return map;
                }
                map.put("result", "Success");
                String [] adminauth = {"name", "status", "capacity"};

                List<String> changeableList = new ArrayList<>(Arrays.asList(adminauth));
                Classroom bean_modified = BeanTools.modify(bean_ori, bean, changeableList);
                map.put("result", classroomService.changeABean(key,bean_modified));
                return map;
            }
            default -> {
                map.put("result", "NoAuth");
                return map;
            }
        }
    }

    @Override
    @RequestMapping(value="/classroom/{classroomid}", method = RequestMethod.PATCH)
    public ResponseEntity<Map<String, Object>> modifyABean(@RequestHeader(value = "Authentication") String authentication,
                                                           @PathVariable("classroomid") String key,
                                                           @RequestBody Classroom bean) {
        return super.modifyABean(authentication, key, bean);
    }

    /* 8-删--deleteABean--删除一个实体*/
    @Override
    Map<String, Object> delBean_impl(String authority, String userid, String key) {
        Map<String, Object> map = new HashMap<>();
        switch (authority){
            case AdminAuthority->{
                map.put("result", classroomService.deleteABean(key));
            }
            default -> {
                map.put("result", "NoAuth");
            }
        }
        return map;
    }

    @Override
    @RequestMapping(value="/classroom/{classroomid}", method = RequestMethod.DELETE)
    public ResponseEntity<Map<String, Object>> delBean(@RequestHeader(value="Authentication") String authentication,
                                                       @PathVariable("classroomid") String key) {
        return super.delBean(authentication, key);
    }

    /* 9-删--deleteBeans--删除多个实体*/
    @Override
    Map<String, Object> delBeans_impl(String authority, String userid, List<?> ids) {
        Map<String, Object> map = new HashMap<>();
        switch (authority) {
            case AdminAuthority -> {
                map.put("result", classroomService.deleteBeans(ids));
            }
            default -> {
                map.put("result", "NoAuth");
            }
        }
        return map;
    }

    @Override
    @RequestMapping(value = "/classrooms", method = RequestMethod.DELETE)
    public ResponseEntity<Map<String, Object>> delBeans(@RequestHeader(value = "Authentication") String authentication,
                                                        @RequestBody JSONArray jsonArray, Class<?> clazz) {
        return super.delBeans(authentication, jsonArray, String.class);
    }
}
