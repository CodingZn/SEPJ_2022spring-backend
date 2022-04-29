package com.example.demo.controller;

import com.alibaba.fastjson.JSONArray;
import com.example.demo.bean.User;
import com.example.demo.utils.BeanTools;
import com.example.demo.bean.Lesson;
import com.example.demo.service.GeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

import static com.example.demo.utils.JWTUtils.*;

@RestController
@CrossOrigin("http://localhost:3000")
public class LessonController extends BasicController<Lesson> {
    private final GeneralService<Lesson> lessonService;

    @Autowired
    public LessonController(GeneralService<Lesson> lessonService) {
        this.lessonService = lessonService;
    }

    /* 该类中所有的方法都来自继承 */

    @Override
    String getId() {
        return "lessonid";
    }

    @Override
    String getIds() {
        return "lessonids";
    }

    @Override
    String getBean() {
        return "lesson";
    }



    /*查--获取所有id*/
    @Override
    Map<String, Object> getAllIds_impl(String authority, String name) {
        Map<String, Object> map = new HashMap<>();
        switch (authority) {
            case AdminAuthority -> {
                map.put("result", "Success");
                map.put(getIds(), lessonService.getAllIds());
            }
            case TeacherAuthority ->{
                map.put("result", "Success");
                List<Lesson> lessonList = new ArrayList<Lesson>(lessonService.getAllBeans());

                for(int i = 0;i < lessonList.size();i++){
                    List<User> teachers = new ArrayList<User>(lessonList.get(i).getTeacher());

                    for(int j = 0;j < teachers.size();j++)
                    {
                        if(teachers.get(i).getName().equals(name) && lessonList.get(i).getStatus().toString().equals("censored"))
                            lessonList.remove(i);
                    }
                }

//              lessonList.removeIf(u -> !Objects.equals(u.getTeacher(), name) && !Objects.equals(u.getStatus().toString(), "censored"));
                List<String> lessonidList = lessonList.stream().map(u -> String.valueOf(u.getLessonid())).collect(Collectors.toList());

                map.put(getIds(), lessonidList);
            }
            case StudentAuthority ->{
                map.put("result", "Success");
                List<Lesson> lessonList = new ArrayList<Lesson>(lessonService.getAllBeans());
                lessonList.removeIf(u -> !Objects.equals(u.getStatus().toString(), "censored"));
                List<String> lessonidList = lessonList.stream().map(u -> String.valueOf(u.getLessonid())).collect(Collectors.toList());
                map.put(getIds(), lessonidList);
            }
            default -> {
                map.put("result", "NoAuth");
            }
        }
        return map;
    }

    @Override
    @RequestMapping(value = "/lessons", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getAllIds(@RequestHeader("Authentication") String authentication) {
        return super.getAllIds(authentication);
    }

    /*查--获取一个实体*/
    @Override
    Map<String, Object> getABean_impl(String authority, String id, String name) {
        Map<String, Object> map = new HashMap<>();

        switch (authority) {
            case AdminAuthority -> {
                Lesson lesson = lessonService.getABean(id);
                if (lesson != null){
                    map.put("result", "Success");
                    map.put(getBean(), lesson);
                }
                else{
                    map.put("result", "NotFound");
                }
                return map;
            }
            case TeacherAuthority -> {
                Lesson lesson = lessonService.getABean(id);
                if (lesson != null &&
                        (Objects.equals(lesson.getStatus().toString(), "censored") || Objects.equals(lesson.getTeacher(), name))) {
                    map.put("result", "Success");
                    map.put(getBean(), lesson);
                }
                else {
                    map.put("result","NotFound");
                }
                return map;
            }
            case StudentAuthority -> {
                Lesson lesson = lessonService.getABean(id);
                if (lesson != null && Objects.equals(lesson.getStatus().toString(), "censored")){
                    map.put("result", "Success");
                    map.put(getBean(), lesson);
                    return map;
                }
                map.put("result","NotFound");
                return map;
            }
            default -> {
                map.put("result", "NoAuth");
                return map;
            }
        }

    }

    @Override
    @RequestMapping(value = "/lesson/{lessonid}", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getABean(@PathVariable("lessonid") String lessonid,
                                                        @RequestHeader("Authentication") String authentication) {
        return super.getABean(lessonid, authentication);
    }

    /*增--新增一个实体,post*/
    @Override
    Map<String, Object> createABean_impl(String authority, String id, Lesson lesson, String name) {

        Map<String, Object> map = new HashMap<>();
        switch (authority) {
            case AdminAuthority -> {
                map.put("result", lessonService.createABean(id, lesson));
            }
            case TeacherAuthority -> {
 /*               lesson.setTeacher(name);*/
                lesson.setStatus(Lesson.Status.pending);
                map.put("result", lessonService.createABean(id, lesson));
            }
            default -> {
                map.put("result", "NoAuth");
            }
        }
        return map;
    }

    @Override
    @RequestMapping(value = "/lesson/{lessonid}", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> createABean(@PathVariable("lessonid") String lessonid,
                                                           @RequestBody Lesson lesson,
                                                           @RequestHeader(value = "Authentication") String authentication) {
        return super.createABean(lessonid, lesson, authentication);
    }

    /*改--重写一个实体*/
    @Override
    Map<String, Object> rewriteABean_impl(String authority, String id, Lesson lesson) {

        Map<String, Object> map = new HashMap<>();
        switch (authority) {
            case AdminAuthority -> {
                Lesson lesson_ori = lessonService.getABean(id);
                if (lesson_ori == null) {
                    map.put("result", "NotFound");
                    return map;
                }
                map.put("result", lessonService.changeABean(id, lesson));
                return map;
            }
            case TeacherAuthority -> {
                Lesson bean_ori = lessonService.getABean(id);
                if (bean_ori == null) {
                    map.put("result", "NotFound");
                    return map;
                }
                String[] changeable = { "lessonname", "arranges"};
                List<String> changeableList = new ArrayList<>(Arrays.asList(changeable));
                Lesson lesson_changed = BeanTools.modify(bean_ori, lesson, changeableList);
                map.put("result", lessonService.changeABean(id, lesson_changed));
                return map;
            }
            default -> {
                map.put("result", "NoAuth");
            }
        }
        return map;
    }

    @Override
    @RequestMapping(value = "/lesson/{lessonid}", method = RequestMethod.PUT)
    public ResponseEntity<Map<String, Object>> rewriteABean(@PathVariable("lessonid") String lessonid,
                                                            @RequestBody Lesson lesson,
                                                            @RequestHeader("Authentication") String authentication) {
        return super.rewriteABean(lessonid, lesson, authentication);
    }

    /*改--重写一个实体*/
    @Override
    Map<String, Object> modifyABean_impl(String authority, String id, Lesson bean) {

        Map<String, Object> map = new HashMap<>();
        switch (authority) {
            case AdminAuthority -> {
                Lesson bean_ori = lessonService.getABean(id);
                if (bean_ori == null) {
                    map.put("result", "NotFound");
                    return map;
                }
                String[] adminauth = {"lessonname", "school", "hour", "credit", "teacher", "introduction", "arranges", "semester" , "majorallowed" , "capacity", "status"};

                List<String> changeableList = new ArrayList<>(Arrays.asList(adminauth));
                Lesson bean_modified = BeanTools.modify(bean_ori, bean, changeableList);
                map.put("result", lessonService.changeABean(id, bean_modified));
                return map;
            }
            case TeacherAuthority -> {
                Lesson bean_ori = lessonService.getABean(id);
                if (bean_ori == null || !("" + bean_ori.getLessonid()).equals(id)) {
                    map.put("result", "NotFound");
                    return map;
                }
                String[] teacherAuth = {"lessonname", "arranges"};

                List<String> changeableList = new ArrayList<>(Arrays.asList(teacherAuth));
                Lesson bean_modified = BeanTools.modify(bean_ori, bean, changeableList);
                map.put("result", lessonService.changeABean(id, bean_modified));
                return map;
            }
            default -> {
                map.put("result", "NoAuth");
                return map;
            }
        }
    }

    @Override
    @RequestMapping(value = "/lesson/{lessonid}", method = RequestMethod.PATCH)
    public ResponseEntity<Map<String, Object>> modifyABean(@PathVariable("lessonid") String lessonid,
                                                           @RequestBody Lesson lesson,
                                                           @RequestHeader(value = "Authentication") String authentication) {
        return super.modifyABean(lessonid, lesson, authentication);
    }

    /*删--删除一个实体*/
    @Override
    Map<String, Object> delBean_impl(String authority, String keyword, String name) {
        Map<String, Object> map = new HashMap<>();
        switch (authority) {
            case AdminAuthority -> {
                map.put("result", lessonService.deleteABean(keyword));
            }
            case TeacherAuthority -> {
                Lesson lesson = lessonService.getABean(keyword);
                if (lesson != null && !Objects.equals(lesson.getTeacher(), name)){
                    map.put("result", "NoAuth");
                    return map;
                }
                map.put("result", lessonService.deleteABean(keyword));
            }
            default -> {
                map.put("result", "NoAuth");
            }
        }
        return map;
    }

    @Override
    @RequestMapping(value = "/lesson/{lessonid}", method = RequestMethod.DELETE)
    public ResponseEntity<Map<String, Object>> delBean(@PathVariable("lessonid") String lessonid,
                                                       @RequestHeader("Authentication") String authentication) {
        return super.delBean(lessonid, authentication);
    }


    /***************未使用的抽象方法******************/

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
}
