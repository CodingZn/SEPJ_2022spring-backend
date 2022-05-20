package com.example.demo.controller;

import com.alibaba.fastjson.JSONArray;
import com.example.demo.utils.JWTUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static com.example.demo.utils.JWTUtils.*;


//Control层，与前端联系
@RestController
@CrossOrigin("http://121.37.100.255:3000")
public abstract class BasicController <T>{

    /* 根据 Controller 层共同属性控制出来的抽象类，
     * 包括了基本的增删改查操作，与前端抽象类保持一致
     * 其中的抽象方法即为各类间的不同之处
     * 抽象方法通过传入权限字段和用户ID来控制下层操作
     *  */

    abstract String getIds();

    abstract String getBean();

    abstract String getBeans();

    /*
     * 方法规范：
     * 具体方法的参数列表：authentication, [key], [<T> bean], [JsonArray]
     * 抽象方法的参数列表：authority, userid, [key] [<T> bean], [JsonArray]
     * */

    /* 1-查--getAllIds--获取所有id*/
    abstract Map<String, Object> getAllIds_impl(String authority, String userid);

    public ResponseEntity<Map<String, Object>> getAllIds(String authentication) {
        Map<String, Object> map = new HashMap<>();

        String credit = ControllerOperation.checkAuthentication(authentication);
        String authority = ControllerOperation.getAuthority(authentication);
        String userid = JWTUtils.decodeToGetValue(authentication.substring(7), "userid");

        if (credit.equals(ValidJWTToken)) {
            map = getAllIds_impl(authority, userid);
            String result = (String) map.get("result");
            map.remove("result");//不要返回额外的信息
            return ControllerOperation.getConductResponse(result, map);
        } else
            return ControllerOperation.getErrorResponse(credit, map);

    }

    /* 2-查--getABean--获取一个实体*/
    abstract Map<String, Object> getABean_impl(String authority, String userid, String key);

    public ResponseEntity<Map<String, Object>> getABean(String authentication, String key) {
        Map<String, Object> map = new HashMap<>();

        String credit = ControllerOperation.checkAuthentication(authentication);
        String authority = ControllerOperation.getAuthority(authentication);System.out.println(authority);
        String userid = JWTUtils.decodeToGetValue(authentication.substring(7), "userid");System.out.println(userid);

        if (credit.equals(ValidJWTToken)) {
            map = getABean_impl(authority, userid, key);
            String result = (String) map.get("result");
            map.remove("result");//不要返回额外的信息
            return ControllerOperation.getConductResponse(result, map);
        } else
            return ControllerOperation.getErrorResponse(credit, map);

    }

    /* 3-查--getAllBeans--获取全部实体*/
    abstract Map<String, Object> getAllBeans_impl(String authority, String userid);

    public ResponseEntity<Map<String, Object>> getAllBeans(String authentication) {
        Map<String, Object> map = new HashMap<>();

        String credit = ControllerOperation.checkAuthentication(authentication);
        String authority = ControllerOperation.getAuthority(authentication);
        String userid = JWTUtils.decodeToGetValue(authentication.substring(7), "userid");

        if (credit.equals(ValidJWTToken)) {
            map = getAllBeans_impl(authority, userid);
            String result = (String) map.get("result");
            map.remove("result");//不要返回额外的信息
            return ControllerOperation.getConductResponse(result, map);
        } else
            return ControllerOperation.getErrorResponse(credit, map);

    }

    /* 4-增--createABean--新增一个实体*/
    abstract Map<String, Object> createABean_impl(String authority, String userid, T bean);

    public ResponseEntity<Map<String, Object>> createABean(String authentication, T bean) {
        Map<String, Object> map = new HashMap<>();

        String credit = ControllerOperation.checkAuthentication(authentication);
        String authority = ControllerOperation.getAuthority(authentication);
        String userid = JWTUtils.decodeToGetValue(authentication.substring(7), "userid");

        if (credit.equals(ValidJWTToken)) {
            map = createABean_impl(authority, userid, bean);
            String result = (String) map.get("result");
            map.remove("result");//不要返回额外的信息
            return ControllerOperation.getConductResponse(result, map);
        } else
            return ControllerOperation.getErrorResponse(credit, map);

    }

    /* 5-增--createBeans--新增多个实体*/
    abstract Map<String, Object> createBeans_impl(String authority, String userid, List<T> beans);

    public ResponseEntity<Map<String, Object>> createBeans(String authentication, JSONArray jsonArray, Class<T> clazz) {
        Map<String, Object> map = new HashMap<>();

        String credit = ControllerOperation.checkAuthentication(authentication);
        String authority = ControllerOperation.getAuthority(authentication);
        String userid = JWTUtils.decodeToGetValue(authentication.substring(7), "userid");

        if (credit.equals(ValidJWTToken)) {
            List<T> beans = jsonArray.toJavaList(clazz);
            map = createBeans_impl(authority, userid, beans);
            String result = (String) map.get("result");
            map.remove("result");//不要返回额外的信息
            return ControllerOperation.getConductResponse(result, map);
        } else
            return ControllerOperation.getErrorResponse(credit, map);
    }

    /* 6-改--rewriteABean--重写一个实体,put*/
    abstract Map<String, Object> rewriteABean_impl(String authority, String userid, String key, T bean);

    public ResponseEntity<Map<String, Object>> rewriteABean(String authentication, String key, T bean) {
        Map<String, Object> map = new HashMap<>();

        String credit = ControllerOperation.checkAuthentication(authentication);
        String authority = ControllerOperation.getAuthority(authentication);
        String userid = JWTUtils.decodeToGetValue(authentication.substring(7), "userid");

        if (credit.equals(ValidJWTToken)) {
            map = rewriteABean_impl(authority, userid, key, bean);
            String result = (String) map.get("result");
            map.remove("result");//不要返回额外的信息
            return ControllerOperation.getConductResponse(result, map);
        } else
            return ControllerOperation.getErrorResponse(credit, map);

    }

    /* 7-改--modifyABean--修改一个实体,patch*/
    abstract Map<String, Object> modifyABean_impl(String authority, String userid, String key, T bean);

    public ResponseEntity<Map<String, Object>> modifyABean(String authentication, String key, T bean) {
        Map<String, Object> map = new HashMap<>();

        String credit = ControllerOperation.checkAuthentication(authentication);
        String authority = ControllerOperation.getAuthority(authentication);
        String userid = JWTUtils.decodeToGetValue(authentication.substring(7), "userid");

        if (credit.equals(ValidJWTToken)) {
            map = modifyABean_impl(authority, userid, key, bean);
            String result = (String) map.get("result");
            map.remove("result");//不要返回额外的信息
            return ControllerOperation.getConductResponse(result, map);
        } else
            return ControllerOperation.getErrorResponse(credit, map);

    }

    /* 8-删--deleteABean--删除一个实体*/
    abstract Map<String, Object> delBean_impl(String authority, String userid, String key);

    public ResponseEntity<Map<String, Object>> delBean(String authentication, String key) {
        Map<String, Object> map = new HashMap<>();

        String credit = ControllerOperation.checkAuthentication(authentication);
        String authority = ControllerOperation.getAuthority(authentication);
        String userid = JWTUtils.decodeToGetValue(authentication.substring(7), "userid");

        if (credit.equals(ValidJWTToken)) {
            map = delBean_impl(authority, userid, key);
            String result = (String) map.get("result");
            map.remove("result");
            return ControllerOperation.getConductResponse(result, map);
        } else
            return ControllerOperation.getErrorResponse(credit, map);

    }

    /* 9-删--deleteBeans--删除多个实体*/
    abstract Map<String, Object> delBeans_impl(String authority, String userid, List<?> ids);

    public ResponseEntity<Map<String, Object>> delBeans(String authentication, JSONArray jsonArray, Class<?> clazz) {
        Map<String, Object> map = new HashMap<>();

        String credit = ControllerOperation.checkAuthentication(authentication);
        String authority = ControllerOperation.getAuthority(authentication);
        String userid = JWTUtils.decodeToGetValue(authentication.substring(7), "userid");

        if (credit.equals(ValidJWTToken)) {
            List<?> ids = jsonArray.toJavaList(clazz);//int or String
            map = delBeans_impl(authority, userid, ids);
            String result = (String) map.get("result");
            map.remove("result");
            return ControllerOperation.getConductResponse(result, map);
        } else
            return ControllerOperation.getErrorResponse(credit, map);

    }


}
