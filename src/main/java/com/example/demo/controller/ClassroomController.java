package com.example.demo.controller;

import com.alibaba.fastjson.JSONArray;
import com.example.demo.bean.Classroom;
import com.example.demo.mapper.straightMappers.ClassroomMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.demo.utils.JWTUtils.*;

@RestController
@CrossOrigin("http://localhost:3000")
public class ClassroomController extends BasicController<Classroom> {

    private final ClassroomMapper classroomMapper;

    @Autowired
    public ClassroomController(ClassroomMapper classroomMapper) {
        this.classroomMapper = classroomMapper;
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

    /*查--获取单个教室*/
    @Override
    Map<String, Object> getABean_impl(String authority, String id, String searchid) {
        Map<String, Object> map = new HashMap<>();
        switch (authority) {
            case AdminAuthority, TeacherAuthority -> {
                Classroom classroom = classroomMapper.findByName(id);
                //下方法传入的name对应basicController方法里keyword,再对应本方法id位置

                map.put("classroom", classroom);
                map.put("result", "Success");
            }
            default -> {
                map.put("result", "NoAuth");
            }
        }
        return map;
    }

    @Override
    @RequestMapping(value = "/classroom/{name}", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getABean(@PathVariable("name") String name,
                                                        @RequestHeader("Authentication") String authentication) {
        return super.getABean(name, authentication);
    }

    @Override
    Map<String, Object> getAllBeans_impl(String authority, String userid) {
        return null;
    }

    /*查--获取所有教室名*/
    @Override
    Map<String, Object> getAllBeans_impl(String authority) {
        Map<String, Object> map = new HashMap<>();
        switch (authority) {
            case AdminAuthority, TeacherAuthority -> {
                List<Classroom> classroomList = classroomMapper.findAll();
                List<String> classroomNameList = classroomList.stream().map(Classroom::getName).toList();

                map.put("names", classroomNameList);
            }
            default -> {
                map.put("result", "NoAuth");
            }
        }
        return map;
    }

    @Override
    @RequestMapping(value = "/classrooms", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getAllBeans(@RequestHeader("Authentication") String authentication) {
        return super.getAllBeans(authentication);
    }

    @Override
    Map<String, Object> createABean_impl(String authority, String userid, String key, Classroom bean) {
        return null;
    }

    @Override
    Map<String, Object> createBeans_impl(String authority, String userid, List<Classroom> beans) {
        return null;
    }

    @Override
    Map<String, Object> rewriteABean_impl(String authority, String userid, String key, Classroom bean) {
        return null;
    }

    @Override
    Map<String, Object> modifyABean_impl(String authority, String userid, String key, Classroom bean) {
        return null;
    }

    /*增--初始化教室*/
    Map<String, Object> createBeans_impl(String authority, JSONArray jsonArray) {

        Map<String, Object> map = new HashMap<>();
        switch (authority) {
            case AdminAuthority -> {
                List<Classroom> classroomList = jsonArray.toJavaList(Classroom.class);
                classroomMapper.saveAll(classroomList);
                map.put("result", "Success");
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
                                                           @RequestBody JSONArray jsonArray) {
        return super.createBeans(authentication, jsonArray);
    }

    /*改--修改单个教室状态*/
    @Override
    Map<String, Object> modifyABean_impl(String authority, String name, Classroom classroom) {
        Map<String, Object> map = new HashMap<>();
        switch (authority) {
            case AdminAuthority -> {
                if (classroomMapper.findByName(name) != null) {
                    classroom.setName(name);
                    classroomMapper.save(classroom);
                    map.put("result", "Success");
                } else
                    map.put("result", "NotFound");
            }
            default -> {
                map.put("result", "NoAuth");
            }
        }
        return map;
    }

    @Override
    @RequestMapping(value = "/classroom/{name}", method = RequestMethod.PUT)
    public ResponseEntity<Map<String, Object>> modifyABean(@PathVariable("name") String name,
                                                           @RequestBody Classroom classroom,
                                                           @RequestHeader(value = "Authentication") String authentication) {//paramater to be changed
        return super.modifyABean(name, classroom, authentication);
    }

    /*删--删除所有教室*/
    @Override
    Map<String, Object> delBeans_impl(String authority) {
        Map<String, Object> map = new HashMap<>();
        switch (authority) {
            case AdminAuthority -> {
                classroomMapper.deleteAll();
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
    public ResponseEntity<Map<String, Object>> delBeans(@RequestHeader("Authentication") String authentication) {
        return super.delBeans(authentication);
    }

    /***************未使用的抽象方法******************/
    @Override
    Map<String, Object> rewriteABean_impl(String authority, String id, Classroom bean) {
        return null;
    }

    @Override
    Map<String, Object> createABean_impl(String authority, String id, Classroom bean, String name) {
        return null;
    }

    @Override
    Map<String, Object> delBean_impl(String authority, String keyword, String name) {
        return null;
    }

    @Override
    Map<String, Object> delBeans_impl(String authority, String userid, List<?> ids) {
        return null;
    }

    @Override
    Map<String, Object> getAllIds_impl(String authority, String name) {
        return null;
    }
}
