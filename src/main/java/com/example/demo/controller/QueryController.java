package com.example.demo.controller;

import com.example.demo.bean.Lesson;
import com.example.demo.bean.User;
import com.example.demo.bean.specialBean.LessonQuery;
import com.example.demo.service.LessonQueryService;
import com.example.demo.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.example.demo.utils.JWTUtils.*;
import static com.example.demo.utils.JWTUtils.StudentAuthority;

@RestController
@CrossOrigin("http://localhost:3000")
public class QueryController {
    private final LessonQueryService queryService;

    @Autowired
    public QueryController(LessonQueryService queryService) {
        this.queryService = queryService;
    }

    //查询搜索
    @RequestMapping(value = "/lessonquery", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> queryLessons(@RequestHeader("Authentication") String authentication,
                                                            @RequestBody LessonQuery lessonQuery){
        Map<String, Object> map = new HashMap<>();

        String credit = ControllerOperation.checkAuthentication(authentication);
        String authority = ControllerOperation.getAuthority(authentication);
        String userid = JWTUtils.decodeToGetValue(authentication.substring(7), "userid");

        if (credit.equals(ValidJWTToken)){
            String result;
            switch (authority) {
                case AdminAuthority -> {
                    LessonQuery lessonQuery_processed = queryService.processAQuery(lessonQuery);

                    map.put("lessonquery", lessonQuery_processed);
                    result = "Success";
                }
                case TeacherAuthority -> {
                    LessonQuery lessonQuery_processed = queryService.processAQuery(lessonQuery);
                    List<Lesson> lessonList = lessonQuery_processed.getResult();
                    lessonList.removeIf(u -> !u.getTeacher().stream().map(User::getUserid).toList().contains(userid)
                            && !Objects.equals(u.getStatus(), Lesson.Status.censored));
                    lessonQuery_processed.setResult(lessonList);
                    map.put("lessonquery", lessonQuery_processed);
                    result = "Success";
                }
                case StudentAuthority -> {
                    LessonQuery lessonQuery_processed = queryService.processAQuery(lessonQuery);
                    List<Lesson> lessonList = lessonQuery_processed.getResult();
                    lessonList.removeIf(u -> !Objects.equals(u.getStatus(), Lesson.Status.censored));
                    map.put("lessonquery", lessonQuery_processed);
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

}
