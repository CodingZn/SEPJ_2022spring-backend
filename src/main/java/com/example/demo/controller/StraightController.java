package com.example.demo.controller;

import com.example.demo.bean.trivialBeans.Classroom;
import com.example.demo.mapper.straightMappers.ClassroomMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("http://localhost:3000")
public class StraightController {

    private final ClassroomMapper straightMapper;

    public StraightController(ClassroomMapper classroomMapper) {
        this.straightMapper = classroomMapper;
    }

    @RequestMapping(value="/classrooms", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> initializeClassrooms(@RequestHeader("Authentication") String authentication,
                                                                    @RequestBody List<Classroom> classroomList) {//paramater to be changed
        Map<String, Object> map = new HashMap<>();

        String credit = ControllerOperation.checkAuthentication(authentication);
        if (credit.equals("IsAdmin")){
            /*
            * 在此增加相关代码，将形参中classroomList中所有的classroom对象写入到数据库中
            * */

            return ControllerOperation.getConductResponse("Success", map);
        }
        else
            return ControllerOperation.getErrorResponse(credit, map);
    }


}
