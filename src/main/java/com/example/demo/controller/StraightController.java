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


    private final ClassroomMapper classroomMapper;
    private final ClasstimeMapper classtimeMapper;

//    private final Ultimatectrl ultimatecontrol;

    public StraightController(ClassroomMapper classroomMapper, ClasstimeMapper classtimeMapper) {
        this.classroomMapper = classroomMapper;
        this.classtimeMapper = classtimeMapper;
//        this.ultimatecontrol = ultimatecontrol;
    }

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

    @RequestMapping(value="/classrooms", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getClassrooms(@RequestHeader("Authentication") String authentication) {
        Map<String, Object> map = new HashMap<>();

        String credit = ControllerOperation.checkAuthentication(authentication);
        if (credit.equals("IsAdmin")){

            List<Classroom> classroomList = classroomMapper.findAll();

            map.put("classrooms", classroomList);
            return ControllerOperation.getConductResponse("Success", map);
        }
        else
            return ControllerOperation.getErrorResponse(credit, map);
    }

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


    /***********************/

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

    @RequestMapping(value="/classtimes", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getClasstimes(@RequestHeader("Authentication") String authentication) {
        Map<String, Object> map = new HashMap<>();

        String credit = ControllerOperation.checkAuthentication(authentication);
        if (credit.equals("IsAdmin")){
            List<Classtime> a = classtimeMapper.findAll();

            map.put("classrooms", a);
            return ControllerOperation.getConductResponse("Success", map);
        }
        else
            return ControllerOperation.getErrorResponse(credit, map);
    }

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
