package com.example.demo.controller;

import com.example.demo.bean.trivialBeans.Classroom;
import com.example.demo.bean.trivialBeans.Classtime;
import com.example.demo.bean.trivialBeans.Ultimatecontrol;
import com.example.demo.mapper.straightMappers.ClassroomMapper;
import com.example.demo.mapper.straightMappers.ClasstimeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("http://localhost:3000")
public class StraightController {


    private final ClassroomMapper classroomMapper;
    private final ClasstimeMapper classtimeMapper;

    private final Ultimatecontrol ultimatecontrol;

    public StraightController(ClassroomMapper classroomMapper, ClasstimeMapper classtimeMapper, Ultimatecontrol ultimatecontrol) {
        this.classroomMapper = classroomMapper;
        this.classtimeMapper = classtimeMapper;
        this.ultimatecontrol = ultimatecontrol;
    }

    @RequestMapping(value="/classrooms", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> initializeClassrooms(@RequestHeader("Authentication") String authentication,
                                                                    @RequestBody List<Classroom> classroomList) {//paramater to be changed
        Map<String, Object> map = new HashMap<>();

        String credit = ControllerOperation.checkAuthentication(authentication);
        if (credit.equals("IsAdmin")){
            classroomMapper.saveAll(classroomList);

            return ControllerOperation.getConductResponse("Success", map);
        }
        else
            return ControllerOperation.getErrorResponse(credit, map);
    }

    @RequestMapping(value="/classrooms/{name}", method = RequestMethod.PUT)
    public ResponseEntity<Map<String, Object>> setClassroomsStatus(@RequestHeader("Authentication") String authentication,
                                                                    @RequestParam("name") String name,
                                                                   @RequestBody Classroom classroom) {//paramater to be changed
        Map<String, Object> map = new HashMap<>();

        String credit = ControllerOperation.checkAuthentication(authentication);
        if (credit.equals("IsAdmin")){

            classroom.setName(name);
            classroomMapper.save(classroom);

            return ControllerOperation.getConductResponse("Success", map);
        }
        else
            return ControllerOperation.getErrorResponse(credit, map);
    }

    @RequestMapping(value="/classrooms/{name}", method = RequestMethod.GET)
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

    @RequestMapping(value="/classrooms/{name}", method = RequestMethod.DELETE)
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
                                                                    @RequestBody List<Classtime> classtimeList) {//paramater to be changed
        Map<String, Object> map = new HashMap<>();

        String credit = ControllerOperation.checkAuthentication(authentication);
        if (credit.equals("IsAdmin")){

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


    /***********************/

    @RequestMapping(value="/controls/{name}", method = RequestMethod.PATCH)
    public ResponseEntity<Map<String, Object>> changeControl (@RequestHeader("Authentication") String authentication,
                                                              @RequestParam("name") String name,
                                                              @RequestBody Ultimatecontrol control) {
        Map<String, Object> map = new HashMap<>();

        String credit = ControllerOperation.checkAuthentication(authentication);
        if (credit.equals("IsAdmin")){

            /*
             * 在此增加相关代码，将形参中 control 的 name 属性设置为参数中的 name
             * 然后将 control 通过 mapper 层里的 save 方法写入到数据库中
             * */



            return ControllerOperation.getConductResponse("Success", map);
        }
        else
            return ControllerOperation.getErrorResponse(credit, map);
    }
}
