package com.example.demo.controller;

import com.alibaba.fastjson.JSONArray;
import com.example.demo.bean.BeanTools;
import com.example.demo.bean.JWTUtils;
import com.example.demo.bean.User;
import com.example.demo.service.GeneralService;
import com.example.demo.service.UserSpecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.example.demo.bean.JWTUtils.*;

@RestController
@CrossOrigin("http://localhost:3000")
public class UserController extends BasicController<User> {
    private final UserSpecService userSpecService;
    private final GeneralService<User> userService;

    @Autowired
    public UserController(UserSpecService userSpecService, GeneralService<User> userService) {
        this.userSpecService = userSpecService;
        this.userService = userService;
        userSpecService.createAdmin();
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
        return "user";
    }

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

    /***********************************************/

    /*查--获取所有id*/
    @Override
    Map<String, Object> getAllIds_impl(String authority, String name) {//权限控制待商榷！
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
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getAllIds(@RequestHeader(value = "Authentication") String authentication) {
        return super.getAllIds(authentication);
    }

    /*查--获取一个实体*/
    @Override
    Map<String, Object> getABean_impl(String authority, String id, String name) {
        Map<String, Object> map = new HashMap<>();

        switch (authority) {
            case AdminAuthority, StudentAuthority, TeacherAuthority -> {
                if (userService.getABean(id) != null) {
                    map.put("result", "Success");
                    map.put(getBean(), userService.getABean(id));
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
    @RequestMapping(value = "/user/{schoolnumber}", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getABean(@PathVariable("schoolnumber") String schoolnumber,
                                                        @RequestHeader(value = "Authentication") String authentication) {
        return super.getABean(schoolnumber, authentication);
    }

    /*增--新增一个用户*/
    @Override
    Map<String, Object> createABean_impl(String authority, String id, User bean, String name) {
        Map<String, Object> map = new HashMap<>();
        switch (authority) {
            case AdminAuthority -> {
                map.put("result", userService.createABean(id, bean));
            }
            default -> {
                map.put("result", "NoAuth");
            }
        }
        return map;
    }

    @Override
    @RequestMapping(value = "/user/{schoolnumber}", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> createABean(@PathVariable("schoolnumber") String schoolnumber,
                                                           @RequestBody User user,
                                                           @RequestHeader(value = "Authentication") String authentication) {
        return super.createABean(schoolnumber, user, authentication);
    }

    /*改--重写一个用户,put*/
    @Override
    Map<String, Object> rewriteABean_impl(String authority, String schoolnumber, User user) {//权限待商榷
        Map<String, Object> map = new HashMap<>();
        switch (authority) {
            case AdminAuthority -> {
                User user_ori = userService.getABean(schoolnumber);
                if (user_ori == null) {
                    map.put("result", "NotFound");
                    return map;
                } else if (!Objects.equals(schoolnumber, user.getSchoolnumber())) {
                    map.put("result", "FormError");
                    return map;
                }
                map.put("result", userService.changeABean(schoolnumber, user));
                return map;
            }
            default -> {
                map.put("result", "NoAuth");
            }
        }
        return map;

    }

    @Override
    @RequestMapping(value = "/user/{schoolnumber}", method = RequestMethod.PUT)
    public ResponseEntity<Map<String, Object>> rewriteABean(@PathVariable("schoolnumber") String schoolnumber,
                                                            @RequestBody User user,
                                                            @RequestHeader(value = "Authentication") String authentication) {
        return super.rewriteABean(schoolnumber, user, authentication);
    }

    /*改--重写一个实体*/
    @Override
    Map<String, Object> modifyABean_impl(String authority, String schoolnumber, User user) {
        Map<String, Object> map = new HashMap<>();
        switch (authority) {
            case AdminAuthority -> {
                User user_ori = userService.getABean(schoolnumber);
                if (user_ori == null) {
                    map.put("result", "NotFound");
                    return map;
                }
                map.put("result", "Success");
                String[] adminauth = {"email", "password", "phonenumber", "name", "school", "major", "status"};

                List<String> changeableList = new ArrayList<>(Arrays.asList(adminauth));
                User user_modified = BeanTools.modify(user_ori, user, changeableList);
                map.put("result", userService.changeABean(schoolnumber, user_modified));
                return map;
            }
            case TeacherAuthority, StudentAuthority -> {
                User user_ori = userService.getABean(schoolnumber);
                if (user_ori == null || !user_ori.getSchoolnumber().equals(schoolnumber)) {
                    map.put("result", "NotFound");
                    return map;
                }
                map.put("result", "Success");
                String[] standard = {"email", "password", "phonenumber"};

                List<String> changeableList = new ArrayList<>(Arrays.asList(standard));
                User user_modified = BeanTools.modify(user_ori, user, changeableList);
                map.put("result", userService.changeABean(schoolnumber, user_modified));
                return map;
            }
            default -> {
                map.put("result", "NoAuth");
                return map;
            }
        }
    }

    @Override
    @RequestMapping(value = "/user/{schoolnumber}", method = RequestMethod.PATCH)
    public ResponseEntity<Map<String, Object>> modifyABean(@PathVariable("schoolnumber") String schoolnumber,
                                                           @RequestBody User user,
                                                           @RequestHeader(value = "Authentication") String authentication) {
        return super.modifyABean(schoolnumber, user, authentication);
    }

    /*删--删除一个实体*/
    @Override
    Map<String, Object> delBean_impl(String authority, String keyword, String name) {
        Map<String, Object> map = new HashMap<>();
        switch (authority) {
            case AdminAuthority -> {
                map.put("result", userService.deleteABean(keyword));

            }
            default -> {
                map.put("result", "NoAuth");
            }
        }
        return map;
    }

    @Override
    @RequestMapping(value = "/user/{schoolnumber}", method = RequestMethod.DELETE)
    public ResponseEntity<Map<String, Object>> delBean(@PathVariable("schoolnumber") String schoolnumber,
                                                       @RequestHeader(value = "Authentication") String authentication) {
        return super.delBean(schoolnumber, authentication);
    }


    /**************特有操作***************/

    /*!!!查==登录操作*/
    @RequestMapping(value = "/token", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> login(@RequestBody User user) {
        String schoolnumber, password;

        schoolnumber = user.getSchoolnumber();
        password = user.getPassword();

        Map<String, Object> map = new HashMap<>();

        User userBean = userSpecService.login(schoolnumber, password);

        if (userBean != null && userBean.getStatus().equals("enabled")) {//success
            String token;
            Map<String, String> payload = new HashMap<>();//自定义payload
            payload.put("schoolnumber", userBean.getSchoolnumber());//学号
            payload.put("name", userBean.getName());//姓名
            payload.put("usertype", userBean.getUsertype());//用户角色
            if (Objects.equals(password, "fDu666666"))
                payload.put("initialUser", "true");

            token = JWTUtils.generateToken(payload);

            map.put("jwt", token);

            //返回Map名值对--json形式
            System.out.println("Successfully login! Token is sent out.");
            map.put("message", "Successfully login!");
            return new ResponseEntity<>(map, HttpStatus.OK);
        } else {//failure

            map.put("message", "Login failed due to wrong schoolnumber or password.");
            System.out.println("Login failed due to wrong schoolnumber or password");
            return new ResponseEntity<>(map, HttpStatus.FORBIDDEN);
        }

    }


}
