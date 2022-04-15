package com.example.demo.controller;

import com.example.demo.bean.BeanTools;
import com.example.demo.bean.Major;
import com.example.demo.bean.UserBean;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.example.demo.bean.JWTUtils.*;

@RestController
@CrossOrigin("http://localhost:3000")
public class MajorController extends BasicController<Major> {
    private final UserService userService;

    @Autowired
    public MajorController(UserService userService) {
        this.userService = userService;
    }


    /* 该类中所有的方法都来自继承 */


    @Override
    String getId() {
        return null;
    }

    @Override
    String getIds() {
        return null;
    }

    @Override
    String getBean() {
        return null;
    }

    /*查--获取新majornumber*/

    @Override
    String getANewConcreteId() {//三种角色都能使用
        return userService.getANewMajornumber();
    }

    @Override
    Map<String, Object> getANewId_impl(String authority) {
        Map<String, Object> map = new HashMap<>();
        switch (authority){
            case AdminAuthority, TeacherAuthority, StudentAuthority ->{
                map.put("result", "Success");
                map.put(getId() ,userService.getANewMajornumber());
            }
            default -> {
                map.put("result", "NoAuth");
            }
        }
        return map;
    }

    @Override
    @RequestMapping(value="/majornumber", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getANewId(@RequestHeader(value="Authentication") String authentication) {
        return super.getANewId(authentication);
    }

    /*查--返回所有majornumber*/

    @Override
    List<String> getAllConcreteIds(Boolean showall) {//admin和student的操作
        return userService.getAllMajornumbers();
    }

    @Override
    List<String> getAllConcreteIds(Boolean showall, String name) {//student
        return userService.getAllMajornumbers();
    }

    @Override
    Map<String, Object> getAllIds_impl(String authority, String name) {
        Map<String, Object> map = new HashMap<>();
        switch (authority){
            case AdminAuthority, TeacherAuthority, StudentAuthority ->{
                map.put("result", "Success");
                map.put(getIds() ,userService.getAllMajornumbers());
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


    /*查--获取一个专业*/
    @Override
    Major getConcreteBean(String id, Boolean showall) {//admin,student
        return userService.getAMajor(id);
    }

    @Override
    Major getConcreteBean(String id, Boolean showall, String name) {//教师操作
        return userService.getAMajor(id);
    }

    @Override
    Map<String, Object> getABean_impl(String authority, String id, String name) {
        Map<String, Object> map = new HashMap<>();

        switch (authority){
            case AdminAuthority, StudentAuthority, TeacherAuthority ->{
                if (userService.getAMajor(id) != null){
                    map.put("result", "Success");
                    map.put(getBean() ,userService.getAMajor(id));
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

    /*增--新增专业*/
    @Override
    String createAConcreteBean(String id, Major bean) {//admin
        return userService.createAMajor(id, bean);
    }

    @Override
    String createAConcreteBean(String id, Major bean, String name) {//teacher,student
        return null;
    }

    @Override
    Map<String, Object> createABean_impl(String authority, String id, Major bean, String name) {
        Map<String, Object> map = new HashMap<>();
        switch (authority){
            case AdminAuthority->{
                map.put("result", userService.createAMajor(id, bean));
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

    /*改--重写一个专业*/
    @Override
    String rewriteConcreteBean(String id, Major bean) {//直接模仿UserController的对应操作去重构，不会的问组长
        return null;
    }

    @Override
    Map<String, Object> rewriteABean_impl(String authority, String id, Major bean) {
        Map<String, Object> map = new HashMap<>();
        switch (authority){
            case AdminAuthority->{
                Major bean_ori = userService.getAMajor(id);
                if (bean_ori == null){
                    map.put("result", "NotFound");
                    return map;
                }
                else if (!Objects.equals(id, String.valueOf(bean.getMajornumber()))){
                    map.put("result", "FormError");
                    return map;
                }
                map.put("result", userService.rewriteAMajor(id,bean));
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

    /*改--修改一个专业patch*/
    @Override
    String modifyAConcreteBean(String majornumber_str, Major major) {//admin
        Major major_ori = userService.getAMajor(majornumber_str);

        if(major_ori == null)
            return "NotFound";

        String [] adminauth = {"school", "name"};
        List<String> changeableList = new ArrayList<>(Arrays.asList(adminauth));

        Major major_modified = BeanTools.modify(major_ori, major, changeableList);

        return userService.rewriteAMajor(majornumber_str, major_modified);

    }

    @Override
    String modifyAConcreteBean(String majornumber_str, Major major, String name) {//teacher,student
        return null;
    }

    @Override
    Map<String, Object> modifyABean_impl(String authority, String id, Major bean) {
        Map<String, Object> map = new HashMap<>();
        switch (authority){
            case AdminAuthority->{
                Major bean_ori = userService.getAMajor(id);
                if (bean_ori == null){
                    map.put("result", "NotFound");
                    return map;
                }
                map.put("result", "Success");
                String [] adminauth = {"school", "name"};

                List<String> changeableList = new ArrayList<>(Arrays.asList(adminauth));
                Major bean_modified = BeanTools.modify(bean_ori, bean, changeableList);
                map.put("result", userService.rewriteAMajor(id,bean_modified));
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

    /*删--删除专业*/
    @Override
    String delConcreteBean(String majornumber_str) {//admin
        return userService.deleteMajor(majornumber_str);
    }

    @Override
    String delConcreteBean(String keyword, String name) {// teacher, student
        return null;
    }

    @Override
    Map<String, Object> delBean_impl(String authority, String keyword, String name) {
        Map<String, Object> map = new HashMap<>();
        switch (authority){
            case AdminAuthority->{
                map.put("result", userService.deleteMajor(keyword));
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


}
