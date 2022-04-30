package com.example.demo.controller;

import com.alibaba.fastjson.JSONArray;
import com.example.demo.utils.BeanTools;
import com.example.demo.bean.Major;
import com.example.demo.service.GeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.example.demo.utils.JWTUtils.*;

@RestController
@CrossOrigin("http://localhost:3000")
public class MajorController extends BasicController<Major> {
    private final GeneralService<Major> majorService;

    @Autowired
    public MajorController(GeneralService<Major> majorService) {
        this.majorService = majorService;
    }


    /* 该类中所有的方法都来自继承 */


    @Override
    String getIds() {
        return "majornumbers";
    }

    @Override
    String getBean() {
        return "major";
    }

    @Override
    String getBeans() {
        return null;
    }


    /*查--返回所有majornumber*/

    @Override
    Map<String, Object> getAllIds_impl(String authority, String name) {
        Map<String, Object> map = new HashMap<>();
        switch (authority){
            case AdminAuthority, TeacherAuthority, StudentAuthority ->{
                map.put("result", "Success");
                map.put(getIds() , majorService.getAllIds());
            }
            default -> {
                map.put("result", "NoAuth");
            }
        }
        return map;
    }

    @Override
    @RequestMapping(value="/majors", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getAllIds(@RequestHeader(value="Authentication") String authentication) {
        System.out.println("111");
        return super.getAllIds(authentication);
    }

    /*查--获取一个实体*/
    @Override
    Map<String, Object> getABean_impl(String authority, String id, String searchid) {
        Map<String, Object> map = new HashMap<>();

        switch (authority){
            case AdminAuthority, StudentAuthority, TeacherAuthority ->{
                if (majorService.getABean(id) != null){
                    map.put("result", "Success");
                    map.put(getBean() , majorService.getABean(id));
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
    @RequestMapping(value="/major/{majornumber}", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getABean(@PathVariable("majornumber") String majornumber_str,
                                                        @RequestHeader(value="Authentication") String authentication) {
        return super.getABean(majornumber_str, authentication);
    }

    @Override
    Map<String, Object> getAllBeans_impl(String authority, String schoolnumber) {
        return null;
    }

    @Override
    Map<String, Object> createABean_impl(String authority, String schoolnumber, String key, Major bean) {
        return null;
    }

    @Override
    Map<String, Object> createBeans_impl(String authority, String schoolnumber, List<Major> beans) {
        return null;
    }

    @Override
    Map<String, Object> rewriteABean_impl(String authority, String schoolnumber, String key, Major bean) {
        return null;
    }

    @Override
    Map<String, Object> modifyABean_impl(String authority, String schoolnumber, String key, Major bean) {
        return null;
    }

    /*增--新增一个实体,post*/
    @Override
    Map<String, Object> createABean_impl(String authority, String id, Major bean, String name) {
        Map<String, Object> map = new HashMap<>();
        switch (authority){
            case AdminAuthority->{
                map.put("result", majorService.createABean(id, bean));
            }
            default -> {
                map.put("result", "NoAuth");
            }
        }
        return map;
    }

    @RequestMapping(value="/major/{majornumber}", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> createABean(@PathVariable("majornumber") String majornumber_str,
                                                           @RequestBody Major major,
                                                           @RequestHeader(value="Authentication") String authentication){
        return super.createABean(majornumber_str, major, authentication);

    }

    /*改--重写一个实体*/
    @Override
    Map<String, Object> rewriteABean_impl(String authority, String id, Major bean) {
        Map<String, Object> map = new HashMap<>();
        switch (authority){
            case AdminAuthority->{
                Major bean_ori = majorService.getABean(id);
                if (bean_ori == null){
                    map.put("result", "NotFound");
                    return map;
                }
                map.put("result", majorService.changeABean(id,bean));
                return map;
            }
            default -> {
                map.put("result", "NoAuth");
            }
        }
        return map;
    }

    @Override
    @RequestMapping(value="/major/{majornumber}", method = RequestMethod.PUT)
    public ResponseEntity<Map<String, Object>> rewriteABean(@PathVariable("majornumber") String majornumber_str,
                                                            @RequestBody Major major,
                                                            @RequestHeader(value="Authentication") String authentication) {
        return super.rewriteABean(majornumber_str, major, authentication);
    }

    /*改--重写一个实体*/
    @Override
    Map<String, Object> modifyABean_impl(String authority, String id, Major bean) {
        Map<String, Object> map = new HashMap<>();
        switch (authority){
            case AdminAuthority->{
                Major bean_ori = majorService.getABean(id);
                if (bean_ori == null){
                    map.put("result", "NotFound");
                    return map;
                }
                map.put("result", "Success");
                String [] adminauth = {"school", "name"};

                List<String> changeableList = new ArrayList<>(Arrays.asList(adminauth));
                Major bean_modified = BeanTools.modify(bean_ori, bean, changeableList);
                map.put("result", majorService.changeABean(id,bean_modified));
                return map;
            }
            default -> {
                map.put("result", "NoAuth");
                return map;
            }
        }
    }

    @Override
    @RequestMapping(value="/major/{majornumber}", method = RequestMethod.PATCH)
    public ResponseEntity<Map<String, Object>> modifyABean(@PathVariable("majornumber") String majornumber_str,
                                                           @RequestBody Major major,
                                                           @RequestHeader(value="Authentication") String authentication) {
        return super.modifyABean(majornumber_str, major, authentication);
    }

    /*删--删除一个实体*/
    @Override
    Map<String, Object> delBean_impl(String authority, String keyword, String name) {
        Map<String, Object> map = new HashMap<>();
        switch (authority){
            case AdminAuthority->{
                map.put("result", majorService.deleteABean(keyword));
            }
            default -> {
                map.put("result", "NoAuth");
            }
        }
        return map;
    }

    @Override
    @RequestMapping(value="/major/{majornumber}", method = RequestMethod.DELETE)
    public ResponseEntity<Map<String, Object>> delBean(@PathVariable("majornumber") String majornumber_str,
                                                       @RequestHeader(value="Authentication") String authentication) {
        return super.delBean(majornumber_str, authentication);
    }

    @Override
    Map<String, Object> delBeans_impl(String authority, String schoolnumber, List<?> ids) {
        return null;
    }

    /***************未使用的抽象方法******************/
    @Override
    Map<String, Object> getAllBeans_impl(String authority) {
        return null;
    }

    @Override
    Map<String, Object> createBeans_impl(String authority, JSONArray jsonArray) {
        return null;
    }

    @Override
    Map<String, Object> delBeans_impl(String authority) {
        return null;
    }


}
