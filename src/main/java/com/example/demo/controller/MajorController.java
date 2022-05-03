package com.example.demo.controller;

import com.alibaba.fastjson.JSONArray;
import com.example.demo.bean.generators.MajoridGenerator;
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
        setGenerator(majorService);
    }

    private void setGenerator(GeneralService<Major> majorService) {
        List<Major> majors = majorService.getAllBeans();
        Optional<Major> max_major = majors.stream().max(Comparator.comparing(u -> Integer.parseInt(u.getMajorid())));
        if (max_major.isPresent()){
            MajoridGenerator.setNextMajorid(Integer.parseInt(max_major.get().getMajorid()) + 1);
        }
        else{
            MajoridGenerator.setNextMajorid(1);
        }
    }


    /* 该类中所有的方法都来自继承 */


    @Override
    String getIds() {
        return "majorids";
    }

    @Override
    String getBean() {
        return "major";
    }

    @Override
    String getBeans() {
        return "majors";
    }


    /* 1-查--getAllIds--获取所有id*/
    @Override
    Map<String, Object> getAllIds_impl(String authority, String userid) {
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
    @RequestMapping(value="/majors/majorids", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getAllIds(@RequestHeader(value="Authentication") String authentication) {
        return super.getAllIds(authentication);
    }

    /* 2-查--getABean--获取一个实体*/
    @Override
    Map<String, Object> getABean_impl(String authority, String userid, String key) {
        Map<String, Object> map = new HashMap<>();

        switch (authority){
            case AdminAuthority, StudentAuthority, TeacherAuthority ->{
                if (majorService.getABean(key) != null){
                    map.put("result", "Success");
                    map.put(getBean() , majorService.getABean(key));
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
    @RequestMapping(value="/major/{majorid}", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getABean(@RequestHeader(value="Authentication") String authentication,
                                                        @PathVariable("majorid") String key) {
        return super.getABean(authentication, key);
    }

    /* 3-查--getAllBeans--获取全部实体*/
    @Override
    Map<String, Object> getAllBeans_impl(String authority, String userid) {
        Map<String, Object> map = new HashMap<>();

        switch (authority) {
            case AdminAuthority -> {
                map.put(getBeans(), majorService.getAllBeans());
                map.put("result", "Success");
            }
            default -> {
                map.put("result", "NoAuth");
            }
        }
        return map;
    }

    @Override
    @RequestMapping(value = "/majors", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getAllBeans(@RequestHeader(value="Authentication") String authentication) {
        return super.getAllBeans(authentication);
    }

    /* 4-增--createABean--新增一个实体*/
    @Override
    Map<String, Object> createABean_impl(String authority, String userid, Major bean) {
        Map<String, Object> map = new HashMap<>();
        switch (authority){
            case AdminAuthority->{
                map.put("result", majorService.createABean(bean));
            }
            default -> {
                map.put("result", "NoAuth");
            }
        }
        return map;
    }

    @Override
    @RequestMapping(value="/major", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> createABean(@RequestHeader(value = "Authentication") String authentication,
                                                           @RequestBody Major bean){
        return super.createABean(authentication, bean);

    }

    /* 5-增--createBeans--新增多个实体*/
    @Override
    Map<String, Object> createBeans_impl(String authority, String userid, List<Major> beans) {
        Map<String, Object> map = new HashMap<>();
        switch (authority) {
            case AdminAuthority -> {
                map.put("result", majorService.createBeans(beans));
            }
            default -> {
                map.put("result", "NoAuth");
            }
        }
        return map;
    }

    @Override
    @RequestMapping(value = "/majors", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> createBeans(@RequestHeader(value = "Authentication") String authentication,
                                                           @RequestBody JSONArray jsonArray, Class<Major> clazz) {

        return super.createBeans(authentication, jsonArray, Major.class);
    }


    /* 6-改--rewriteABean--重写一个实体,put*/
    @Override
    Map<String, Object> rewriteABean_impl(String authority, String userid, String key, Major bean) {
        Map<String, Object> map = new HashMap<>();
        switch (authority){
            case AdminAuthority->{
                map.put("result", majorService.changeABean(key, bean));
                return map;
            }
            default -> {
                map.put("result", "NoAuth");
            }
        }
        return map;
    }

    @Override
    @RequestMapping(value="/major/{majorid}", method = RequestMethod.PUT)
    public ResponseEntity<Map<String, Object>> rewriteABean(@RequestHeader(value = "Authentication") String authentication,
                                                            @PathVariable("majorid") String key,
                                                            @RequestBody Major bean) {
        return super.rewriteABean(authentication, key, bean);
    }

    /* 7-改--modifyABean--修改一个实体,patch*/
    @Override
    Map<String, Object> modifyABean_impl(String authority, String userid, String key, Major bean) {
        Map<String, Object> map = new HashMap<>();
        switch (authority){
            case AdminAuthority->{
                Major bean_ori = majorService.getABean(key);
                if (bean_ori == null){
                    map.put("result", "NotFound");
                    return map;
                }
                map.put("result", "Success");
                String [] adminauth = {"school", "name"};

                List<String> changeableList = new ArrayList<>(Arrays.asList(adminauth));
                Major bean_modified = BeanTools.modify(bean_ori, bean, changeableList);
                map.put("result", majorService.changeABean(key,bean_modified));
                return map;
            }
            default -> {
                map.put("result", "NoAuth");
                return map;
            }
        }
    }

    @Override
    @RequestMapping(value="/major/{majorid}", method = RequestMethod.PATCH)
    public ResponseEntity<Map<String, Object>> modifyABean(@RequestHeader(value = "Authentication") String authentication,
                                                           @PathVariable("majorid") String key,
                                                           @RequestBody Major bean) {
        return super.modifyABean(authentication, key, bean);
    }

    /* 8-删--deleteABean--删除一个实体*/
    @Override
    Map<String, Object> delBean_impl(String authority, String userid, String key) {
        Map<String, Object> map = new HashMap<>();
        switch (authority){
            case AdminAuthority->{
                map.put("result", majorService.deleteABean(key));
            }
            default -> {
                map.put("result", "NoAuth");
            }
        }
        return map;
    }

    @Override
    @RequestMapping(value="/major/{majorid}", method = RequestMethod.DELETE)
    public ResponseEntity<Map<String, Object>> delBean(@RequestHeader(value="Authentication") String authentication,
                                                       @PathVariable("majorid") String key) {
        return super.delBean(authentication, key);
    }

    /* 9-删--deleteBeans--删除多个实体*/
    @Override
    Map<String, Object> delBeans_impl(String authority, String userid, List<?> ids) {
        Map<String, Object> map = new HashMap<>();
        switch (authority) {
            case AdminAuthority -> {
                map.put("result", majorService.deleteBeans(ids));
            }
            default -> {
                map.put("result", "NoAuth");
            }
        }
        return map;
    }

    @Override
    @RequestMapping(value = "/majors", method = RequestMethod.DELETE)
    public ResponseEntity<Map<String, Object>> delBeans(@RequestHeader(value = "Authentication") String authentication,
                                                        @RequestBody JSONArray jsonArray, Class<?> clazz) {
        return super.delBeans(authentication, jsonArray, String.class);
    }
}
