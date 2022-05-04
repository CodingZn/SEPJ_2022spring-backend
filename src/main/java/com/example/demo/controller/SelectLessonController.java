package com.example.demo.controller;

import com.example.demo.service.LessonConductService;
import com.example.demo.utils.JWTUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.example.demo.utils.JWTUtils.*;

@RestController
@CrossOrigin("http://localhost:3000")
public class SelectLessonController {
    private final LessonConductService lessonConductService;

    public SelectLessonController(LessonConductService lessonConductService) {
        this.lessonConductService = lessonConductService;
    }

    //选课
    @RequestMapping(value = "user/{userid}/lessonsTaking/{lessonid}", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> selectALesson(@RequestHeader("Authentication") String authentication,
                                                             @PathVariable("userid") String userid_url,
                                                             @PathVariable("lessonid") String lessonid){
        Map<String, Object> map = new HashMap<>();

        String credit = ControllerOperation.checkAuthentication(authentication);
        String authority = ControllerOperation.getAuthority(authentication);
        String userid = JWTUtils.decodeToGetValue(authentication.substring(7), "userid");

        if (credit.equals(ValidJWTToken)){
            String result;
            switch (authority) {
                case StudentAuthority -> {
                    if(!Objects.equals(userid, userid_url)){
                        result="NoAuth";
                        return ControllerOperation.getConductResponse(result, map);
                    }
                    result = lessonConductService.selectALesson(userid, lessonid);
                    return ControllerOperation.getMessageResponse(result, map);
                }
                default ->{
                    result="NoAuth";
                }
            }
            return ControllerOperation.getConductResponse(result, map);
        }
        else return ControllerOperation.getErrorResponse(credit, map);
    }

    //退课
    @RequestMapping(value = "user/{userid}/lessonsTaking/{lessonid}", method = RequestMethod.DELETE)
    public ResponseEntity<Map<String, Object>> quitALesson(@RequestHeader("Authentication") String authentication,
                                                           @PathVariable("userid") String userid_url,
                                                           @PathVariable("lessonid") String lessonid){
        Map<String, Object> map = new HashMap<>();

        String credit = ControllerOperation.checkAuthentication(authentication);
        String authority = ControllerOperation.getAuthority(authentication);
        String userid = JWTUtils.decodeToGetValue(authentication.substring(7), "userid");

        if (credit.equals(ValidJWTToken)){
            String result;
            switch (authority) {
                case StudentAuthority -> {
                    if(!Objects.equals(userid, userid_url)){
                        result="NoAuth";
                        return ControllerOperation.getConductResponse(result, map);
                    }
                    result = lessonConductService.quitALesson(userid, lessonid);
                }
                default ->{
                    result="NoAuth";
                }
            }
            return ControllerOperation.getConductResponse(result, map);
        }
        else return ControllerOperation.getErrorResponse(credit, map);
    }
}
