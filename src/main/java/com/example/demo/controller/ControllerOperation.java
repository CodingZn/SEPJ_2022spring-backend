package com.example.demo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.example.demo.bean.JWTUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.swing.text.html.parser.Entity;
import java.util.Map;
import java.util.Objects;

import static com.example.demo.bean.JWTUtils.*;

public class ControllerOperation {

    /*这个类封装了一些 Controller 层中的通用操作，以静态方法进行调用*/

    //返回令牌是否有效
    public static String checkAuthentication(String authentication){
        String token = authentication.substring(7);//截取掉请求头中的 “Bearer ”
        String usertype = JWTUtils.decodeToGetValue(token, "usertype");

        if (usertype == null){//token无效情况
            System.out.println("Token is invalid.");
            return InvalidJWTToken;
        }
        else{
            System.out.println("Token is valid.");
            return ValidJWTToken;
        }

    }

    //返回令牌对应的用户权限
    public static String getAuthority(String authentication){
        String token = authentication.substring(7);//截取掉请求头中的 “Bearer ”
        String usertype = JWTUtils.decodeToGetValue(token, "usertype");

        if (Objects.equals(usertype, "admin")){
            System.out.println("Is admin.");
            return AdminAuthority;
        }
        else if (Objects.equals(usertype, "teacher")){
            System.out.println("Is teacher.");
            return TeacherAuthority;
        }
        else if (Objects.equals(usertype, "student")){
            System.out.println("Is student.");
            return StudentAuthority;
        }
        else
            System.out.println("No authority.");
            return NoAuthority;
    }

    //令牌权限与用户发起的请求不符时的顶层返回函数
    public static ResponseEntity<Map<String, Object>> getErrorResponse(String result, Map<String, Object> map){
        if (result.equals(IncorrectJWTToken)){//token无效情况
            System.out.println("Token is invalid.");
            map.put("message","用户身份无效！");
            return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
        }
        else {
            System.out.println("Token is incorrect.");
            map.put("message","JWT令牌错误！");
            return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
        }
    }

    //执行操作后的顶层返回函数，根据 Service 层函数返回值控制路径
    public static ResponseEntity<Map<String, Object>> getConductResponse(String result, Map<String, Object> map){
        switch (result) {
            case "Success" -> {
                System.out.println("Successfully conducted!");
                map.put("message", "操作成功！");
                return new ResponseEntity<>(map, HttpStatus.OK);
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
            default -> {
                System.out.println("Unknown Error");
                map.put("message", "未知错误！");
                return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
            }
        }
    }

//    //执行查找操作后的顶层返回函数
//    public static <T> ResponseEntity<Map<String, Object>> getSearchResponse(String result, Map<String, Object> map){
//        //实体不为null即为查找成功
//        if (result.equals("Success")) {
//            System.out.println("Successfully found!");
//            map.put("message", "查找成功！");
////            map.putAll(JSON.parseObject(JSON.toJSONString(entity), new TypeReference<>() {}));
//            return new ResponseEntity<>(map, HttpStatus.OK);
//        }
//        else if (result.equals("NotFound")){
//            System.out.println("Entity not found!");
//            map.put("message", "查找内容不存在！");
//            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
//        }
//        else{
//            System.out.println("Unknown Error!");
//            map.put("message", "未知错误！");
//            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
//        }
//
//    }

}
