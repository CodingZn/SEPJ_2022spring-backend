package com.example.demo.controller;

import com.alibaba.fastjson.JSONArray;
import com.example.demo.bean.generators.UseridGenerator;
import com.example.demo.mapper.straightMappers.UltimatecontrolMapper;
import com.example.demo.utils.BeanTools;
import com.example.demo.utils.JWTUtils;
import com.example.demo.bean.User;
import com.example.demo.service.GeneralService;
import com.example.demo.service.UserSpecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

import static com.example.demo.bean.specialBean.Ultimatectrl.*;
import static com.example.demo.utils.JWTUtils.*;

@RestController
@CrossOrigin
public class UserController extends BasicController <User>{
    private final UserSpecService userSpecService;
    private final GeneralService<User> userService;
    private final UltimatecontrolMapper control;

    private static String now_year;

    @Autowired
    public UserController(UserSpecService userSpecService, GeneralService<User> userService, UltimatecontrolMapper control) {
        this.userSpecService = userSpecService;
        this.userService = userService;
        this.control = control;
        userSpecService.createAdmin();
        setGeneratorInit();
    }

    private void setGeneratorInit() {
        List<User> students = userSpecService.getAllByUsertype(User.Type.student);
        List<User> teachers = userSpecService.getAllByUsertype(User.Type.teacher);
        now_year = control.findByName(KEY_YEAR_CONTROL).getValue();
        UseridGenerator.setNow_year(now_year);

        List<Integer> studentIds_tmp = students.stream().map(u -> {
            if(u.getUserid().substring(0,2).equals(now_year))
                return Integer.parseInt(u.getUserid().substring(2));
            else return null;
        }).toList();
        List<Integer> studentIds = new ArrayList<>(studentIds_tmp);
        studentIds.removeIf(Objects::isNull);
        try {
            int a = Collections.max(studentIds);
            UseridGenerator.setNextStudentid(1 + a);

        }catch (Exception e){
            UseridGenerator.setNextStudentid(1);
        }

        List<Integer> teacherIds_tmp = teachers.stream().map(u -> {
            if(u.getUserid().substring(0,2).equals(now_year))
                return Integer.parseInt(u.getUserid().substring(2));
            else return null;
        }).toList();
        List<Integer> teacherIds = new ArrayList<>(teacherIds_tmp);
        teacherIds.removeIf(Objects::isNull);
        try{
            int a = Collections.max(teacherIds);
            UseridGenerator.setNextTeacherid(1+a);

        }catch (Exception e){
            UseridGenerator.setNextTeacherid(1);
        }

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

    /* 1-???--getAllIds--????????????id*/
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

    /* 2-???--getABean--??????????????????*/
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

    /* 3-???--getAllBeans--??????????????????*/
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

    /* 4-???--createABean--??????????????????*/
    @Override
    Map<String, Object> createABean_impl(String authority, String userid, User bean) {
        Map<String, Object> map = new HashMap<>();
        switch (authority) {
            case AdminAuthority -> {
                map.put("result", userService.createABean(bean));
            }
            default -> {
                map.put("result", "NoAuth");
            }
        }
        return map;
    }

    @Override
    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> createABean(@RequestHeader(value = "Authentication") String authentication,
                                                           @RequestBody User user) {
        return super.createABean(authentication, user);
    }

    /* 5-???--createBeans--??????????????????*/
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
        return super.createBeans(authentication, jsonArray, User.class);
    }

    /* 6-???--rewriteABean--??????????????????,put*/
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

    /* 7-???--modifyABean--??????????????????,patch*/
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
                map.put("result", userService.changeABean(key, user_modified));
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
                map.put("result", userService.changeABean(key, user_modified));
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

    /* 8-???--deleteABean--??????????????????*/
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

    /* 9-???--deleteBeans--??????????????????*/
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

    /**************????????????***************/

    /*!!!???==????????????*/
    @RequestMapping(value = "/token", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> login(@RequestBody User user) {
        String userid, password;

        userid = user.getUserid();
        password = user.getPassword();

        Map<String, Object> map = new HashMap<>();

        User userBean = userSpecService.login(userid, password);

        if (userBean != null && userBean.getStatus().equals(User.Status.enabled)) {//success
            String token;
            Map<String, String> payload = new HashMap<>();//?????????payload
            payload.put("userid", userBean.getUserid());//??????
            payload.put("name", userBean.getName());//??????
            payload.put("usertype", userBean.getUsertype().toString());//????????????
            if (Objects.equals(password, "fDu666666"))
                payload.put("initialUser", "true");

            token = JWTUtils.generateToken(payload);

            map.put("jwt", token);

            //??????Map?????????--json??????
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
