package com.example.demo.controller;

import com.example.demo.bean.specialBean.LessonQuery;
import com.example.demo.service.LessonQueryService;
import com.example.demo.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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
                case AdminAuthority, TeacherAuthority, StudentAuthority -> {
                    LessonQuery lessonQuery_processed = queryService.processAQuery(lessonQuery);
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
