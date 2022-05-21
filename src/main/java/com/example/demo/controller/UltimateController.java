package com.example.demo.controller;

import com.example.demo.bean.specialBean.Ultimatectrl;
import com.example.demo.exceptions.MyException;
import com.example.demo.mapper.straightMappers.UltimatecontrolMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.demo.utils.JWTUtils.*;
import static com.example.demo.bean.specialBean.Ultimatectrl.*;

@RestController
@CrossOrigin
public class UltimateController {

    private final UltimatecontrolMapper ultimatecontrolMapper;

    @Autowired
    public UltimateController(UltimatecontrolMapper ultimatecontrolMapper) {
        this.ultimatecontrolMapper = ultimatecontrolMapper;
        if (!havingControl(KEY_CLASS_CONTROL))
            createAControl(KEY_CLASS_CONTROL, VALUE_CLASS_CONTROL_DISABLED);
        if (!havingControl(KEY_SEMESTER_CONTROL))
            createAControl(KEY_SEMESTER_CONTROL, "2021B");
        if (!havingControl(KEY_YEAR_CONTROL))
            createAControl(KEY_YEAR_CONTROL, "21");
        if (!havingControl(KEY_LATEST_GRADE_CONTROL))
            createAControl(KEY_LATEST_GRADE_CONTROL, "22");
        if (!havingControl(KEY_OLDEST_GRADE_CONTROL))
            createAControl(KEY_OLDEST_GRADE_CONTROL, "17");
    }

    private void createAControl(String key, String value){
        Ultimatectrl ultimatectrl = new Ultimatectrl(key, value);
        ultimatecontrolMapper.save(ultimatectrl);
    }

    private boolean havingControl(String key){
        return ultimatecontrolMapper.findById(key).isPresent();
    }

    /*获取一个开关状态*/
    @RequestMapping(value="/control/{key}", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getControlStatus (@RequestHeader("Authentication") String authentication,
                                                              @PathVariable("key") String key) {
        Map<String, Object> map = new HashMap<>();

        String credit = ControllerOperation.checkAuthentication(authentication);
        String authority = ControllerOperation.getAuthority(authentication);
        if (credit.equals(ValidJWTToken)){

            Ultimatectrl ultimatectrl = ultimatecontrolMapper.findByName(key);
            map.put("control", ultimatectrl);
            return ControllerOperation.getConductResponse("Success", map);

        }
        else
            return ControllerOperation.getConductResponse(credit, map);
    }

    //获取选课开关的选项
    /*获取一个开关状态*/
    @RequestMapping(value="/control/classControl/values", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getClassControl (@RequestHeader("Authentication") String authentication) {
        Map<String, Object> map = new HashMap<>();

        String credit = ControllerOperation.checkAuthentication(authentication);
        String authority = ControllerOperation.getAuthority(authentication);
        if (credit.equals(ValidJWTToken)){

            List<String> values = new ArrayList<>();
            values.add(VALUE_CLASS_CONTROL_DISABLED);
            values.add(VALUE_CLASS_CONTROL_FIRST);
            values.add(VALUE_CLASS_CONTROL_SECOND);
            map.put("values", values);
            return ControllerOperation.getConductResponse("Success", map);

        }
        else
            return ControllerOperation.getConductResponse(credit, map);
    }

    /*获取所有开关状态*/
    @RequestMapping(value="/controls", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getAllControlsStatus (@RequestHeader("Authentication") String authentication) {
        Map<String, Object> map = new HashMap<>();

        String credit = ControllerOperation.checkAuthentication(authentication);
        String authority = ControllerOperation.getAuthority(authentication);
        if (credit.equals(ValidJWTToken)){
            List<Ultimatectrl> ultimatectrls = ultimatecontrolMapper.findAll();
            map.put("controls", ultimatectrls);
            return ControllerOperation.getConductResponse("Success", map);

        }
        else
            return ControllerOperation.getConductResponse(credit, map);
    }

    /*修改一个开关*/
    @RequestMapping(value="/control/{key}", method = RequestMethod.PATCH)
    public ResponseEntity<Map<String, Object>> changeControl (@RequestHeader("Authentication") String authentication,
                                                              @PathVariable("key") String key,
                                                              @RequestBody Ultimatectrl control) {
        Map<String, Object> map = new HashMap<>();

        String credit = ControllerOperation.checkAuthentication(authentication);
        String authority = ControllerOperation.getAuthority(authentication);
        if (authority.equals(AdminAuthority)){

            control.setName(key);
            //检查输入value值的格式
            if(ultimatecontrolMapper.findByName(key) != null){
                switch (control.getName()){
                    case KEY_CLASS_CONTROL->{
                        if(!control.getValue().matches(REGEX_CLASS_CONTROL))
                            throw new MyException("必须为"+VALUE_CLASS_CONTROL_DISABLED+"、"+VALUE_CLASS_CONTROL_FIRST+"、"
                            +VALUE_CLASS_CONTROL_SECOND+"之一！");
                    }
                    case KEY_SEMESTER_CONTROL->{
                        if (!control.getValue().matches(REGEX_SEMESTER_CONTROL))
                            throw new MyException("格式错误！请输入四位年份+A或B。");
                    }
                    case KEY_YEAR_CONTROL, KEY_LATEST_GRADE_CONTROL, KEY_OLDEST_GRADE_CONTROL->{
                        if (!control.getValue().matches(REGEX_YEAR_CONTROL))
                            throw new MyException("格式错误！请输入两位年份。");
                    }
                    default -> {
                        return ControllerOperation.getConductResponse("NotFound", map);
                    }
                }
                ultimatecontrolMapper.save(control);
                return ControllerOperation.getConductResponse("Success", map);
            }
            return ControllerOperation.getConductResponse("NotFound", map);
        }
        else
            return ControllerOperation.getConductResponse(authority, map);
    }
}
