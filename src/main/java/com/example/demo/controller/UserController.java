package com.example.demo.controller;

import com.alibaba.fastjson.JSONArray;
import com.example.demo.utils.BeanTools;
import com.example.demo.utils.JWTUtils;
import com.example.demo.bean.User;
import com.example.demo.service.GeneralService;
import com.example.demo.service.UserSpecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.example.demo.utils.JWTUtils.*;

@RestController
@CrossOrigin("http://localhost:3000")
public class UserController extends BasicController <User>{
    private final UserSpecService userSpecService;
    private final GeneralService<User> userService;

    @Autowired
    public UserController(UserSpecService userSpecService, GeneralService<User> userService) {
        this.userSpecService = userSpecService;
        this.userService = userService;
        userSpecService.createAdmin();
    }

    @Override
    String getIds() {
        return "userids";
    }

    @Override
    String getBean() {
        return "user";
    }

    @Override
    String getBeans() {
        return "users";
    }



    /***********************************************/

    /* 1-查--getAllIds--获取所有id*/
    @Override
    Map<String, Object> getAllIds_impl(String authority, String userid) {
        Map<String, Object> map = new HashMap<>();
        switch (authority) {
            case AdminAuthority, TeacherAuthority, StudentAuthority -> {
                map.put("result", "Success");
                map.put(getIds(), userService.getAllIds());
            }
            default -> {
                map.put("result", "NoAuth");
            }
        }
        return map;
    }

    @Override
    @RequestMapping(value = "/users/userids", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getAllIds(@RequestHeader(value = "Authentication") String authentication) {
        return super.getAllIds(authentication);
    }

    /* 2-查--getABean--获取一个实体*/
    @Override
    Map<String, Object> getABean_impl(String authority, String userid, String key) {
        Map<String, Object> map = new HashMap<>();

        switch (authority) {
            case AdminAuthority -> {
                if (userService.getABean(key) != null) {
                    map.put("result", "Success");
                    map.put(getBean(), userService.getABean(key));
                } else {
                    map.put("result", "NotFound");
                }
            }
            case StudentAuthority, TeacherAuthority ->{
                if (userService.getABean(key) != null && userid.equals(key)) {
                    map.put("result", "Success");
                    map.put(getBean(), userService.getABean(key));
                } else {
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
    @RequestMapping(value = "/user/{userid}", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getABean(@RequestHeader(value = "Authentication") String authentication,
                                                        @PathVariable("userid") String key) {
        return super.getABean(authentication, key);
    }

    /* 3-查--getAllBeans--获取全部实体*/
    @Override
    Map<String, Object> getAllBeans_impl(String authority, String userid) {
        Map<String, Object> map = new HashMap<>();

        switch (authority) {
            case AdminAuthority -> {
                map.put(getBeans(), userService.getAllBeans());
                map.put("result", "Success");
            }
            default -> {
                map.put("result", "NoAuth");
            }
        }
        return map;
    }

    @Override
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getAllBeans(@RequestHeader(value = "Authentication") String authentication) {
        return super.getAllBeans(authentication);
    }

    /* 4-增--createABean--新增一个实体*/
    @Override
    Map<String, Object> createABean_impl(String authority, String userid, String key, User bean) {
        Map<String, Object> map = new HashMap<>();
        switch (authority) {
            case AdminAuthority -> {
                map.put("result", userService.createABean(key, bean));
            }
            default -> {
                map.put("result", "NoAuth");
            }
        }
        return map;
    }

    @Override
    @RequestMapping(value = "/user/{userid}", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> createABean(@RequestHeader(value = "Authentication") String authentication,
                                                           @PathVariable("userid") String key,
                                                           @RequestBody User user) {
        return super.createABean(authentication, key, user);
    }

    /* 5-增--createBeans--新增多个实体*/
    @Override
    Map<String, Object> createBeans_impl(String authority, String userid, List<User> beans) {
        Map<String, Object> map = new HashMap<>();
        switch (authority) {
            case AdminAuthority -> {
                map.put("result", userService.createBeans(beans));
            }
            default -> {
                map.put("result", "NoAuth");
            }
        }
        return map;
    }

    @Override
    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> createBeans(@RequestHeader(value = "Authentication") String authentication,
                                                           @RequestBody JSONArray jsonArray, Class<User> clazz) {
        return super.createBeans(authentication, jsonArray, clazz);
    }

    /* 6-改--rewriteABean--重写一个实体,put*/
    @Override
    Map<String, Object> rewriteABean_impl(String authority, String userid, String key, User bean) {
        Map<String, Object> map = new HashMap<>();
        switch (authority) {
            case AdminAuthority -> {
                map.put("result", userService.changeABean(key, bean));
                return map;
            }
            default -> {
                map.put("result", "NoAuth");
            }
        }
        return map;

    }

    @Override
    @RequestMapping(value = "/user/{userid}", method = RequestMethod.PUT)
    public ResponseEntity<Map<String, Object>> rewriteABean(@RequestHeader(value = "Authentication") String authentication,
                                                            @PathVariable("userid") String key,
                                                            @RequestBody User user) {
        return super.rewriteABean(authentication, key, user);
    }

    /* 7-改--modifyABean--修改一个实体,patch*/
    @Override
    Map<String, Object> modifyABean_impl(String authority, String userid, String key, User user) {
        Map<String, Object> map = new HashMap<>();
        switch (authority) {
            case AdminAuthority -> {
                User user_ori = userService.getABean(key);
                if (user_ori == null) {
                    map.put("result", "NotFound");
                    return map;
                }
                map.put("result", "Success");
                String[] adminauth = {"email", "password", "phonenumber", "name", "school", "major", "status","grade"};

                List<String> changeableList = new ArrayList<>(Arrays.asList(adminauth));
                User user_modified = BeanTools.modify(user_ori, user, changeableList);
                map.put("result", userService.changeABean(userid, user_modified));
                return map;
            }
            case TeacherAuthority, StudentAuthority -> {
                User user_ori = userService.getABean(key);
                if (user_ori == null || !user_ori.getUserid().equals(userid)) {
                    map.put("result", "NotFound");
                    return map;
                }
                map.put("result", "Success");
                String[] standard = {"email", "password", "phonenumber"};

                List<String> changeableList = new ArrayList<>(Arrays.asList(standard));
                User user_modified = BeanTools.modify(user_ori, user, changeableList);
                map.put("result", userService.changeABean(userid, user_modified));
                return map;
            }
            default -> {
                map.put("result", "NoAuth");
                return map;
            }
        }
    }

    @Override
    @RequestMapping(value = "/user/{userid}", method = RequestMethod.PATCH)
    public ResponseEntity<Map<String, Object>> modifyABean(@RequestHeader(value = "Authentication") String authentication,
                                                           @PathVariable("userid") String key,
                                                           @RequestBody User user) {
        return super.modifyABean(authentication, key, user);
    }

    /* 8-删--deleteABean--删除一个实体*/
    @Override
    Map<String, Object> delBean_impl(String authority, String userid, String key) {
        Map<String, Object> map = new HashMap<>();
        switch (authority) {
            case AdminAuthority -> {
                map.put("result", userService.deleteABean(key));
            }
            default -> {
                map.put("result", "NoAuth");
            }
        }
        return map;
    }

    @Override
    @RequestMapping(value = "/user/{userid}", method = RequestMethod.DELETE)
    public ResponseEntity<Map<String, Object>> delBean(@RequestHeader(value = "Authentication") String authentication,
                                                       @PathVariable("userid") String key) {
        return super.delBean(authentication, key);
    }

    /* 9-删--deleteBeans--删除多个实体*/
    @Override
    Map<String, Object> delBeans_impl(String authority, String userid, List<?> ids) {
        Map<String, Object> map = new HashMap<>();
        switch (authority) {
            case AdminAuthority -> {
                map.put("result", userService.deleteBeans(ids));
            }
            default -> {
                map.put("result", "NoAuth");
            }
        }
        return map;
    }

    @Override
    @RequestMapping(value = "/users", method = RequestMethod.DELETE)
    public ResponseEntity<Map<String, Object>> delBeans(@RequestHeader(value = "Authentication") String authentication,
                                                        @RequestBody JSONArray jsonArray, Class<?> clazz) {
        return super.delBeans(authentication, jsonArray, String.class);
    }

    /**************特有操作***************/

    /*!!!查==登录操作*/
    @RequestMapping(value = "/token", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> login(@RequestBody User user) {
        String userid, password;

        userid = user.getUserid();
        password = user.getPassword();

        Map<String, Object> map = new HashMap<>();

        User userBean = userSpecService.login(userid, password);

        if (userBean != null && userBean.getStatus().toString().equals("enabled")) {//success
            String token;
            Map<String, String> payload = new HashMap<>();//自定义payload
            payload.put("userid", userBean.getUserid());//学号
            payload.put("name", userBean.getName());//姓名
            payload.put("usertype", userBean.getUsertype().toString());//用户角色
            if (Objects.equals(password, "fDu666666"))
                payload.put("initialUser", "true");

            token = JWTUtils.generateToken(payload);

            map.put("jwt", token);

            //返回Map名值对--json形式
            System.out.println("Successfully login! Token is sent out.");
            map.put("message", "Successfully login!");
            return new ResponseEntity<>(map, HttpStatus.OK);
        } else {//failure

            map.put("message", "Login failed due to wrong userid or password.");
            System.out.println("Login failed due to wrong userid or password");
            return new ResponseEntity<>(map, HttpStatus.FORBIDDEN);
        }

    }


}
