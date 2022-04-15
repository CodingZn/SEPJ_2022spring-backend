package com.example.demo.controller;

import com.example.demo.bean.BeanTools;
import com.example.demo.bean.Lesson;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.example.demo.bean.JWTUtils.*;

@RestController
@CrossOrigin("http://localhost:3000")
public class LessonController extends BasicController<Lesson> {
    private final UserService userService;

    @Autowired
    public LessonController(UserService userService) {
        this.userService = userService;
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

    @Override
    Map<String, Object> getANewId_impl(String authority) {
        Map<String, Object> map = new HashMap<>();
        switch (authority) {
            case AdminAuthority, TeacherAuthority -> {
                map.put("result", "Success");
                map.put(getId(), userService.getANewLessonid());
            }
            default -> {
                map.put("result", "NoAuth");
            }
        }
        return map;
    }

    @Override
    @RequestMapping(value = "/lessonid", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getANewId(@RequestHeader("Authentication") String authentication) {
        return super.getANewId(authentication);
    }


    @Override
    Map<String, Object> getAllIds_impl(String authority, String name) {
        Map<String, Object> map = new HashMap<>();
        switch (authority) {
            case AdminAuthority -> {
                map.put("result", "Success");
                map.put(getIds(), userService.getAllLessonid());
            }
            case TeacherAuthority ->{
                map.put("result", "Success");
                map.put(getIds(), userService.getAllLessonid(name, false));
            }
            case StudentAuthority ->{
                map.put("result", "Success");
                map.put(getIds(), userService.getAllLessonid(false));
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

    @Override
    Map<String, Object> getABean_impl(String authority, String id, String name) {
        Map<String, Object> map = new HashMap<>();

        switch (authority) {
            case AdminAuthority -> {
                Lesson lesson = userService.getALesson(id);
                if (lesson != null){
                    map.put("result", "Success");
                    map.put(getBean(), lesson);
                }
                else{
                    map.put("result", "NotFound");
                }
                return map;
            }
            case StudentAuthority -> {
                Lesson lesson = userService.getALesson(id);
                if (lesson != null && Objects.equals(lesson.getStatus(), "censored")){
                    map.put("result", "Success");
                    map.put(getBean(), lesson);
                    return map;
                }
                map.put("result","NotFound");
                return map;
            }
            case TeacherAuthority -> {
                Lesson lesson = userService.getALesson(id);
                if (lesson != null &&
                        (Objects.equals(lesson.getStatus(), "censored") || Objects.equals(lesson.getTeacher(), name))) {
                    map.put("result", "Success");
                    map.put(getBean(), lesson);
                    return map;
                }
                else {
                    map.put("result","NotFound");
                    return map;
                }
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

    @Override
    Map<String, Object> createABean_impl(String authority, String id, Lesson lesson, String name) {

        Map<String, Object> map = new HashMap<>();
        switch (authority) {
            case AdminAuthority -> {
                lesson.setStatus("censored");
                map.put("result", userService.createALesson(id, lesson));
            }
            case TeacherAuthority -> {
                lesson.setTeacher(name);
                lesson.setStatus("pending");
                map.put("result", userService.createALesson(id, lesson));
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

    @Override
    Map<String, Object> rewriteABean_impl(String authority, String id, Lesson bean) {

        Map<String, Object> map = new HashMap<>();
        switch (authority) {
            case AdminAuthority -> {
                Lesson bean_ori = userService.getALesson(id);
                if (bean_ori == null) {
                    map.put("result", "NotFound");
                    return map;
                }
                map.put("result", userService.rewriteALesson(id, bean));
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

    @Override
    Map<String, Object> modifyABean_impl(String authority, String id, Lesson bean) {

        Map<String, Object> map = new HashMap<>();
        switch (authority) {
            case AdminAuthority -> {
                Lesson bean_ori = userService.getALesson(id);
                if (bean_ori == null) {
                    map.put("result", "NotFound");
                    return map;
                }
                String[] adminauth = {"lessonname", "school", "hour", "credit", "teacher", "introduction", "period", "place", "capacity", "status"};

                List<String> changeableList = new ArrayList<>(Arrays.asList(adminauth));
                Lesson bean_modified = BeanTools.modify(bean_ori, bean, changeableList);
                map.put("result", userService.rewriteALesson(id, bean_modified));
                return map;
            }
            case TeacherAuthority -> {
                Lesson bean_ori = userService.getALesson(id);
                if (bean_ori == null || !("" + bean_ori.getLessonid()).equals(id)) {
                    map.put("result", "NotFound");
                    return map;
                }
                String[] teacherAuth = {"lessonname", "period", "place"};

                List<String> changeableList = new ArrayList<>(Arrays.asList(teacherAuth));
                Lesson bean_modified = BeanTools.modify(bean_ori, bean, changeableList);
                map.put("result", userService.rewriteALesson(id, bean_modified));
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


    @Override
    Map<String, Object> delBean_impl(String authority, String keyword, String name) {
        Map<String, Object> map = new HashMap<>();
        switch (authority) {
            case AdminAuthority -> {
                map.put("result", userService.deleteLesson(keyword));
            }
            case TeacherAuthority -> {
                map.put("result", userService.deleteLesson(keyword, name));
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


}
