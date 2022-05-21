package com.example.demo.controller;

import com.example.demo.bean.Lesson;
import com.example.demo.bean.Lessonrequest;
import com.example.demo.bean.User;
import com.example.demo.service.GeneralService;
import com.example.demo.service.LessonConductService;
import com.example.demo.utils.BeanTools;
import com.example.demo.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.example.demo.utils.JWTUtils.*;
import static com.example.demo.utils.JWTUtils.AdminAuthority;

@RestController
@CrossOrigin
public class LessonrequestController extends BasicController <Lessonrequest> {
    private final GeneralService<User> userService;
    private final GeneralService<Lessonrequest> lessonreqService;
    private final LessonConductService lessonConductService;

    @Autowired
    public LessonrequestController(GeneralService<User> userService, GeneralService<Lessonrequest> lessonreqService, LessonConductService lessonConductService) {
        this.userService = userService;
        this.lessonreqService = lessonreqService;
        this.lessonConductService = lessonConductService;
    }

    @Override
    String getIds() {
        return "lessonrequestids";
    }

    @Override
    String getBean() {
        return "lessonrequest";
    }

    @Override
    String getBeans() {
        return "lessonrequests";
    }

    /* ****************以下为管理员操作****************** */

    /* 1-查--getAllIds--获取所有id*/
    @Override
    Map<String, Object> getAllIds_impl(String authority, String userid) {
        Map<String, Object> map = new HashMap<>();
        switch (authority){
            case AdminAuthority ->{
                map.put("result", "Success");
                map.put(getIds() , lessonreqService.getAllIds());
            }
            default -> {
                map.put("result", "NoAuth");
            }
        }
        return map;
    }

    @Override
    @RequestMapping(value="/lessonrequests/lessonrequestids", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getAllIds(@RequestHeader(value="Authentication") String authentication) {
        return super.getAllIds(authentication);
    }

    /* 2-查--getABean--获取一个实体*/
    @Override
    Map<String, Object> getABean_impl(String authority, String userid, String key) {
        Map<String, Object> map = new HashMap<>();

        switch (authority){
            case AdminAuthority ->{
                if (lessonreqService.getABean(key) != null){
                    map.put("result", "Success");
                    map.put(getBean() , lessonreqService.getABean(key));
                }
                else{
                    map.put("result", "NotFound");
                }
            }
            default -> {
                map.put("result", "NoAuth");
            }
        }
        return map;
    }

    @Override
    @RequestMapping(value="/lessonrequest/{lessonrequestid}", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getABean(@RequestHeader(value="Authentication") String authentication,
                                                        @PathVariable("lessonrequestid") String key) {
        return super.getABean(authentication, key);
    }

    /* 3-查--getAllBeans--获取全部实体*/
    @Override
    Map<String, Object> getAllBeans_impl(String authority, String userid) {
        Map<String, Object> map = new HashMap<>();

        switch (authority) {
            case AdminAuthority -> {
                map.put(getBeans(), lessonreqService.getAllBeans());
                map.put("result", "Success");
            }
            case TeacherAuthority, StudentAuthority -> {
                User user = userService.getABean(userid);
                map.put(getBeans(), user.getLessonrequests());
                map.put("result", "Success");
            }
            default -> {
                map.put("result", "NoAuth");
            }
        }
        return map;
    }

    @Override
    @RequestMapping(value = "/lessonrequests", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getAllBeans(@RequestHeader(value="Authentication") String authentication) {
        return super.getAllBeans(authentication);
    }

    /* 4-增--createABean--新增一个实体*/

    @Override
    Map<String, Object> createABean_impl(String authority, String userid, Lessonrequest bean) {
        Map<String, Object> map = new HashMap<>();

        switch (authority) {
            case TeacherAuthority, StudentAuthority -> {
                User user = userService.getABean(userid);
                bean.setApplicant(user);
                String message = lessonreqService.createABean(bean);
                map.put("message", message);
                map.put("result", "Message");

            }
            default -> {
                map.put("result", "NoAuth");
            }
        }
        return map;
    }


    @Override
    @RequestMapping(value="/lessonrequest", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> createABean(@RequestHeader(value = "Authentication") String authentication,
                                                           @RequestBody Lessonrequest bean) {
        return super.createABean(authentication, bean);
    }

    @Override
    Map<String, Object> createBeans_impl(String authority, String userid, List<Lessonrequest> beans) {
        return null;
    }

    @Override
    Map<String, Object> rewriteABean_impl(String authority, String userid, String key, Lessonrequest bean) {
        return null;
    }

    @Override
    Map<String, Object> modifyABean_impl(String authority, String userid, String key, Lessonrequest bean) {
        Map<String, Object> map = new HashMap<>();
        switch (authority){
            case AdminAuthority->{
                Lessonrequest bean_ori = lessonreqService.getABean(key);
                if (bean_ori == null){
                    map.put("result", "NotFound");
                    return map;
                }
                else if (bean_ori.getStatus() != Lessonrequest.Status.pending){
                    map.put("result", "NoAuth");
                    return map;
                }
                map.put("result", "Success");
                String [] adminauth = {"status"};
                List<String> changeableList = new ArrayList<>(Arrays.asList(adminauth));
                Lessonrequest bean_modified = BeanTools.modify(bean_ori, bean, changeableList);
                //通过申请时自动选课
                if(bean_modified.getStatus() == Lessonrequest.Status.accepted){
                    User applicant = bean_modified.getApplicant();
                    Lesson lesson = bean_modified.getLesson();
                    String message = lessonConductService.autoSelectALesson(applicant, lesson);
                    if (!message.equals("Success")){
                        map.put("message", message);
                        map.put("result", "Message");
                        return map;
                    }

                }
                map.put("result", lessonreqService.changeABean(key,bean_modified));
                return map;
            }
            case StudentAuthority, TeacherAuthority -> {
                String result;
                Lessonrequest bean_ori = lessonreqService.getABean(key);
                if (bean_ori == null) {
                    result = "NotFound";
                } else if (!Objects.equals(bean_ori.getApplicant().getUserid(), userid)) {//申请的学号不对应
                    result = "NoAuth";
                } else if (bean_ori.getStatus() != Lessonrequest.Status.pending) {//申请已被审批
                    result = "NoAuth";
                } else {
                    map.put("result", "Success");
                    String[] auth = {"requestReason"};

                    List<String> changeableList = new ArrayList<>(Arrays.asList(auth));
                    Lessonrequest bean_modified = BeanTools.modify(bean_ori, bean, changeableList);
                    result = lessonreqService.changeABean(key, bean_modified);

                }
                map.put("result", result);
            }
            default -> {
                map.put("result", "NoAuth");
            }
        }
        return map;
    }

    @Override
    @RequestMapping(value="/lessonrequest/{lessonrequestid}", method = RequestMethod.PATCH)
    public ResponseEntity<Map<String, Object>> modifyABean(@RequestHeader(value = "Authentication") String authentication,
                                                           @PathVariable("lessonrequestid") String key,
                                                           @RequestBody Lessonrequest bean) {
        return super.modifyABean(authentication, key, bean);
    }

    /* 8-删--deleteABean--删除一个实体*/
    @Override
    Map<String, Object> delBean_impl(String authority, String userid, String key) {
        Map<String, Object> map = new HashMap<>();
        String result;
        switch (authority) {
            case AdminAuthority -> {
                result = lessonreqService.deleteABean(key);
                map.put("result", result);
            }
            case StudentAuthority -> {
                Lessonrequest bean = lessonreqService.getABean(key);
                if (bean.getStatus() != Lessonrequest.Status.pending){//申请已被审批
                    result = "NoAuth";
                }
                else{//只能给自己创建
                    if (!Objects.equals(bean.getApplicant().getUserid(), userid)){//申请的学号不对应
                        result = "NoAuth";
                    }
                    else
                        result = lessonreqService.deleteABean(key);
                }
                map.put("result", result);
            }
            default -> {
                map.put("result", "NoAuth");
            }
        }
        return map;
    }

    @Override
    @RequestMapping(value="lessonrequest/{lessonrequestid}", method = RequestMethod.DELETE)
    public ResponseEntity<Map<String, Object>> delBean(@RequestHeader(value="Authentication") String authentication,
                                                       @PathVariable("lessonrequestid") String key){
        return super.delBean(authentication, key);
    }

    @Override
    Map<String, Object> delBeans_impl(String authority, String userid, List<?> ids) {
        return null;
    }

    /* ****************以下为学生本人操作****************** */

    /* 3-查--getAllBeans--获取学生本人的全部实体*/
    @RequestMapping(value = "/user/{userid}/lessonrequests", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getAllBeans_Student(@RequestHeader(value="Authentication") String authentication,
                                                                   @PathVariable (name = "userid") String userid_url) {
        Map<String, Object> map = new HashMap<>();

        String credit = ControllerOperation.checkAuthentication(authentication);
        String authority = ControllerOperation.getAuthority(authentication);
        String userid = JWTUtils.decodeToGetValue(authentication.substring(7), "userid");

        if (credit.equals(ValidJWTToken)){
            String result;
            switch (authority) {
                case StudentAuthority -> {
                    if (!Objects.equals(userid_url, userid)){//申请的学号不对应
                        result = "NoAuth";
                    }
                    else{//只能给自己创建
                        User user = userService.getABean(userid);
                        map.put("lessonrequests", user.getLessonrequests());
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


    /* 4-增--createABean--新增一个实体*/
    @RequestMapping(value="/user/{userid}/lessonrequest", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> createABean(@RequestHeader(value = "Authentication") String authentication,
                                                           @PathVariable (name = "userid") String userid_url,
                                                           @RequestBody Lessonrequest bean){
        Map<String, Object> map = new HashMap<>();

        String credit = ControllerOperation.checkAuthentication(authentication);
        String authority = ControllerOperation.getAuthority(authentication);
        String userid = JWTUtils.decodeToGetValue(authentication.substring(7), "userid");

        if (credit.equals(ValidJWTToken)){
            String result;
            switch (authority) {
                case StudentAuthority -> {
                    if (!Objects.equals(userid_url, userid)){//申请的学号不对应
                        result = "NoAuth";
                    }
                    else{//只能给自己创建
                        User user = userService.getABean(userid);
                        bean.setApplicant(user);
                        String message = lessonreqService.createABean(bean);
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

    /* 7-改--modifyABean--修改一个实体,patch*/
    @RequestMapping(value="/user/{userid}/lessonrequest/{lessonrequestid}", method = RequestMethod.PATCH)
    public ResponseEntity<Map<String, Object>> modifyABean_student(@RequestHeader(value = "Authentication") String authentication,
                                                                   @PathVariable (name = "userid") String userid_url,
                                                                   @PathVariable(name = "lessonrequestid") String key,
                                                                   @RequestBody Lessonrequest bean){
        Map<String, Object> map = new HashMap<>();

        String credit = ControllerOperation.checkAuthentication(authentication);
        String authority = ControllerOperation.getAuthority(authentication);
        String userid = JWTUtils.decodeToGetValue(authentication.substring(7), "userid");

        if (credit.equals(ValidJWTToken)){
            String result;
            switch (authority) {
                case StudentAuthority -> {
                    if (!Objects.equals(userid_url, userid)){//申请的学号不对应
                        result = "NoAuth";
                    }
                    else{//只能给自己创建
                        Lessonrequest bean_ori = lessonreqService.getABean(key);
                        if (bean_ori == null){
                            result = "NotFound";
                        }
                        else if (!Objects.equals(bean_ori.getApplicant().getUserid(), userid)){//申请的学号不对应
                            result = "NoAuth";
                        }
                        else if (bean_ori.getStatus() != Lessonrequest.Status.pending){//申请已被审批
                            result = "NoAuth";
                        }
                        else{
                            map.put("result", "Success");
                            String [] auth = {"requestReason"};

                            List<String> changeableList = new ArrayList<>(Arrays.asList(auth));
                            Lessonrequest bean_modified = BeanTools.modify(bean_ori, bean, changeableList);
                            result = lessonreqService.changeABean(key,bean_modified);

                        }
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

    /* 8-删--deleteABean--删除一个实体*/
    @RequestMapping(value="/user/{userid}/lessonrequest/{lessonrequestid}", method = RequestMethod.DELETE)
    public ResponseEntity<Map<String, Object>> delBean(@RequestHeader(value="Authentication") String authentication,
                                                       @PathVariable (name = "userid") String userid_url,
                                                       @PathVariable("lessonrequestid") String key) {
        Map<String, Object> map = new HashMap<>();

        String credit = ControllerOperation.checkAuthentication(authentication);
        String authority = ControllerOperation.getAuthority(authentication);
        String userid = JWTUtils.decodeToGetValue(authentication.substring(7), "userid");

        if (credit.equals(ValidJWTToken)){
            String result;
            switch (authority) {
                case StudentAuthority -> {
                    Lessonrequest bean = lessonreqService.getABean(key);
                    if (!Objects.equals(userid_url, userid)){//申请的学号不对应
                        result = "NoAuth";
                    }
                    else if (bean.getStatus() != Lessonrequest.Status.pending){//申请已被审批
                        result = "NoAuth";
                    }
                    else{//只能给自己创建
                        if (!Objects.equals(bean.getApplicant().getUserid(), userid)){//申请的学号不对应
                            result = "NoAuth";
                        }
                        else
                            result = lessonreqService.deleteABean(key);
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

}
