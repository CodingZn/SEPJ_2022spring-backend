package com.example.demo.controller;

import com.example.demo.bean.Lessonrequest;
import com.example.demo.service.GeneralService;
import com.example.demo.utils.BeanTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.example.demo.utils.JWTUtils.*;
import static com.example.demo.utils.JWTUtils.AdminAuthority;

@RestController
@CrossOrigin("http://localhost:3000")
public class LessonrequestController extends BasicController <Lessonrequest> {
    private final GeneralService<Lessonrequest> lessonreqService;

    @Autowired
    public LessonrequestController(GeneralService<Lessonrequest> lessonreqService) {
        this.lessonreqService = lessonreqService;
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
            case AdminAuthority, StudentAuthority ->{
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
    Map<String, Object> createABean_impl(String authority, String userid, String key, Lessonrequest bean) {
        Map<String, Object> map = new HashMap<>();
        switch (authority){
            case StudentAuthority->{
                map.put("result", lessonreqService.createABean(key, bean));
            }
            default -> {
                map.put("result", "NoAuth");
            }
        }
        return map;
    }

    @Override
    @RequestMapping(value="/lessonrequest/{lessonrequestid}", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> createABean(@RequestHeader(value = "Authentication") String authentication,
                                                           @PathVariable("lessonrequestid") String key,
                                                           @RequestBody Lessonrequest bean){
        return super.createABean(authentication, key, bean);

    }

    /* 5-增--createBeans--新增多个实体*/
    @Override
    Map<String, Object> createBeans_impl(String authority, String userid, List<Lessonrequest> beans) {
//        Map<String, Object> map = new HashMap<>();
//        switch (authority) {
//            case AdminAuthority -> {
//                map.put("result", lessonreqService.createBeans(beans));
//            }
//            default -> {
//                map.put("result", "NoAuth");
//            }
//        }
//        return map;
        return null;
    }

//    @Override
//    @RequestMapping(value = "/lessonrequests", method = RequestMethod.POST)
//    public ResponseEntity<Map<String, Object>> createBeans(@RequestHeader(value = "Authentication") String authentication,
//                                                           @RequestBody JSONArray jsonArray, Class<Lessonrequest> clazz) {
//        return super.createBeans(authentication, jsonArray, clazz);
//    }


    /* 6-改--rewriteABean--重写一个实体,put*/
    @Override
    Map<String, Object> rewriteABean_impl(String authority, String userid, String key, Lessonrequest bean) {
//        Map<String, Object> map = new HashMap<>();
//        switch (authority){
//            case AdminAuthority->{
//                map.put("result", lessonreqService.changeABean(key, bean));
//                return map;
//            }
//            default -> {
//                map.put("result", "NoAuth");
//            }
//        }
//        return map;
        return null;
    }

//    @Override
//    @RequestMapping(value="/lessonrequest/{lessonrequestid}", method = RequestMethod.PUT)
//    public ResponseEntity<Map<String, Object>> rewriteABean(@RequestHeader(value = "Authentication") String authentication,
//                                                            @PathVariable("lessonrequestid") String key,
//                                                            @RequestBody Lessonrequest bean) {
//        return super.rewriteABean(authentication, key, bean);
//    }

    /* 7-改--modifyABean--修改一个实体,patch*/
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
                map.put("result", "Success");
                String [] adminauth = {"status"};
//自动选课！写到服务层更好？
                List<String> changeableList = new ArrayList<>(Arrays.asList(adminauth));
                Lessonrequest bean_modified = BeanTools.modify(bean_ori, bean, changeableList);
                map.put("result", lessonreqService.changeABean(key,bean_modified));
                return map;
            }
            case StudentAuthority->{
                Lessonrequest bean_ori = lessonreqService.getABean(key);
                if (bean_ori == null){
                    map.put("result", "NotFound");
                    return map;
                }
                map.put("result", "Success");
                String [] auth = {"requestReason"};

                List<String> changeableList = new ArrayList<>(Arrays.asList(auth));
                Lessonrequest bean_modified = BeanTools.modify(bean_ori, bean, changeableList);
                map.put("result", lessonreqService.changeABean(key,bean_modified));
                return map;
            }
            default -> {
                map.put("result", "NoAuth");
                return map;
            }
        }
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
        switch (authority){
            case StudentAuthority->{
                map.put("result", lessonreqService.deleteABean(key));
            }
            default -> {
                map.put("result", "NoAuth");
            }
        }
        return map;
    }

    @Override
    @RequestMapping(value="/lessonrequest/{lessonrequestid}", method = RequestMethod.DELETE)
    public ResponseEntity<Map<String, Object>> delBean(@RequestHeader(value="Authentication") String authentication,
                                                       @PathVariable("lessonrequestid") String key) {
        return super.delBean(authentication, key);
    }

    /* 9-删--deleteBeans--删除多个实体*/
    @Override
    Map<String, Object> delBeans_impl(String authority, String userid, List<?> ids) {
//        Map<String, Object> map = new HashMap<>();
//        switch (authority) {
//            case AdminAuthority -> {
//                map.put("result", lessonreqService.deleteBeans(ids));
//            }
//            default -> {
//                map.put("result", "NoAuth");
//            }
//        }
//        return map;
        return null;
    }

//    @Override
//    @RequestMapping(value = "/lessonrequests", method = RequestMethod.DELETE)
//    public ResponseEntity<Map<String, Object>> delBeans(@RequestHeader(value = "Authentication") String authentication,
//                                                        @RequestBody JSONArray jsonArray, Class<?> clazz) {
//        return super.delBeans(authentication, jsonArray, String.class);
//    }

    /* **************特殊操作**************** */

    /* 修改选课状态后，自动选入课程*/
    private boolean autoSelectLesson( String lessonid, String studentid){
        return false;
    }
}
