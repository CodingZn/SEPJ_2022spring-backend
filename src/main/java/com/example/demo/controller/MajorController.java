package com.example.demo.controller;

import com.example.demo.bean.JWTUtils;
import com.example.demo.bean.Major;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin("http://localhost:3000")
public class MajorController {
    private final UserService userService;

    @Autowired//将Service层注入到Controller层
    public MajorController(UserService userService){
        this.userService = userService;
//        userService.createMajortest();
        userService.getAllMajornumbers();
    }

    /*获取新majornumber*/
    @RequestMapping(value="/majornumber", method = RequestMethod.GET)
    public ResponseEntity<Map<String, String>> getANewMajornumber(@RequestHeader(value="Authentication") String authentication){
        Map<String,String> map = new HashMap<>();

        /*管理员权限*/
        String token = authentication.substring(7);//截取掉“Bearer ”

        String usertype = JWTUtils.decodeToGetValue(token, "usertype");
        if (usertype == null){//token无效情况
            System.out.println("Token is invalid.");
            map.put("message","Token is invalid.");
            return new ResponseEntity<Map<String, String>>(map, HttpStatus.UNAUTHORIZED);
        }
        if (Objects.equals(usertype, "admin")){
            String newMajornumber = userService.getANewMajornumber();
            map.put("majornumber", newMajornumber);
            return new ResponseEntity<Map<String, String>>(map, HttpStatus.OK);
        }
        else{
            System.out.println("Request is not from administrator");
            map.put("message","Request is not from administrator");
            return new ResponseEntity<Map<String, String>>(map,HttpStatus.FORBIDDEN);
        }

    }

    /*新增专业*/
    @RequestMapping(value="/major/{majornumber}", method = RequestMethod.POST)
    public ResponseEntity<Map<String, String>> addMajor(@PathVariable("majornumber") String majornumber_str,
                                                        @RequestBody Major major,
                                                        @RequestHeader(value="Authentication") String authentication){
        Map<String,String> map = new HashMap<>();

        String token = authentication.substring(7);//截取掉“Bearer ”

        String usertype = JWTUtils.decodeToGetValue(token, "usertype");
        if (usertype == null){//token无效情况
            System.out.println("Token is invalid.");
            map.put("message","Token is invalid.");
            return new ResponseEntity<Map<String, String>>(map, HttpStatus.UNAUTHORIZED);
        }

        String str;


        if (Objects.equals(usertype, "admin")){
            try{
                int majornumber = Integer.parseInt(majornumber_str);
                major.setMajornumber(majornumber);

            }catch (NumberFormatException e){//传入majornumber格式不对
                map.put("message","majornumber格式错误！");
                return new ResponseEntity<Map<String, String>>(map, HttpStatus.BAD_REQUEST);
            }

            str = userService.createMajor(major);
            switch (str){
                case "Success":
                    System.out.println("Successfully added!");
                    map.put("message","Successfully added!");
                    return new ResponseEntity<Map<String, String>>(map, HttpStatus.OK);
                case "FormError":
                    System.out.println("FormError!");
                    map.put("message","格式错误！");
                    return new ResponseEntity<Map<String, String>>(map, HttpStatus.BAD_REQUEST);
                default:
                    System.out.println("Unknown Error");
                    map.put("message","Unknown Error");
                    return new ResponseEntity<Map<String, String>>(map ,HttpStatus.BAD_REQUEST);
            }

        }
        else{
            System.out.println("Request is not from administrator");
            map.put("message","Request is not from administrator");
            return new ResponseEntity<Map<String, String>>(map,HttpStatus.FORBIDDEN);
        }

    }

    /*删除专业*/
    @RequestMapping(value="/major/{majornumber}", method = RequestMethod.DELETE)
    public ResponseEntity<Map<String, String>> delMajor(@PathVariable("majornumber") String majornumber_str,
                                                        @RequestHeader(value="Authentication") String authentication){
        Map<String,String> map = new HashMap<>();


        /*对jwt令牌进行验证的操作*/
        String token = authentication.substring(7);//截取掉“Bearer ”

        String usertype = JWTUtils.decodeToGetValue(token, "usertype");
        if (usertype == null){//token无效情况
            System.out.println("Token is invalid.");
            map.put("message","Token is invalid.");
            return new ResponseEntity<Map<String, String>>(map, HttpStatus.UNAUTHORIZED);
        }

        String str;
        if (Objects.equals(usertype, "admin")){
            //验证majornumber格式
            try{
                int majornumber = Integer.parseInt(majornumber_str);
                str = this.userService.deleteMajor(majornumber);

            }catch (NumberFormatException e){//传入majornumber格式不对
                map.put("message","majornumber格式错误！");
                return new ResponseEntity<Map<String, String>>(map, HttpStatus.BAD_REQUEST);
            }
        }
        else{//不是admin
            System.out.println("Request is not from administrator");
            map.put("message","Request is not from administrator");
            return new ResponseEntity<Map<String, String>>(map,HttpStatus.FORBIDDEN);
        }

        switch (str){
            case "success":
                map.put("message","Success");
                return new ResponseEntity<Map<String, String>>(map, HttpStatus.OK);

            case "nonexistent":
                map.put("message","不存在此专业！");
                return new ResponseEntity<Map<String, String>>(map, HttpStatus.BAD_REQUEST);

            default:
                map.put("message","未知错误！");
                return new ResponseEntity<Map<String, String>>(map, HttpStatus.BAD_REQUEST);
        }

    }

    /*返回所有majornumber*/
    @RequestMapping(value="/majors", method = RequestMethod.GET)
    public ResponseEntity<Map<String, List<String>>> getAllMajornumbers(@RequestHeader(value="Authentication") String authentication){
        Map<String,List<String>> map = new HashMap<>();

        List<String> strings = new ArrayList<>();
        /*对jwt令牌进行验证的操作*/
        String token = authentication.substring(7);//截取掉“Bearer ”

        String usertype = JWTUtils.decodeToGetValue(token, "usertype");
        if (usertype == null){//token无效情况
            System.out.println("Token is invalid.");
            return new ResponseEntity<Map<String, List<String>>>(HttpStatus.UNAUTHORIZED);
        }

        String str;
        if (Objects.equals(usertype, "admin")){
            strings = userService.getAllMajornumbers();
            System.out.println("strings="+strings);
            map.put("majornumbers",strings);
            return new ResponseEntity<Map<String, List<String>>>(map, HttpStatus.OK);
        }
        else{//不是admin
            System.out.println("Request is not from administrator");

            return new ResponseEntity<Map<String, List<String>>>(HttpStatus.FORBIDDEN);
        }
    }

    /*获取一个专业*/
    @RequestMapping(value="/major/{majornumber}", method = RequestMethod.GET)
    public ResponseEntity<Map<String, String>> getAMajor(@PathVariable("majornumber") String majornumber_str,
                                                        @RequestHeader(value="Authentication") String authentication) {
        Map<String, String> map = new HashMap<>();

        String token = authentication.substring(7);//截取掉“Bearer ”

        String usertype = JWTUtils.decodeToGetValue(token, "usertype");
        if (usertype == null) {//token无效情况
            System.out.println("Token is invalid.");
            map.put("message", "Token is invalid.");
            return new ResponseEntity<Map<String, String>>(map, HttpStatus.UNAUTHORIZED);
        }

        Major major;
        try {
            int majornumber = Integer.parseInt(majornumber_str);
            major = userService.getAMajor(majornumber);
        } catch (NumberFormatException e) {//传入majornumber格式不对
            map.put("message", "majornumber格式错误！");
            return new ResponseEntity<Map<String, String>>(map, HttpStatus.BAD_REQUEST);
        }

        if (major != null) {
            map.put("name", major.getName());
            map.put("school", major.getSchool());
            map.put("majornumber",String.valueOf(major.getMajornumber()));
            return new ResponseEntity<Map<String, String>>(map, HttpStatus.OK);
        } else {
            map.put("message", "不存在此专业！");
            return new ResponseEntity<Map<String, String>>(map, HttpStatus.BAD_REQUEST);
        }
    }

    /*重写一个专业*/
    @RequestMapping(value="/major/{majornumber}", method = RequestMethod.PUT)
    public ResponseEntity<Map<String, String>> rewriteAMajor(@PathVariable("majornumber") String majornumber_str,
                                                         @RequestBody Major major,
                                                         @RequestHeader(value="Authentication") String authentication) {
        Map<String, String> map = new HashMap<>();

        String token = authentication.substring(7);//截取掉“Bearer ”

        String usertype = JWTUtils.decodeToGetValue(token, "usertype");

        if (usertype == null){//token无效情况
            System.out.println("Token is invalid.");
            map.put("message","Token is invalid.");
            return new ResponseEntity<Map<String, String>>(map, HttpStatus.UNAUTHORIZED);
        }else if (!usertype.equals("admin")){
            System.out.println("Request is not from administrator");
            map.put("message","Request is not from administrator");
            return new ResponseEntity<Map<String, String>>(map,HttpStatus.FORBIDDEN);
        }

        try {
            int majornumber = Integer.parseInt(majornumber_str);
            major.setMajornumber(majornumber);
        } catch (NumberFormatException e) {//传入majornumber格式不对
            map.put("message", "majornumber格式错误！");
            return new ResponseEntity<Map<String, String>>(map, HttpStatus.BAD_REQUEST);
        }

        String str = userService.createMajor(major);

        switch (str){//createMajor的返回操作解析
            case "Success":
                System.out.println("Successfully added!");
                map.put("message","Successfully added!");
                return new ResponseEntity<Map<String, String>>(map, HttpStatus.OK);
            case "FormError":
                System.out.println("FormError!");
                map.put("message","格式错误！");
                return new ResponseEntity<Map<String, String>>(map, HttpStatus.BAD_REQUEST);
            default:
                System.out.println("Unknown Error");
                map.put("message","Unknown Error");
                return new ResponseEntity<Map<String, String>>(map ,HttpStatus.BAD_REQUEST);
        }

    }

    /*修改一个专业,patch*/
    @RequestMapping(value="/major/{majornumber}", method = RequestMethod.PATCH)
    public ResponseEntity<Map<String, String>> changeAMajor(@PathVariable("majornumber") String majornumber_str,
                                                            @RequestBody Major major,
                                                            @RequestHeader(value="Authentication") String authentication) {
        Map<String, String> map = new HashMap<>();

        String token = authentication.substring(7);//截取掉“Bearer ”

        String usertype = JWTUtils.decodeToGetValue(token, "usertype");
        if (usertype == null) {//token无效情况
            System.out.println("Token is invalid.");
            map.put("message", "Token is invalid.");
            return new ResponseEntity<Map<String, String>>(map, HttpStatus.UNAUTHORIZED);
        } else if (!usertype.equals("admin")) {
            System.out.println("Request is not from administrator");
            map.put("message", "Request is not from administrator");
            return new ResponseEntity<Map<String, String>>(map, HttpStatus.FORBIDDEN);
        }

        int majornumber;
        try {
            majornumber = Integer.parseInt(majornumber_str);
            major.setMajornumber(majornumber);
        } catch (NumberFormatException e) {//传入majornumber格式不对
            map.put("message", "majornumber格式错误！");
            return new ResponseEntity<Map<String, String>>(map, HttpStatus.BAD_REQUEST);
        }

        Major major_old = userService.getAMajor(majornumber);
        if(major.getSchool() != null)  major_old.setSchool(major.getSchool());
        if(major.getName() != null) major_old.setName(major.getName());

        String str = userService.createMajor(major_old);
System.out.println(str);
        switch (str){//createMajor的返回操作解析
            case "Success":
                System.out.println("Successfully added!");
                map.put("message","Successfully added!");
                return new ResponseEntity<Map<String, String>>(map, HttpStatus.OK);
            case "FormError":
                System.out.println("FormError!");
                map.put("message","格式错误！");
                return new ResponseEntity<Map<String, String>>(map, HttpStatus.BAD_REQUEST);
            default:
                System.out.println("Unknown Error");
                map.put("message","Unknown Error");
                return new ResponseEntity<Map<String, String>>(map ,HttpStatus.BAD_REQUEST);
        }

    }



}
