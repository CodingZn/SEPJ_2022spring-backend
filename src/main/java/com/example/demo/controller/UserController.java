package com.example.demo.controller;

import com.example.demo.bean.BeanTools;
import com.example.demo.bean.JWTUtils;
import com.example.demo.bean.UserBean;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.util.*;


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

    /*查--登录操作*/
    @RequestMapping(value="/token", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> login(@RequestBody UserBean user) {
        String schoolnumber, password;

        schoolnumber = user.getSchoolnumber();
        password = user.getPassword();

        Map<String, Object> map = new HashMap<>();

        UserBean userBean=userService.login(schoolnumber, password);

        if(userBean != null && userBean.getStatus().equals("enabled")){//success
            String token;
            Map<String, String> payload = new HashMap<>();//自定义payload
            payload.put("schoolnumber",userBean.getSchoolnumber());//学号
            payload.put("name",userBean.getName());//姓名
            payload.put("usertype",userBean.getUsertype());//用户角色
            if (Objects.equals(password, "fDu666666"))
                payload.put("initialUser","true");

            token = JWTUtils.getToken(payload);

            map.put("jwt",token);

            //返回Map名值对--json形式
            System.out.println("Successfully login! Token is sent out.");
            map.put("message", "Successfully login!");
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
        else {//failure

            map.put("message","Login failed due to wrong schoolnumber or password.");
            System.out.println("Login failed due to wrong schoolnumber or password");
            return new ResponseEntity<>(map, HttpStatus.FORBIDDEN);
        }

    }

    /*查--返回所有schoolnumber*/
    @RequestMapping(value="/users", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getAllSchoolnumbers(@RequestHeader(value="Authentication") String authentication){
        Map<String, Object> map = new HashMap<>();

        String credit = ControllerOperation.checkAuthentication(authentication);

        if (credit.equals("admin")){//必然成功，未重构
            List<String> strings = userService.getAllSchoolnumbers();
            System.out.println("strings="+strings);
            map.put("schoolnumbers",strings);//change
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
        else{
            return ControllerOperation.getErrorResponse(credit, map);
        }

    }

    /*查--获取一个用户信息*/
    @RequestMapping(value="/user/{schoolnumber}", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getAUser(@PathVariable("schoolnumber") String schoolnumber,
                                                        @RequestHeader(value="Authentication") String authentication) {
        Map<String, Object> map = new HashMap<>();

        String credit = ControllerOperation.checkAuthentication(authentication);

        if (credit.equals("admin")){
            UserBean userBean;
            userBean = userService.getAUser(schoolnumber);

            return ControllerOperation.getSearchResponse(userBean, map);
        }
        else{
            return ControllerOperation.getErrorResponse(credit, map);
        }
    }

    /*增--注册新用户*/
    @RequestMapping(value="/user/{schoolnumber}", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> createAUser(@PathVariable("schoolnumber") String schoolnumber,
                                                           @RequestBody UserBean regisuser,
                                                           @RequestHeader(value = "Authentication") String authentication){

        Map<String, Object> map = new HashMap<>();

        UserBean userBean;
        regisuser.setSchoolnumber(schoolnumber);//默认使用url提供的学工号
        userBean = regisuser;

        String credit = ControllerOperation.checkAuthentication(authentication);

        if (credit.equals("admin")){
            System.out.println(userBean);
            String result = userService.register(userBean);
            System.out.println(result);

            switch (result){
                case "SchoolnumberConflict"://学工号冲突
                    System.out.println("Schoolnumber Conflict.");
                    map.put("message","Schoolnumber Conflict.");
                    return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
                case "IdentitynumberConflict"://身份证号冲突
                    System.out.println("Identitynumber Conflict.");
                    map.put("message","Identitynumber Conflict.");
                    return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);

                default:
                    return ControllerOperation.getConductResponse(result, map);
            }
        }
        else{
            return ControllerOperation.getErrorResponse(credit, map);
        }

    }
    /*改--重写一个用户,put*/
    @RequestMapping(value="/user/{schoolnumber}", method = RequestMethod.PUT)
    public ResponseEntity<Map<String, Object>> rewriteAUser(@PathVariable("schoolnumber") String schoolnumber,
                                                            @RequestBody UserBean userBean,
                                                            @RequestHeader(value="Authentication") String authentication) {
        Map<String, Object> map = new HashMap<>();

        String credit = ControllerOperation.checkAuthentication(authentication);

        if (credit.equals("admin")){
            userBean.setSchoolnumber(schoolnumber);
            String result = userService.rewriteUser(userBean);

            return ControllerOperation.getConductResponse(result, map);

        }
        else{
            return ControllerOperation.getErrorResponse(credit, map);
        }


    }

    /*改--修改一个用户,patch*/
    @RequestMapping(value="/user/{schoolnumber}", method = RequestMethod.PATCH)
    public ResponseEntity<Map<String, Object>> changeAUser(@PathVariable("schoolnumber") String schoolnumber,
                                                           @RequestBody UserBean userBean,
                                                           @RequestHeader(value="Authentication") String authentication) {
        Map<String, Object> map = new HashMap<>();
        String schoolnumber_jwt = JWTUtils.decodeToGetValue(authentication.substring(7), "schoolnumber");

        String credit = ControllerOperation.checkAuthentication(authentication);

        if (credit.equals("admin") || Objects.equals(schoolnumber_jwt, schoolnumber)){
            UserBean userBean_ori = userService.getAUser(schoolnumber);

            String [] adminauth = {"email", "password", "phonenumber", "name", "school", "major", "status"};
            String [] standard = {"email", "password", "phonenumber"};
            List<String> changeableList;

            //身份不同，权限（可修改的项）不同
            if (credit.equals("admin"))
                changeableList = new ArrayList<>(Arrays.asList(adminauth));
            else
                changeableList = new ArrayList<>(Arrays.asList(standard));

            UserBean userBean_modified = BeanTools.modify(userBean_ori, userBean, changeableList);

            String result = userService.rewriteUser(userBean_modified);
            return ControllerOperation.getConductResponse(result, map);
        }
        else{
            return ControllerOperation.getErrorResponse(credit, map);
        }

    }


    /*删--删除用户*/
    @RequestMapping(value="/user/{schoolnumber}", method = RequestMethod.DELETE)
    public ResponseEntity<Map<String, Object>> delMajor(@PathVariable("schoolnumber") String schoolnumber,
                                                        @RequestHeader(value="Authentication") String authentication){
        Map<String, Object> map = new HashMap<>();

        String credit = ControllerOperation.checkAuthentication(authentication);

        if (credit.equals("admin")){
            String result = this.userService.deleteUser(schoolnumber);
            return ControllerOperation.getConductResponse(result, map);
        }
        else{
            return ControllerOperation.getErrorResponse(credit, map);
        }

    }


}
