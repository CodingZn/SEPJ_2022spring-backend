package com.example.demo.controller;

import com.example.demo.bean.BeanTools;
import com.example.demo.bean.Major;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin("http://localhost:3000")
public class MajorController extends BasicController<Major> {
    private final UserService userService;

    @Autowired
    public MajorController(UserService userService) {
        this.userService = userService;
    }

    @Override
    String auth1() {
        return "IsAdmin";
    }

    @Override
    String auth2() {
        return "IsTeacher";
    }

    @Override
    String auth3() {
        return "IsStudent";
    }

    @Override
    String getIds() {
        return "majornumbers";
    }

    @Override
    String getId() {
        return "majornumber";
    }


    /**************继承操作***************/


    /*查--获取新majornumber*/
    @Override
    String getANewConcreteId() {
        return userService.getANewMajornumber();
    }

    @Override
    @RequestMapping(value="/majornumber", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getANewId(@RequestHeader(value="Authentication") String authentication) {
        return super.getANewId(authentication);
    }

    /*查--返回所有majornumber*/

    @Override
    List<String> getAllConcreteIds(Boolean showall) {
        return userService.getAllMajornumbers();
    }

    @Override
    List<String> getAllConcreteIds(Boolean showall, String name) {
        return null;
    }



    @Override
    @RequestMapping(value="/majors", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getAllIds(@RequestHeader(value="Authentication") String authentication) {
        return super.getAllIds(authentication);
    }


    /*查--获取一个专业*/
    @Override
    Major getConcreteBean(String id, Boolean showall) {
        return userService.getAMajor(id);
    }

    @Override
    Major getConcreteBean(String id, Boolean showall, String name) {
        return null;
    }

    @Override
    @RequestMapping(value="/major/{majornumber}", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getABean(@PathVariable("majornumber") String majornumber_str,
                                                        @RequestHeader(value="Authentication") String authentication) {
        return super.getABean(majornumber_str, authentication);
    }

    /*增--新增专业*/
    @Override
    String createAConcreteBean(String id, Major bean) {
        return userService.createAMajor(id, bean);
    }

    @Override
    String createAConcreteBean(String id, Major bean, String name) {
        return null;
    }
    @RequestMapping(value="/major/{majornumber}", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> createABean(@PathVariable("majornumber") String majornumber_str,
                                                           @RequestBody Major major,
                                                           @RequestHeader(value="Authentication") String authentication){
       return super.createABean(majornumber_str, major, authentication);

    }

    /*改--重写一个专业*/
    @Override
    String rewriteConcreteBean(String id, Major bean) {
        return null;
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
    String modifyAConcreteBean(String majornumber_str, Major major) {
        Major major_ori = userService.getAMajor(majornumber_str);

        if(major_ori == null)
            return "NotFound";

        String [] adminauth = {"school", "name"};
        List<String> changeableList = new ArrayList<>(Arrays.asList(adminauth));

        Major major_modified = BeanTools.modify(major_ori, major, changeableList);

        return userService.rewriteAMajor(majornumber_str, major_modified);

    }

    @Override
    String modifyAConcreteBean(String majornumber_str, Major major, String name) {
        return null;
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
    String delConcreteBean(String majornumber_str) {
        return userService.deleteMajor(majornumber_str);
    }

    @Override
    String delConcreteBean(String keyword, String name) {
        return null;
    }

    @Override
    @RequestMapping(value="/major/{majornumber}", method = RequestMethod.DELETE)
    public ResponseEntity<Map<String, Object>> delBean(@PathVariable("majornumber") String majornumber_str,
                                                       @RequestHeader(value="Authentication") String authentication) {
        return super.delBean(majornumber_str, authentication);
    }


}
