package com.example.demo.controller;

import com.alibaba.fastjson.JSONArray;
import com.example.demo.bean.trivialBeans.Classroom;
import com.example.demo.bean.trivialBeans.Classtime;
import com.example.demo.mapper.straightMappers.ClassroomMapper;
import com.example.demo.mapper.straightMappers.ClasstimeMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin("http://localhost:3000")
public class StraightController {

    /* 直属控制类，直接调用 Mapper 层，
    * 因为操作较简单没有继承抽象类
    * 后续可能随着要求变更而继承抽象类 */

    private final ClassroomMapper classroomMapper;
    private final ClasstimeMapper classtimeMapper;

    public StraightController(ClassroomMapper classroomMapper, ClasstimeMapper classtimeMapper) {
        this.classroomMapper = classroomMapper;
        this.classtimeMapper = classtimeMapper;
    }


    /***********教室操作************/

    /*增--初始化教室*/
    @RequestMapping(value="/classrooms", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> initializeClassrooms(@RequestHeader("Authentication") String authentication,
                                                                    @RequestBody JSONArray jsonArray) {
        Map<String, Object> map = new HashMap<>();

        String credit = ControllerOperation.checkAuthentication(authentication);
        if (credit.equals("IsAdmin")){
            List<Classroom> classroomList = jsonArray.toJavaList(Classroom.class);
            classroomMapper.saveAll(classroomList);
            return ControllerOperation.getConductResponse("Success", map);
        }
        else
            return ControllerOperation.getErrorResponse(credit, map);
    }

    /*改--修改单个教室状态*/
    @RequestMapping(value="/classroom/{name}", method = RequestMethod.PUT)
    public ResponseEntity<Map<String, Object>> setClassroomsStatus(@RequestHeader("Authentication") String authentication,
                                                                    @PathVariable("name") String name,
                                                                   @RequestBody Classroom classroom) {//paramater to be changed
        Map<String, Object> map = new HashMap<>();

        String credit = ControllerOperation.checkAuthentication(authentication);
        if (credit.equals("IsAdmin")){
            if(classroomMapper.findByName(name) != null){
                classroom.setName(name);
                classroomMapper.save(classroom);
                return ControllerOperation.getConductResponse("Success", map);

            }
            else
                return ControllerOperation.getConductResponse("NotFound", map);

        }
        else
            return ControllerOperation.getErrorResponse(credit, map);
    }

    /*查--获取所有教室名*/
    @RequestMapping(value="/classrooms", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getAllClassroomName(@RequestHeader("Authentication") String authentication) {
        Map<String, Object> map = new HashMap<>();

        String credit = ControllerOperation.checkAuthentication(authentication);
        if (credit.equals("IsAdmin")){

            List<Classroom> classroomList = classroomMapper.findAll();
            List<String> classroomNameList = classroomList.stream().map(Classroom::getName).toList();

            map.put("names", classroomNameList);
            return ControllerOperation.getConductResponse("Success", map);
        }
        else
            return ControllerOperation.getErrorResponse(credit, map);
    }

    /*查--获取单个教室*/
    @RequestMapping(value="/classroom/{name}", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getAClassroom(@RequestHeader("Authentication") String authentication,
                                                             @PathVariable("name") String name) {
        Map<String, Object> map = new HashMap<>();

        String credit = ControllerOperation.checkAuthentication(authentication);
        if (credit.equals("IsAdmin")){

            Classroom classroom = classroomMapper.findByName(name);
            return ControllerOperation.getSearchResponse(classroom, map);
        }
        else
            return ControllerOperation.getErrorResponse(credit, map);
    }

    /*删--删除所有教室*/
    @RequestMapping(value="/classrooms", method = RequestMethod.DELETE)
    public ResponseEntity<Map<String, Object>> delClassrooms(@RequestHeader("Authentication") String authentication) {
        Map<String, Object> map = new HashMap<>();

        String credit = ControllerOperation.checkAuthentication(authentication);
        if (credit.equals("IsAdmin")){

            classroomMapper.deleteAll();

            return ControllerOperation.getConductResponse("Success", map);
        }
        else
            return ControllerOperation.getErrorResponse(credit, map);
    }


    /***********上课时间操作************/

    /*增--初始化上课时间*/
    @RequestMapping(value="/classtimes", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> initializeClasstimes(@RequestHeader("Authentication") String authentication,
                                                                    @RequestBody JSONArray jsonArray) {//paramater to be changed
        Map<String, Object> map = new HashMap<>();

        String credit = ControllerOperation.checkAuthentication(authentication);
        if (credit.equals("IsAdmin")){

            List<Classtime> classtimeList = jsonArray.toJavaList(Classtime.class);

            classtimeMapper.saveAll(classtimeList);

            return ControllerOperation.getConductResponse("Success", map);
        }
        else
            return ControllerOperation.getErrorResponse(credit, map);
    }

    /*查--获取所有上课时间对象*/
    @RequestMapping(value="/classtime/{name}", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getAClasstime(@RequestHeader("Authentication") String authentication,
                                                             @PathVariable("name") String name) {
        Map<String, Object> map = new HashMap<>();

        String credit = ControllerOperation.checkAuthentication(authentication);
        if (credit.equals("IsAdmin")){

            Classtime classtime = classtimeMapper.findByName(name);

            return ControllerOperation.getSearchResponse(classtime, map);
        }
        else
            return ControllerOperation.getErrorResponse(credit, map);
    }

    /*查--获取所有上课时间段*/
    @RequestMapping(value="/classtimes", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getClasstimes(@RequestHeader("Authentication") String authentication) {
        Map<String, Object> map = new HashMap<>();

        String credit = ControllerOperation.checkAuthentication(authentication);
        if (credit.equals("IsAdmin")){
            List<Classtime> a = classtimeMapper.findAll();

            List<Classtime> classtimeList = classtimeMapper.findAll();
            List<String> classtimeNameList = classtimeList.stream().map(Classtime::getName).toList();

            map.put("names", classtimeNameList);
            return ControllerOperation.getConductResponse("Success", map);
        }
        else
            return ControllerOperation.getErrorResponse(credit, map);
    }

    /*删--删除所有上课时间对象*/
    @RequestMapping(value="/classtimes", method = RequestMethod.DELETE)
    public ResponseEntity<Map<String, Object>> delClasstimes(@RequestHeader("Authentication") String authentication) {
        Map<String, Object> map = new HashMap<>();

        String credit = ControllerOperation.checkAuthentication(authentication);
        if (credit.equals("IsAdmin")){

            classtimeMapper.deleteAll();

            return ControllerOperation.getConductResponse("Success", map);
        }
        else
            return ControllerOperation.getErrorResponse(credit, map);
    }


}
