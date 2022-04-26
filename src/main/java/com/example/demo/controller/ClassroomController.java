package com.example.demo.controller;

import com.alibaba.fastjson.JSONArray;
import com.example.demo.bean.trivialBeans.Classroom;
import com.example.demo.mapper.straightMappers.ClassroomMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.demo.bean.JWTUtils.*;

@RestController
@CrossOrigin("http://localhost:3000")
public class ClassroomController extends BasicController<Classroom> {

    private final ClassroomMapper classroomMapper;

    public ClassroomController(ClassroomMapper classroomMapper) {
        this.classroomMapper = classroomMapper;
    }

    /*查--获取单个教室*/
    @Override
    Map<String, Object> getABean_impl(String authority, String id, String name) {
        Map<String, Object> map = new HashMap<>();
        switch (authority) {
            case AdminAuthority, TeacherAuthority -> {
                Classroom classroom = classroomMapper.findByName(name);

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
    Map<String, Object> modifyABean_impl(String authority, String id, Classroom classroom) {
        Map<String, Object> map = new HashMap<>();
        switch (authority) {
            case AdminAuthority -> {
                if (classroomMapper.findByName(id) != null) {
                    classroom.setName(id);
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
}
