package com.example.demo.controller;

import com.example.demo.bean.trivialBeans.Ultimatectrl;
import com.example.demo.mapper.straightMappers.UltimatecontrolMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin("http://localhost:3000")
public class UltimateController {

    /* 总控制类，负责控制整个教务系统的关键开关
    * 目前只有选课开关的控制 */
    private final UltimatecontrolMapper ultimatecontrolMapper;


    public UltimateController(UltimatecontrolMapper ultimatecontrolMapper) {
        this.ultimatecontrolMapper = ultimatecontrolMapper;
        Ultimatectrl ultimatectrl = new Ultimatectrl("classcontrol", "enabled");
        ultimatecontrolMapper.save(ultimatectrl);
    }

    /*获取一个开关状态*/
    @RequestMapping(value="/controls/{name}", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getControlStatus (@RequestHeader("Authentication") String authentication,
                                                              @PathVariable("name") String name) {
        Map<String, Object> map = new HashMap<>();

        String credit = ControllerOperation.checkAuthentication(authentication);
        if (credit.equals("IsAdmin") || credit.equals("IsTeacher") || credit.equals("IsStudent")){

            Ultimatectrl ultimatectrl = ultimatecontrolMapper.findByName(name);
            map.put("control", ultimatectrl);
            return ControllerOperation.getConductResponse("Success", map);

        }
        else
            return ControllerOperation.getErrorResponse(credit, map);
    }

    /*修改一个开关*/
    @RequestMapping(value="/controls/{name}", method = RequestMethod.PATCH)
    public ResponseEntity<Map<String, Object>> changeControl (@RequestHeader("Authentication") String authentication,
                                                              @PathVariable("name") String name,
                                                              @RequestBody Ultimatectrl control) {
        Map<String, Object> map = new HashMap<>();

        String credit = ControllerOperation.checkAuthentication(authentication);
        if (credit.equals("IsAdmin")){

            control.setName(name);
            if(ultimatecontrolMapper.findByName(name) != null){
                ultimatecontrolMapper.save(control);

                return ControllerOperation.getConductResponse("Success", map);
            }
            return ControllerOperation.getConductResponse("NotFound", map);
        }
        else
            return ControllerOperation.getErrorResponse(credit, map);
    }
}
