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
    private final UltimatecontrolMapper ultimatecontrolMapper;


    public UltimateController(UltimatecontrolMapper ultimatecontrolMapper) {
        this.ultimatecontrolMapper = ultimatecontrolMapper;
        Ultimatectrl ultimatectrl = new Ultimatectrl("classcontrol", "enabled");
        ultimatecontrolMapper.save(ultimatectrl);
    }


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
