package com.example.demo.controller;

import com.example.demo.bean.BeanTools;
import com.example.demo.bean.Lesson;
import com.example.demo.bean.UserBean;
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

    /*查--获取新 lessonid */
    @Override
    String getANewConcreteId() {//admin, teacher
        return userService.getANewLessonid();
    }

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

    @Override
    Map<String, Object> getANewId_impl(String authority) {
        Map<String, Object> map = new HashMap<>();
        switch (authority) {
            case AdminAuthority, TeacherAuthority, StudentAuthority -> {
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


    /*查--返回所有lessonid*/
    @Override
    List<String> getAllConcreteIds(Boolean showall) {
        if (showall)//admin
            return userService.getAllLessonid();
        else//student
            return userService.getAllLessonid(false);
    }

    @Override
    List<String> getAllConcreteIds(Boolean showall, String name) {//teacher
        return userService.getAllLessonid(name, false);
    }

    @Override
    Map<String, Object> getAllIds_impl(String authority, String name) {
        Map<String, Object> map = new HashMap<>();
        switch (authority) {
            case AdminAuthority, TeacherAuthority, StudentAuthority -> {
                map.put("result", "Success");
                map.put(getIds(), userService.getAllLessonid());
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

    /*查--获取一个lesson*/
    @Override
    Lesson getConcreteBean(String id, Boolean showall) {
        if (showall) {//admin
            return userService.getALesson(id);
        } else {//student
            Lesson lesson = userService.getALesson(id);
            if (lesson != null && !Objects.equals(lesson.getStatus(), "censored"))
                return null;
            return lesson;
        }
    }

    @Override
    Lesson getConcreteBean(String id, Boolean showall, String name) {//teacher
        Lesson lesson = userService.getALesson(id);
        if (lesson != null) {
            if (Objects.equals(lesson.getStatus(), "censored") || Objects.equals(lesson.getTeacher(), name))
                return lesson;
        }
        return null;
    }

    @Override
    Map<String, Object> getABean_impl(String authority, String id, String name) {
        Map<String, Object> map = new HashMap<>();

        switch (authority) {
            case AdminAuthority -> {
                map.put("result", "Success");
                map.put(getBean(), userService.getALesson(id));
            }
            case StudentAuthority -> {
                Lesson lesson = userService.getALesson(id);
                if (lesson != null && !Objects.equals(lesson.getStatus(), "censored")){
                    map.put("result","NotFound");
                }
                map.put("result", "Success");
                map.put(getBean(), lesson);
            }
            case TeacherAuthority -> {
                Lesson lesson = userService.getALesson(id);
                if (lesson != null) {
                    if (Objects.equals(lesson.getStatus(), "censored") || Objects.equals(lesson.getTeacher(), name))
                        map.put("result", "Success");
                        map.put(getBean(), lesson);
                }
                map.put("result","NotFound");
            }
            default -> {
                map.put("result", "NoAuth");
            }
        }
        return map;
    }


    @Override
    @RequestMapping(value = "/lesson/{lessonid}", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getABean(@PathVariable("lessonid") String lessonid,
                                                        @RequestHeader("Authentication") String authentication) {
        return super.getABean(lessonid, authentication);
    }

    /*增--新增lesson*/
    @Override
    String createAConcreteBean(String lessonid, Lesson lesson) {//admin
        lesson.setStatus("censored");
        return userService.createALesson(lessonid, lesson);
    }

    @Override
    String createAConcreteBean(String lessonid, Lesson lesson, String name) {//教师操作
        lesson.setTeacher(name);
        lesson.setStatus("pending");
        return userService.createALesson(lessonid, lesson);

    }

    @Override
    Map<String, Object> createABean_impl(String authority, String id, Lesson bean, String name) {

        Map<String, Object> map = new HashMap<>();
        switch (authority) {
            case AdminAuthority -> {
                map.put("result", "Success");
                bean.setStatus("censored");
                map.put(getIds(), userService.createALesson(id, bean));
            }
            case TeacherAuthority -> {
                map.put("result", "Success");
                bean.setTeacher(name);
                bean.setStatus("pending");
                map.put(getIds(), userService.createALesson(id, bean));
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

    /*改--重写一个lesson*/
    @Override
    String rewriteConcreteBean(String id, Lesson bean) {//直接模仿UserController的对应操作去重构，不会的问组长
        return null;
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
                } else if (!Objects.equals(id, bean.getLessonid())) {
                    map.put("result", "FormError");
                    return map;
                }
                map.put("result", "Success");
                map.put(getBean(), userService.rewriteALesson(id, bean));
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

    /*改--修改一个lesson，patch*/
    @Override
    String modifyAConcreteBean(String lessonid, Lesson lesson) {//admin
        Lesson lesson_ori = userService.getALesson(lessonid);

        if (lesson_ori == null)
            return "NotFound";

        String[] adminauth = {"lessonname", "school", "hour", "credit", "teacher", "introduction", "period", "place", "capacity", "status"};

        List<String> changeableList = new ArrayList<>(Arrays.asList(adminauth));
        Lesson lesson_modified = BeanTools.modify(lesson_ori, lesson, changeableList);
        return userService.rewriteALesson(lessonid, lesson_modified);
    }

    @Override
    String modifyAConcreteBean(String lessonid, Lesson lesson, String name) {//teacher
        Lesson lesson_ori = userService.getALesson(lessonid);

        if (lesson_ori == null)
            return "NotFound";
        if (!lesson_ori.getTeacher().equals(name))
            return "NotFound";

        String[] teacherAuth = {"lessonname", "period", "place"};

        List<String> changeableList = new ArrayList<>(Arrays.asList(teacherAuth));
        Lesson lesson_modified = BeanTools.modify(lesson_ori, lesson, changeableList);
        return userService.rewriteALesson(lessonid, lesson_modified);
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
                map.put("result", "Success");
                String[] adminauth = {"lessonname", "school", "hour", "credit", "teacher", "introduction", "period", "place", "capacity", "status"};

                List<String> changeableList = new ArrayList<>(Arrays.asList(adminauth));
                Lesson bean_modified = BeanTools.modify(bean_ori, bean, changeableList);
                map.put(getBean(), userService.rewriteALesson(id, bean_modified));
                return map;
            }
            case TeacherAuthority, StudentAuthority -> {
                Lesson bean_ori = userService.getALesson(id);
                if (bean_ori == null || !("" + bean_ori.getLessonid()).equals(id)) {
                    map.put("result", "NotFound");
                    return map;
                }
                map.put("result", "Success");
                String[] teacherAuth = {"lessonname", "period", "place"};

                List<String> changeableList = new ArrayList<>(Arrays.asList(teacherAuth));
                Lesson bean_modified = BeanTools.modify(bean_ori, bean, changeableList);
                map.put(getBean(), userService.rewriteALesson(id, bean_modified));
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


    /*删--删除lesson*/
    @Override
    String delConcreteBean(String keyword) {//admin
        return userService.deleteLesson(keyword);
    }

    @Override
    String delConcreteBean(String keyword, String name) {// teacher
        return userService.deleteLesson(keyword, name);
    }

    @Override
    Map<String, Object> delBean_impl(String authority, String keyword, String name) {
        Map<String, Object> map = new HashMap<>();
        switch (authority) {
            case AdminAuthority -> {
                map.put("result", "Success");
                userService.deleteLesson(keyword);
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
