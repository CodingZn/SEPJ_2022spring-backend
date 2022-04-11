package com.example.demo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.example.demo.bean.JWTUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.swing.text.html.parser.Entity;
import java.util.Map;
import java.util.Objects;

public class ControllerOperation {
    public static String checkAuthentication(String authentication){
        String token = authentication.substring(7);//截取掉“Bearer ”
        String usertype = JWTUtils.decodeToGetValue(token, "usertype");

        if (usertype == null){//token无效情况
            System.out.println("Token is invalid.");
            return "InvalidTokenError";
        }
        if (Objects.equals(usertype, "admin")){
            System.out.println("Is admin.");
            return "IsAdmin";
        }
        else if (Objects.equals(usertype, "teacher")){
            System.out.println("Is teacher.");
            return "IsTeacher";
        }
        else if (Objects.equals(usertype, "student")){
            System.out.println("Is student.");
            return "IsStudent";
        }
        else{
            System.out.println("Request is not from user");
            return "NotUser";
        }
    }

    public static ResponseEntity<Map<String, Object>> getErrorResponse(String result, Map<String, Object> map){
        if (result.equals("InvalidTokenError")){//token无效情况
            System.out.println("Token is invalid.");
            map.put("message","用户身份无效！");
            return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
        }
        if (result.equals("NotAdmin")){//not admin
            System.out.println("Request is not from administrator");
            map.put("message","无权限操作！");
            return new ResponseEntity<>(map, HttpStatus.FORBIDDEN);
        }
        else{
            System.out.println("Unknown Error");
            map.put("message","未知错误！");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
    }

    public static ResponseEntity<Map<String, Object>> getConductResponse(String result, Map<String, Object> map){
        switch (result) {
            case "Success" -> {
                System.out.println("Successfully conducted!");
                map.put("message", "操作成功！");
                return new ResponseEntity<>(map, HttpStatus.OK);
            }
            case "FormError" -> {
                System.out.println("FormError!");
                map.put("message", "输入信息格式错误！");
                return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
            }
            case "DependError" -> {
                System.out.println("DependError!");
                map.put("message", "输入信息含有不存在的依赖属性！");
                return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
            }
            case "NotFound" -> {
                System.out.println("Entry not found!");
                map.put("message", "无对应信息！");
                return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
            }
            case "Conflict" -> {
                System.out.println("Entry Conflict!");
                map.put("message", "信息条目冲突！");
                return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
            }
            default -> {
                System.out.println("Unknown Error");
                map.put("message", "未知错误！");
                return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
            }
        }
    }

    public static <T> ResponseEntity<Map<String, Object>> getSearchResponse(T entity, Map<String, Object> map){
        //实体不为null即为查找成功
        if (entity != null) {
            System.out.println("Successfully found!");
            map.put("message", "查找成功！");
            map.putAll(JSON.parseObject(JSON.toJSONString(entity), new TypeReference<>() {}));
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
        else{
            System.out.println("Entity not found!");
            map.put("message", "查找内容不存在！");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }

    }

}
