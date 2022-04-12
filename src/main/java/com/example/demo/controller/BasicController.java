package com.example.demo.controller;

import com.example.demo.bean.JWTUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.util.*;


//Control层，与前端联系
@RestController
@CrossOrigin("http://localhost:3000")
public abstract class BasicController <T>{

    /* 根据 Controller 层共同属性控制出来的抽象类，
    * 包括了基本的增删改查操作，与前端抽象类保持一致
    * 其中的抽象方法即为各类间的不同之处 */

    /* 令牌权限 */
    abstract String auth1();//admin authority

    abstract String auth2();//teacher authority

    abstract String auth3();//student authority


    /* 请求中的相关参数 */

    abstract String getIds();
    abstract String getId();


    /*查--获取新id*/
    abstract String getANewConcreteId();

    public ResponseEntity<Map<String, Object>> getANewId(@RequestHeader(value="Authentication") String authentication){
        Map<String,Object> map = new HashMap<>();

        String credit = ControllerOperation.checkAuthentication(authentication);


        if (credit.equals(auth1())){//一定成功，不做封装
            String newMajornumber = getANewConcreteId();

            map.put(getId(), newMajornumber);
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
        else if (credit.equals(auth2())){
            return ControllerOperation.getErrorResponse(credit, map);
        }
        else if (credit.equals(auth3())){
            return ControllerOperation.getErrorResponse(credit, map);
        }
        else {
            return ControllerOperation.getErrorResponse(credit, map);
        }


    }

    /*查--返回所有id值，id值的身份由被调用函数确定*/
    abstract List<String> getAllConcreteIds(Boolean showall);
    abstract List<String> getAllConcreteIds(Boolean showall, String name);


    public ResponseEntity<Map<String, Object>> getAllIds(String authentication){
        Map<String, Object> map = new HashMap<>();

        String credit = ControllerOperation.checkAuthentication(authentication);
        String name = JWTUtils.decodeToGetValue(authentication.substring(7), "name");

        if (credit.equals(auth1())){//必然成功，未重构
            List<String> strings = getAllConcreteIds(true);

            System.out.println("strings="+strings);
            map.put(getIds(),strings);//change
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
        else if (credit.equals(auth2())){
            List<String> strings = getAllConcreteIds(false, name);
            System.out.println("strings="+strings);
            map.put(getIds(),strings);//change
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
        else if (credit.equals(auth3())){
            List<String> strings = getAllConcreteIds(false);
            System.out.println("strings="+strings);
            map.put(getIds(),strings);//change
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
        else{
            return ControllerOperation.getErrorResponse(credit, map);
        }

    }

    /*查--获取一个实体信息*/
    abstract T getConcreteBean(String id, Boolean showall);

    abstract T getConcreteBean(String id, Boolean showall, String name);

    public ResponseEntity<Map<String, Object>>getABean(String keyword, String authentication) {
        Map<String, Object> map = new HashMap<>();

        String credit = ControllerOperation.checkAuthentication(authentication);
        String name = JWTUtils.decodeToGetValue(authentication.substring(7), "name");
        if (credit.equals(auth1())){
            T entity = getConcreteBean(keyword, true);

            return ControllerOperation.getSearchResponse(entity, map);
        }
        else if (credit.equals(auth2())){
            T entity = getConcreteBean(keyword, false, name);
            return ControllerOperation.getSearchResponse(entity, map);
        }
        else if (credit.equals(auth3())){
            T entity = getConcreteBean(keyword, false);
            return ControllerOperation.getSearchResponse(entity, map);
        }
        else{
            return ControllerOperation.getErrorResponse(credit, map);
        }
    }

    /*增--新增一个实体,post*/

    abstract String createAConcreteBean(String id, T bean);

    abstract String createAConcreteBean(String id, T bean, String name);

    public ResponseEntity<Map<String, Object>> createABean(String id, T bean, String authentication){
        Map<String, Object> map = new HashMap<>();

        String credit = ControllerOperation.checkAuthentication(authentication);
        String name = JWTUtils.decodeToGetValue(authentication.substring(7), "name");

        if (credit.equals(auth1())){

            String result = createAConcreteBean(id, bean);

            return ControllerOperation.getConductResponse(result, map);

        }
        else if (credit.equals(auth2())){
            String result = createAConcreteBean(id, bean, name);

            return ControllerOperation.getConductResponse(result, map);

        }
        else if (credit.equals(auth3())){
            return ControllerOperation.getErrorResponse(credit, map);
        }
        else{
            return ControllerOperation.getErrorResponse(credit, map);
        }
    }

    /*改--重写一个实体,put*/
    abstract String rewriteConcreteBean(String id, T bean);

    public ResponseEntity<Map<String, Object>> rewriteABean(String id, T bean, String authentication) {
        Map<String, Object> map = new HashMap<>();

        String credit = ControllerOperation.checkAuthentication(authentication);

        if (credit.equals(auth1())){

            String result = modifyAConcreteBean(id, bean);

            return ControllerOperation.getConductResponse(result, map);

        }
        else if (credit.equals(auth2())){
            return ControllerOperation.getErrorResponse(credit, map);
        }
        else if (credit.equals(auth3())){
            return ControllerOperation.getErrorResponse(credit, map);
        }
        else{
            return ControllerOperation.getErrorResponse(credit, map);
        }


    }

    /*改--重写一个实体, patch */
    abstract String modifyAConcreteBean(String id, T bean);

    abstract String modifyAConcreteBean(String id, T bean, String name);

    public ResponseEntity<Map<String, Object>> modifyABean(String id, T bean, String authentication) {
        Map<String, Object> map = new HashMap<>();

        String credit = ControllerOperation.checkAuthentication(authentication);
        String name = JWTUtils.decodeToGetValue(authentication.substring(7), "name");
        if (credit.equals(auth1())){

            String result = modifyAConcreteBean(id, bean);

            return ControllerOperation.getConductResponse(result, map);

        }
        else if (credit.equals(auth2())){
            String result = modifyAConcreteBean(id, bean, name);

            return ControllerOperation.getConductResponse(result, map);
        }
        else if (credit.equals(auth3())){
            return ControllerOperation.getErrorResponse(credit, map);
        }
        else{
            return ControllerOperation.getErrorResponse(credit, map);
        }

    }

    /*删--删除实体*/

    abstract String delConcreteBean(String keyword);

    abstract String delConcreteBean(String keyword, String name);

    public ResponseEntity<Map<String, Object>> delBean(String keyword, String authentication){
        Map<String, Object> map = new HashMap<>();

        String credit = ControllerOperation.checkAuthentication(authentication);
        String name = JWTUtils.decodeToGetValue(authentication.substring(7), "name");
        if (credit.equals(auth1())){
            String result = delConcreteBean(keyword);
            return ControllerOperation.getConductResponse(result, map);
        }
        else if (credit.equals(auth2())){
            String result = delConcreteBean(keyword, name);
            return ControllerOperation.getConductResponse(result, map);
        }
        else if (credit.equals(auth3())){
            return ControllerOperation.getErrorResponse(credit, map);
        }
        else{
            return ControllerOperation.getErrorResponse(credit, map);
        }

    }


}
