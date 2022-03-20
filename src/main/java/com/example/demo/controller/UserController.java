package com.example.demo.controller;

import com.auth0.jwt.interfaces.DecodedJWT;

import com.example.demo.bean.JWTUtils;
import com.example.demo.bean.UserBean;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


//Control层，与前端联系
@RestController
@CrossOrigin("http://localhost:3000")
public class UserController {
    private final UserService userService;

    @Autowired//将Service层注入到Controller层
    public UserController(UserService userService){
        this.userService = userService;
        userService.createAdmin();
    }

    /*登录操作*/
    @RequestMapping(value="/token", method = RequestMethod.POST)
    public ResponseEntity<Map<String, String>> login(@RequestBody UserBean user) throws JsonProcessingException {
        String schoolnumber, password;

        schoolnumber = user.getSchoolnumber();
        password = user.getPassword();

        Map<String, String> map = new HashMap<>();

        //通过学工号和密码查询实现登录
        UserBean userBean=userService.login(schoolnumber, password);

        if(userBean != null){//success
            String token;
            Map<String, String> payload = new HashMap<>();//自定义payload
            payload.put("schoolnumber",userBean.getSchoolnumber());//学号
            payload.put("name",userBean.getName());//姓名
            payload.put("usertype",userBean.getUsertype());//用户角色

            token = JWTUtils.getToken(payload);

            map.put("jwt",token);

            //返回Map名值对--json形式
            System.out.println("Successfully login! Token is sent out.");
            map.put("message", "Successfully login!");
            return new ResponseEntity<Map<String, String>>(map, HttpStatus.OK);
        }
        else {//failure

            map.put("message","Login failed due to wrong schoolnumber or password.");
            System.out.println("Login failed due to wrong schoolnumber or password");
            return new ResponseEntity<Map<String, String>>(map, HttpStatus.FORBIDDEN);
        }

    }

    /*注册新用户*/
    @RequestMapping(value="/user/{schoolnumber}", method = RequestMethod.POST)
    public ResponseEntity<Map<String, String>> register(@PathVariable("schoolnumber") String schoolnumber,
                                           @RequestBody UserBean regisuser,
                                           @RequestHeader(value = "Authentication") String authentication){

        UserBean userBean;
        regisuser.setSchoolnumber(schoolnumber);//默认使用url提供的学工号
        userBean = regisuser;

        Map<String, String> map = new HashMap<>();

        String str;
        String usertype;

        System.out.println("Authentication: "+authentication);

        String token = authentication.substring(7);//截取掉“Bearer ”

        try{
            DecodedJWT verify = JWTUtils.verify(token);
            //jwt验证成功
            //验证character
            usertype = verify.getClaim("usertype").asString();

        }catch (Exception e) {//无效token
            System.out.println("Token is invalid.");

            map.put("message","Token is invalid.");
            return new ResponseEntity<Map<String, String>>(map, HttpStatus.UNAUTHORIZED);
        }

        if (Objects.equals(usertype, "admin")){//验证为admin，成功进行注册操作
            System.out.println(userBean);
            str = userService.register(userBean);
            System.out.println(str);

            switch (str){
                case "Success":
                    System.out.println("Successfully registered!");
                    map.put("message","Successfully registered!");
                    return new ResponseEntity<Map<String, String>>(map, HttpStatus.OK);
                case "SchoolnumberConflict"://学工号冲突
                    System.out.println("Schoolnumber Conflict.");
                    map.put("message","Schoolnumber Conflict.");
                    return new ResponseEntity<Map<String, String>>(map ,HttpStatus.BAD_REQUEST);
                case "IdentitynumberConflict"://身份证号冲突
                    System.out.println("Identitynumber Conflict.");
                    map.put("message","Identitynumber Conflict.");
                    return new ResponseEntity<Map<String, String>>(map ,HttpStatus.BAD_REQUEST);
                case "FormError":
                    System.out.println("Data Form Error!");
                    map.put("message","Data Form Error!");
                    return new ResponseEntity<Map<String, String>>(map ,HttpStatus.BAD_REQUEST);
                default:
                    System.out.println("Unknown Error");
                    map.put("message","Unknown Error");
                    return new ResponseEntity<Map<String, String>>(map ,HttpStatus.BAD_REQUEST);
            }

        }
        else{//不是admin
            System.out.println("Request is not from administrator");
            map.put("message","Request is not from administrator");
            return new ResponseEntity<Map<String, String>>(map,HttpStatus.FORBIDDEN);
        }
    }

    /*修改密码*/
    @RequestMapping(value="/user/{schoolnumber}", method = RequestMethod.PATCH)
    public ResponseEntity<Map<String, String>> changePassword(@PathVariable("schoolnumber") String schoolnumber_url,
                                                 @RequestBody UserBean changeuser,
                                                 @RequestHeader(value = "Authentication") String authentication){
        String newpassword, schoolnumber;
        String str;
        newpassword = changeuser.getPassword();

        System.out.println("Authentication: "+authentication);

        Map<String, String> map = new HashMap<>();

        String token = authentication.substring(7);//截取掉“Bearer ”

        try{
            DecodedJWT verify = JWTUtils.verify(token);
            //jwt验证成功
            //通过jwt获取登录者的学工号
            schoolnumber = verify.getClaim("schoolnumber").asString();

            System.out.println("schoolnumber_url="+schoolnumber_url);
            System.out.println("schoolnumber="+schoolnumber);

            if (!Objects.equals(schoolnumber, schoolnumber_url)){//验证jwt中学工号与路径中学工号是否一致
                System.out.println("Request data don't match.");
                map.put("message","Request data don't match.");
                return new ResponseEntity<Map<String, String>>(map , HttpStatus.BAD_REQUEST);
            }


        }catch (Exception e) {//无效token
            System.out.println("Token is invalid.");
            map.put("message","Token is invalid.");
            return new ResponseEntity<Map<String, String>>(map , HttpStatus.BAD_REQUEST);
        }

        //通过搜索学工号重置密码
        str= userService.changePassword(schoolnumber, newpassword);

        switch (str){
            case "Success":
                System.out.println("Successfully changed password!");
                map.put("message","Successfully changed password!");
                return new ResponseEntity<Map<String, String>>(map ,HttpStatus.OK);
            case "NotAllowedPassword":
                System.out.println("New password is not allowed.");
                map.put("message","New password is not allowed.");
                return new ResponseEntity<Map<String, String>>(map ,HttpStatus.BAD_REQUEST);

            default:
                System.out.println("Unknown Error");
                map.put("message","Unknown Error");
                return new ResponseEntity<Map<String, String>>(map, HttpStatus.BAD_REQUEST);
        }
    }

}
