package com.example.demo.controller;

import com.example.demo.bean.BeanTools;
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
        userService.getAllMajornumbers();
    }

    /*查--获取新majornumber*/
    @RequestMapping(value="/majornumber", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getANewMajornumber(@RequestHeader(value="Authentication") String authentication){
        Map<String,Object> map = new HashMap<>();

        String credit = ControllerOperation.checkAuthentication(authentication);

        if (credit.equals("IsAdmin")){//一定成功，不做封装
            String newMajornumber = userService.getANewMajornumber();

            map.put("majornumber", newMajornumber);
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
        else {
            return ControllerOperation.getErrorResponse(credit, map);
        }


    }

    /*查--返回所有majornumber*/
    @RequestMapping(value="/majors", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getAllMajornumbers(@RequestHeader(value="Authentication") String authentication){
        Map<String, Object> map = new HashMap<>();

        String credit = ControllerOperation.checkAuthentication(authentication);

        if (credit.equals("IsAdmin")){//一定成功，不做封装
            List<String>strings = userService.getAllMajornumbers();
            System.out.println("strings="+strings);
            map.put("majornumbers",strings);
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
        else {
            return ControllerOperation.getErrorResponse(credit, map);
        }
    }

    /*查--获取一个专业*/
    @RequestMapping(value="/major/{majornumber}", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getAMajor(@PathVariable("majornumber") String majornumber_str,
                                                         @RequestHeader(value="Authentication") String authentication) {
        Map<String, Object> map = new HashMap<>();

        String credit = ControllerOperation.checkAuthentication(authentication);
        if (!credit.equals("InvalidTokenError")){

            Major major = userService.getAMajor(majornumber_str);

            return ControllerOperation.getSearchResponse(major, map);
        }else {
            return ControllerOperation.getErrorResponse(credit, map);
        }

    }

    /*增--新增专业*/
    @RequestMapping(value="/major/{majornumber}", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> addMajor(@PathVariable("majornumber") String majornumber_str,
                                                        @RequestBody Major major,
                                                        @RequestHeader(value="Authentication") String authentication){
        Map<String,Object> map = new HashMap<>();

        String credit = ControllerOperation.checkAuthentication(authentication);
        if (credit.equals("IsAdmin")){
            try{
                int majornumber = Integer.parseInt(majornumber_str);
                major.setMajornumber(majornumber);

            }catch (NumberFormatException e){//传入majornumber格式不对
                map.put("message","majornumber格式错误！");
                return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
            }
            String result = userService.writeAMajor(majornumber_str, major);
            return ControllerOperation.getConductResponse(result, map);
        }
        else{
            return ControllerOperation.getErrorResponse(credit, map);
        }

    }

    /*改--重写一个专业*/
    @RequestMapping(value="/major/{majornumber}", method = RequestMethod.PUT)
    public ResponseEntity<Map<String, Object>> rewriteAMajor(@PathVariable("majornumber") String majornumber_str,
                                                             @RequestBody Major major,
                                                             @RequestHeader(value="Authentication") String authentication) {
        Map<String, Object> map = new HashMap<>();

        String credit = ControllerOperation.checkAuthentication(authentication);

        if (credit.equals("IsAdmin")){
//空指针异常
            String result = userService.writeAMajor(majornumber_str, major);
//            Major major_ori = userService.getAMajor(majornumber_str);
//            String result;
//            if (major_ori == null){
//                result = "NotFound";
//            }
//            else{
//                major.setMajornumber(major_ori.getMajornumber());
//                result = userService.createMajor(majornumber_str, major);
//            }
            return ControllerOperation.getConductResponse(result,map);
        }
        else {
            return ControllerOperation.getErrorResponse(credit, map);
        }

    }

    /*!!改--修改一个专业,patch*/
    @RequestMapping(value="/major/{majornumber}", method = RequestMethod.PATCH)
    public ResponseEntity<Map<String, Object>> changeAMajor(@PathVariable("majornumber") String majornumber_str,
                                                            @RequestBody Major major,
                                                            @RequestHeader(value="Authentication") String authentication) {
        Map<String, Object> map = new HashMap<>();

        String credit = ControllerOperation.checkAuthentication(authentication);

        if (credit.equals("IsAdmin")){
            Major major_ori = userService.getAMajor(majornumber_str);
            if (major_ori == null)//查找不到
                return ControllerOperation.getSearchResponse(major_ori, map);
            major.setMajornumber(major_ori.getMajornumber());

            String [] changeable = {"email", "password", "phonenumber"};
            List<String> changeableList;
            changeableList = new ArrayList<>(Arrays.asList(changeable));
            Major major_modified = BeanTools.modify(major_ori, major, changeableList);
            String result = userService.writeAMajor(majornumber_str, major_modified);
            return ControllerOperation.getConductResponse(result, map);
        }
        else {
            return ControllerOperation.getErrorResponse(credit, map);
        }

    }


    /*删--删除专业*/
    @RequestMapping(value="/major/{majornumber}", method = RequestMethod.DELETE)
    public ResponseEntity<Map<String, Object>> delMajor(@PathVariable("majornumber") String majornumber_str,
                                                        @RequestHeader(value="Authentication") String authentication){
        Map<String, Object> map = new HashMap<>();

        String credit = ControllerOperation.checkAuthentication(authentication);

        if (credit.equals("IsAdmin")){

            String result = userService.deleteMajor(majornumber_str);

            return ControllerOperation.getConductResponse(result, map);
        }
        else {
            return ControllerOperation.getErrorResponse(credit, map);
        }

    }


}