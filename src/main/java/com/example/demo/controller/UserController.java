package com.example.demo.controller;

import com.example.demo.bean.BeanTools;
import com.example.demo.bean.JWTUtils;
import com.example.demo.bean.UserBean;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.example.demo.bean.JWTUtils.*;

@RestController
@CrossOrigin("http://localhost:3000")
public class UserController extends BasicController <UserBean>{
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
        userService.createAdmin();
    }


    @Override
    String getANewConcreteId() {
        return null;
    }

    @Override
    String getId() {
        return "schoolnumber";
    }

    @Override
    String getIds() {
        return "schoolnumbers";
    }

    @Override
    String getBean() {
        return "userBean";
    }

    /***************未使用的抽象方法******************/

    @Override
    Map<String, Object> getANewId_impl(String auth) {
        return null;
    }


    @Override
    List<String> getAllConcreteIds(Boolean showall, String name) {
        return null;
    }



    /**************继承操作***************/

    /*查--获取一个用户信息*/
    @Override
    UserBean getConcreteBean(String id, Boolean showall) {//管理员和学生权限操作
        return userService.getAUser(id);
    }
    @Override
    UserBean getConcreteBean(String id, Boolean showall, String name) {//教师权限操作
        return userService.getAUser(id);
    }

    @Override
    Map<String, Object> getABean_impl(String authority, String id, String name) {
        Map<String, Object> map = new HashMap<>();

        switch (authority){
            case AdminAuthority, StudentAuthority, TeacherAuthority ->{
                map.put("result", "Success");
                map.put(getBean() ,userService.getAUser(id));
            }
            default -> {
                map.put("result", "NoAuth");
            }
        }
        return map;
    }

    @Override
    @RequestMapping(value="/user/{schoolnumber}", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getABean(@PathVariable("schoolnumber") String schoolnumber,
                                                        @RequestHeader(value="Authentication") String authentication) {
        return super.getABean(schoolnumber, authentication);
    }

    /*查--返回所有主键值*/
    @Override
    List<String> getAllConcreteIds(Boolean showall) {

        return null;
    }

    @Override
    Map<String, Object> getAllIds_impl(String authority, String name) {//权限控制待商榷！
        Map<String, Object> map = new HashMap<>();
        switch (authority){
            case AdminAuthority, TeacherAuthority, StudentAuthority ->{
                map.put("result", "Success");
                map.put(getIds() ,userService.getAllSchoolnumbers());
            }
            default -> {
                map.put("result", "NoAuth");
            }
        }
        return map;

    }

    @Override
    @RequestMapping(value="/users", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getAllIds(@RequestHeader(value="Authentication") String authentication) {
        return super.getAllIds(authentication);
    }


    /*增--新增一个用户*/

    @Override
    String createAConcreteBean(String id, UserBean bean) {
        return userService.createAUser(bean);
    }

    @Override
    String createAConcreteBean(String id, UserBean bean, String name) {
        return null;
    }

    @Override
    Map<String, Object> createABean_impl(String authority, String id, UserBean bean, String name) {
        Map<String, Object> map = new HashMap<>();
        switch (authority){
            case AdminAuthority->{
                map.put("result", userService.createAUser(bean));
            }
            default -> {
                map.put("result", "NoAuth");
            }
        }
        return map;

    }

    @Override
    @RequestMapping(value="/user/{schoolnumber}", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> createABean(@PathVariable("schoolnumber") String schoolnumber,
                                                           @RequestBody UserBean userBean,
                                                           @RequestHeader(value = "Authentication") String authentication) {
        return super.createABean(schoolnumber, userBean, authentication);
    }


    /*改--重写一个用户,put*/

    @Override
    String rewriteConcreteBean(String id, UserBean bean) {
        return null;
    }

    @Override
    Map<String, Object> rewriteABean_impl(String authority, String schoolnumber, UserBean userBean) {//权限待商榷
        Map<String, Object> map = new HashMap<>();
        switch (authority){
            case AdminAuthority->{
                UserBean userBean_ori = userService.getAUser(schoolnumber);
                if (userBean_ori == null){
                    map.put("result", "NotFound");
                    return map;
                }
                else if (!Objects.equals(schoolnumber, userBean.getSchoolnumber())){
                    map.put("result", "FormError");
                    return map;
                }
                map.put("result", userService.rewriteUser(userBean));
                return map;
            }
            default -> {
                map.put("result", "NoAuth");
            }
        }
        return map;

    }


    @Override
    @RequestMapping(value="/user/{schoolnumber}", method = RequestMethod.PUT)
    public ResponseEntity<Map<String, Object>> rewriteABean(@PathVariable("schoolnumber") String schoolnumber,
                                                            @RequestBody UserBean userBean,
                                                            @RequestHeader(value="Authentication") String authentication) {
        return super.rewriteABean(schoolnumber, userBean, authentication);
    }

    /*改--修改一个用户，patch*/
    @Override
    String modifyAConcreteBean(String schoolnumber, UserBean userBean) {//admin
        UserBean userBean_ori = userService.getAUser(schoolnumber);
        if (userBean_ori == null)
            return "NotFound";

        String [] adminauth = {"email", "password", "phonenumber", "name", "school", "major", "status"};

        List<String> changeableList = new ArrayList<>(Arrays.asList(adminauth));
        UserBean userBean_modified = BeanTools.modify(userBean_ori, userBean, changeableList);
        return userService.rewriteUser(userBean_modified);

    }

    @Override
    String modifyAConcreteBean(String schoolnumber, UserBean userBean, String name) {//teacher and student

        UserBean userBean_ori = userService.getAUser(schoolnumber);
        if (userBean_ori == null)
            return "NotFound";
        if (!userBean_ori.getSchoolnumber().equals(schoolnumber)){
            return "NotFound";
        }
        System.out.println("2");
        String [] standard = {"email", "password", "phonenumber"};

        List<String> changeableList = new ArrayList<>(Arrays.asList(standard));
        UserBean userBean_modified = BeanTools.modify(userBean_ori, userBean, changeableList);
        System.out.println("3");
        return userService.rewriteUser(userBean_modified);

    }

    @Override
    Map<String, Object> modifyABean_impl(String authority, String schoolnumber, UserBean userBean) {
        Map<String, Object> map = new HashMap<>();
        switch (authority){
            case AdminAuthority->{
                UserBean userBean_ori = userService.getAUser(schoolnumber);
                if (userBean_ori == null){
                    map.put("result", "NotFound");
                    return map;
                }
                map.put("result", "Success");
                String [] adminauth = {"email", "password", "phonenumber", "name", "school", "major", "status"};

                List<String> changeableList = new ArrayList<>(Arrays.asList(adminauth));
                UserBean userBean_modified = BeanTools.modify(userBean_ori, userBean, changeableList);
                map.put("result", userService.rewriteUser(userBean_modified));
                return map;
            }
            case TeacherAuthority, StudentAuthority->{
                UserBean userBean_ori = userService.getAUser(schoolnumber);
                if (userBean_ori == null || !userBean_ori.getSchoolnumber().equals(schoolnumber)){
                    map.put("result", "NotFound");
                    return map;
                }
                map.put("result", "Success");
                String [] standard = {"email", "password", "phonenumber"};

                List<String> changeableList = new ArrayList<>(Arrays.asList(standard));
                UserBean userBean_modified = BeanTools.modify(userBean_ori, userBean, changeableList);
                map.put("result", userService.rewriteUser(userBean_modified));
                return map;
            }
            default -> {
                map.put("result", "NoAuth");
                return map;
            }
        }
    }

    @Override
    @RequestMapping(value="/user/{schoolnumber}", method = RequestMethod.PATCH)
    public ResponseEntity<Map<String, Object>> modifyABean(@PathVariable("schoolnumber") String schoolnumber,
                                                           @RequestBody UserBean userBean,
                                                           @RequestHeader(value="Authentication") String authentication) {
        return super.modifyABean(schoolnumber, userBean, authentication);
    }


    /*删--删除用户*/
    @Override
    String delConcreteBean(String keyword) {
        return userService.deleteUser(keyword);
    }

    @Override
    String delConcreteBean(String keyword, String name) {
        return null;
    }

    @Override
    Map<String, Object> delBean_impl(String authority, String keyword, String name) {
        Map<String, Object> map = new HashMap<>();
        switch (authority){
            case AdminAuthority->{
                map.put("result", userService.deleteUser(keyword));

            }
            default -> {
                map.put("result", "NoAuth");
            }
        }
        return map;
    }

    @Override
    @RequestMapping(value="/user/{schoolnumber}", method = RequestMethod.DELETE)
    public ResponseEntity<Map<String, Object>> delBean(@PathVariable("schoolnumber") String schoolnumber,
                                                       @RequestHeader(value="Authentication") String authentication) {
        return super.delBean(schoolnumber, authentication);
    }

    /**************特有操作***************/

    /*!!!查==登录操作*/
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



}
