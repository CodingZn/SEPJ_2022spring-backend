package com.example.demo.controller;

import com.example.demo.bean.JWTUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static com.example.demo.bean.JWTUtils.*;


//Control层，与前端联系
@RestController
@CrossOrigin("http://localhost:3000")
public abstract class BasicController <T>{

    /* 根据 Controller 层共同属性控制出来的抽象类，
    * 包括了基本的增删改查操作，与前端抽象类保持一致
    * 其中的抽象方法即为各类间的不同之处
    * 抽象方法通过传入权限字段来控制下层操作 */

    /*查--获取新 lessonid */
    abstract String getANewConcreteId();

    /*************获取字段的抽象方法***************/
    abstract String getId();
    abstract String getIds();
    abstract String getBean();

    /*查--获取新id*/
    abstract Map<String, Object> getANewId_impl(String authority);

    public ResponseEntity<Map<String, Object>> getANewId(@RequestHeader(value="Authentication") String authentication){
        Map<String,Object> map = new HashMap<>();

        String credit = ControllerOperation.checkAuthentication(authentication);
        String authority = ControllerOperation.getAuthority(authentication);

        if (credit.equals(ValidJWTToken)){
            map = getANewId_impl(authority);
            String result = (String) map.get("result");
            map.remove("result");//不要返回额外的信息
            return ControllerOperation.getConductResponse(result, map);
        }
        else
            return ControllerOperation.getErrorResponse(credit, map);
//
//        if (credit.equals(auth1())){//一定成功，不做封装
//            String newConcreteId = getANewConcreteId();
//
//            map.put(getId(), newConcreteId);
//            return new ResponseEntity<>(map, HttpStatus.OK);
//        }
//        else if (credit.equals(auth2())){
//            String newConcreteId = getANewConcreteId();
//
//            map.put(getId(), newConcreteId);
//            return new ResponseEntity<>(map, HttpStatus.OK);
//        }
//        else if (credit.equals(auth3())){
//            return ControllerOperation.getErrorResponse(credit, map);
//        }
//        else {
//            return ControllerOperation.getErrorResponse(credit, map);
//        }


    }

    /*查--返回所有id值，id值的身份由被调用函数确定*/
    abstract List<String> getAllConcreteIds(Boolean showall);
    abstract List<String> getAllConcreteIds(Boolean showall, String name);

    abstract Map<String, Object> getAllIds_impl(String authority, String name);

    public ResponseEntity<Map<String, Object>> getAllIds(String authentication){
        Map<String, Object> map = new HashMap<>();

        String credit = ControllerOperation.checkAuthentication(authentication);
        String authority = ControllerOperation.getAuthority(authentication);
        String name = JWTUtils.decodeToGetValue(authentication.substring(7), "name");

        if (credit.equals(ValidJWTToken)){
            map = getAllIds_impl(authority, name);
            String result = (String) map.get("result");
            map.remove("result");//不要返回额外的信息
            return ControllerOperation.getConductResponse(result, map);
        }
        else
            return ControllerOperation.getErrorResponse(credit, map);


//        if (credit.equals(auth1())){//必然成功，未重构
//            List<String> strings = getAllConcreteIds(true);
//
//            System.out.println("strings="+strings);
//            map.put(getIds(),strings);//change
//            return new ResponseEntity<>(map, HttpStatus.OK);
//        }
//        else if (credit.equals(auth2())){
//            List<String> strings = getAllConcreteIds(false, name);
//            System.out.println("strings="+strings);
//            map.put(getIds(),strings);//change
//            return new ResponseEntity<>(map, HttpStatus.OK);
//        }
//        else if (credit.equals(auth3())){
//            List<String> strings = getAllConcreteIds(false);
//            System.out.println("strings="+strings);
//            map.put(getIds(),strings);//change
//            return new ResponseEntity<>(map, HttpStatus.OK);
//        }
//        else{
//            return ControllerOperation.getErrorResponse(credit, map);
//        }

    }

    /*查--获取一个实体信息*/
    abstract T getConcreteBean(String id, Boolean showall);
    abstract T getConcreteBean(String id, Boolean showall, String name);

    abstract Map<String, Object> getABean_impl(String authority, String id, String name);

    public ResponseEntity<Map<String, Object>>getABean(String keyword, String authentication) {
        Map<String, Object> map = new HashMap<>();

        String credit = ControllerOperation.checkAuthentication(authentication);
        String authority = ControllerOperation.getAuthority(authentication);
        String name = JWTUtils.decodeToGetValue(authentication.substring(7), "name");

        if (credit.equals(ValidJWTToken)){
            map = getABean_impl(authority, keyword, name);
            String result = (String) map.get("result");
            map.remove("result");//不要返回额外的信息
            return ControllerOperation.getConductResponse(result, map);
        }
        else
            return ControllerOperation.getErrorResponse(credit, map);
//        if (credit.equals(auth1())){
//            T entity = getConcreteBean(keyword, true);
//
//            return ControllerOperation.getSearchResponse(entity, map);
//        }
//        else if (credit.equals(auth2())){
//            T entity = getConcreteBean(keyword, false, name);
//            return ControllerOperation.getSearchResponse(entity, map);
//        }
//        else if (credit.equals(auth3())){
//            T entity = getConcreteBean(keyword, false);
//            return ControllerOperation.getSearchResponse(entity, map);
//        }
//        else{
//            return ControllerOperation.getErrorResponse(credit, map);
//        }
    }

    /*增--新增一个实体,post*/

    abstract String createAConcreteBean(String id, T bean);
    abstract String createAConcreteBean(String id, T bean, String name);

    abstract Map<String, Object> createABean_impl (String authority, String id, T bean, String name);

    public ResponseEntity<Map<String, Object>> createABean(String id, T bean, String authentication){
        Map<String, Object> map = new HashMap<>();

        String credit = ControllerOperation.checkAuthentication(authentication);
        String authority = ControllerOperation.getAuthority(authentication);
        String name = JWTUtils.decodeToGetValue(authentication.substring(7), "name");

        if (credit.equals(ValidJWTToken)){
            map = createABean_impl (authority, id, bean, name);
            String result = (String) map.get("result");
            map.remove("result");//不要返回额外的信息
            return ControllerOperation.getConductResponse(result, map);
        }
        else
            return ControllerOperation.getErrorResponse(credit, map);

//        if (credit.equals(auth1())){
//
//            String result = createAConcreteBean(id, bean);
//
//            return ControllerOperation.getConductResponse(result, map);
//
//        }
//        else if (credit.equals(auth2())){
//            String result = createAConcreteBean(id, bean, name);
//
//            return ControllerOperation.getConductResponse(result, map);
//
//        }
//        else if (credit.equals(auth3())){
//            return ControllerOperation.getErrorResponse(credit, map);
//        }
//        else{
//            return ControllerOperation.getErrorResponse(credit, map);
//        }
    }

    /*改--重写一个实体,put*/
    abstract String rewriteConcreteBean(String id, T bean);

    abstract Map<String, Object> rewriteABean_impl(String authority, String id, T bean);

    public ResponseEntity<Map<String, Object>> rewriteABean(String keyword, T bean, String authentication) {
        Map<String, Object> map = new HashMap<>();

        String credit = ControllerOperation.checkAuthentication(authentication);
        String authority = ControllerOperation.getAuthority(authentication);

        if (credit.equals(ValidJWTToken)){
            map = modifyABean_impl(authority, keyword, bean);
            String result = (String) map.get("result");
            map.remove("result");//不要返回额外的信息
            return ControllerOperation.getConductResponse(result, map);
        }
        else
            return ControllerOperation.getErrorResponse(credit, map);

//        if (credit.equals(auth1())){
//
//            String result = modifyAConcreteBean(id, bean);
//
//            return ControllerOperation.getConductResponse(result, map);
//
//        }
//        else if (credit.equals(auth2())){
//            return ControllerOperation.getErrorResponse(credit, map);
//        }
//        else if (credit.equals(auth3())){
//            return ControllerOperation.getErrorResponse(credit, map);
//        }
//        else{
//            return ControllerOperation.getErrorResponse(credit, map);
//        }


    }

    /*改--修改一个实体, patch */
    abstract String modifyAConcreteBean(String id, T bean);

    abstract String modifyAConcreteBean(String id, T bean, String name);

    abstract Map<String, Object> modifyABean_impl (String authority, String id, T bean);

    public ResponseEntity<Map<String, Object>> modifyABean(String keyword, T bean, String authentication) {
        Map<String, Object> map = new HashMap<>();

        String credit = ControllerOperation.checkAuthentication(authentication);
        String authority = ControllerOperation.getAuthority(authentication);
        String name = JWTUtils.decodeToGetValue(authentication.substring(7), "name");

        if (credit.equals(ValidJWTToken)){
            map = modifyABean_impl(authority, keyword, bean);
            String result = (String) map.get("result");
            map.remove("result");//不要返回额外的信息
            return ControllerOperation.getConductResponse(result, map);
        }
        else
            return ControllerOperation.getErrorResponse(credit, map);

//        if (credit.equals(auth1())){
//
//            String result = modifyAConcreteBean(id, bean);
//
//            return ControllerOperation.getConductResponse(result, map);
//
//        }
//        else if (credit.equals(auth2())){
//            String result = modifyAConcreteBean(id, bean, name);
//
//            return ControllerOperation.getConductResponse(result, map);
//        }
//        else if (credit.equals(auth3())){
//            String result = modifyAConcreteBean(id, bean, name);
//
//            return ControllerOperation.getConductResponse(result, map);
//        }
//        else{
//            return ControllerOperation.getErrorResponse(credit, map);
//        }

    }

    /*删--删除实体*/

    abstract String delConcreteBean(String keyword);

    abstract String delConcreteBean(String keyword, String name);

    abstract Map<String, Object> delBean_impl(String authority, String keyword, String name);

    public ResponseEntity<Map<String, Object>> delBean(String keyword, String authentication){
        Map<String, Object> map = new HashMap<>();

        String credit = ControllerOperation.checkAuthentication(authentication);
        String authority = ControllerOperation.getAuthority(authentication);
        String name = JWTUtils.decodeToGetValue(authentication.substring(7), "name");

        if (credit.equals(ValidJWTToken)){
            map = delBean_impl(authority, keyword, name);
            String result = (String) map.get("result");
            map.remove("result");
            return ControllerOperation.getConductResponse(result, map);
        }
        else
            return ControllerOperation.getErrorResponse(credit, map);

//        if (credit.equals(auth1())){
//            String result = delConcreteBean(keyword);
//            return ControllerOperation.getConductResponse(result, map);
//        }
//        else if (credit.equals(auth2())){
//            String result = delConcreteBean(keyword, name);
//            return ControllerOperation.getConductResponse(result, map);
//        }
//        else if (credit.equals(auth3())){
//            return ControllerOperation.getErrorResponse(credit, map);
//        }
//        else{
//            return ControllerOperation.getErrorResponse(credit, map);
//        }

    }


}
