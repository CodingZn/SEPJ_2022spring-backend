package com.example.demo.controller;

import com.example.demo.bean.Lesson;
import com.example.demo.bean.User;
import com.example.demo.service.LessonConductService;
import com.example.demo.utils.JWTUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.example.demo.utils.JWTUtils.*;

@RestController
@CrossOrigin("http://121.37.100.255:3000")
public class LessonConductController {
    private final LessonConductService lessonConductService;

    public LessonConductController(LessonConductService lessonConductService) {
        this.lessonConductService = lessonConductService;
    }

    //学生选课
    @RequestMapping(value = "/user/{userid}/lessonsTaking", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> selectALesson(@RequestHeader("Authentication") String authentication,
                                                             @PathVariable("userid") String userid_url,
                                                             @RequestBody Lesson lesson){
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
                    }
                    else {
                        String lessonid = String.valueOf(lesson.getLessonid());
                        String message = lessonConductService.selectALesson(userid, lessonid);
                        map.put("message", message);
                        result = "Message";
                    }
                }
                default ->{
                    result="NoAuth";
                }
            }
            return ControllerOperation.getConductResponse(result, map);
        }
        else return ControllerOperation.getErrorResponse(credit, map);
    }

    //学生退课
    @RequestMapping(value = "/user/{userid}/lessonsTaking/{lessonid}", method = RequestMethod.DELETE)
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

    //学生查看所有已修课程
    @RequestMapping(value = "/user/{userid}/lessonsTaken", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getAllLessonsTaken(@RequestHeader("Authentication") String authentication,
                                                                  @PathVariable("userid") String userid_url){
        Map<String, Object> map = new HashMap<>();

        String credit = ControllerOperation.checkAuthentication(authentication);
        String authority = ControllerOperation.getAuthority(authentication);
        String userid = JWTUtils.decodeToGetValue(authentication.substring(7), "userid");

        if (credit.equals(ValidJWTToken)){
            String result;
            switch (authority) {
                case StudentAuthority-> {
                    if(!Objects.equals(userid, userid_url)){
                        result="NoAuth";
                        return ControllerOperation.getConductResponse(result, map);
                    }
                    List<Lesson> lessonList = lessonConductService.getAllLessonsTaken(userid);
                    map.put("lessonsTaken", lessonList);
                    result = "Success";
                }
                default ->{
                    result="NoAuth";
                }
            }
            return ControllerOperation.getConductResponse(result, map);
        }
        else return ControllerOperation.getErrorResponse(credit, map);
    }

    //学生和老师 查看所有已选、正在上的课程
    @RequestMapping(value = "/user/{userid}/lessonsTaking", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getAllLessonsTaking(@RequestHeader("Authentication") String authentication,
                                                                  @PathVariable("userid") String userid_url){
        Map<String, Object> map = new HashMap<>();

        String credit = ControllerOperation.checkAuthentication(authentication);
        String authority = ControllerOperation.getAuthority(authentication);
        String userid = JWTUtils.decodeToGetValue(authentication.substring(7), "userid");

        if (credit.equals(ValidJWTToken)){
            String result;
            switch (authority) {
                case StudentAuthority, TeacherAuthority-> {
                    if(!Objects.equals(userid, userid_url)){
                        result="NoAuth";
                        return ControllerOperation.getConductResponse(result, map);
                    }
                    List<Lesson> lessonList = lessonConductService.getAllLessonsTaking(userid);
                    map.put("lessonsTaking", lessonList);
                    result = "Success";
                }
                default ->{
                    result="NoAuth";
                }
            }
            return ControllerOperation.getConductResponse(result, map);
        }
        else return ControllerOperation.getErrorResponse(credit, map);
    }

    //管理员和老师查看某门课的选课名单
    @RequestMapping(value = "/lesson/{lessonid}/classmates", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getAllStudentsTakingLesson(@RequestHeader("Authentication") String authentication,
                                                                   @PathVariable("lessonid") String lessonid){
        Map<String, Object> map = new HashMap<>();

        String credit = ControllerOperation.checkAuthentication(authentication);
        String authority = ControllerOperation.getAuthority(authentication);
        String userid = JWTUtils.decodeToGetValue(authentication.substring(7), "userid");

        if (credit.equals(ValidJWTToken)){
            String result;
            switch (authority) {
                case AdminAuthority -> {
                    List<String> userList = lessonConductService.getAllStudentIdsTakingLesson(lessonid);
                    map.put("classmates", userList);
                    result = "Success";
                }
                case TeacherAuthority -> {
                    List<String> userList = lessonConductService.getAllStudentIdsTakingLesson(lessonid, userid);
                    if (userList == null){
                        result = "NoAuth";
                    }
                    else{
                        map.put("classmates", userList);
                        result = "Success";
                    }
                }
                default ->{
                    result="NoAuth";
                }
            }
            return ControllerOperation.getConductResponse(result, map);
        }
        else return ControllerOperation.getErrorResponse(credit, map);
    }

    //管理员一轮后踢人
    @RequestMapping(value = "/roundFinishRequest/1", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> kickOffAllExceededStudent(@RequestHeader("Authentication") String authentication){
        Map<String, Object> map = new HashMap<>();

        String credit = ControllerOperation.checkAuthentication(authentication);
        String authority = ControllerOperation.getAuthority(authentication);
        String userid = JWTUtils.decodeToGetValue(authentication.substring(7), "userid");

        if (credit.equals(ValidJWTToken)){
            String result;
            switch (authority) {
                case AdminAuthority -> {
                    result = "Success";
                    lessonConductService.kickAllExceededClassmates();
                }
                default ->{
                    result="NoAuth";
                }
            }
            return ControllerOperation.getConductResponse(result, map);
        }
        else return ControllerOperation.getErrorResponse(credit, map);
    }

    //老师pass学生们
    @RequestMapping(value = "lesson/{lessonid}/pass", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> passStudents(@RequestHeader("Authentication") String authentication,
                                                            @PathVariable("lessonid") String lessonid,
                                                            @RequestBody List<String> studentids){
        Map<String, Object> map = new HashMap<>();

        String credit = ControllerOperation.checkAuthentication(authentication);
        String authority = ControllerOperation.getAuthority(authentication);
        String userid = JWTUtils.decodeToGetValue(authentication.substring(7), "userid");

        if (credit.equals(ValidJWTToken)){
            String result;
            switch (authority) {
                case TeacherAuthority -> {
                    result = lessonConductService.passStudents(lessonid, userid, studentids);
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
