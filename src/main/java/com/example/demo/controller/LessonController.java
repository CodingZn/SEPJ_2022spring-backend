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
    String getIds() {
        return "lessonids";
    }

    @Override
    String getBean() {
        return "lesson";
    }

    @Override
    String getBeans() {
        return "lessons";
    }


    /* 1-查--getAllIds--获取所有id*/
    @Override
    Map<String, Object> getAllIds_impl(String authority, String userid) {
        Map<String, Object> map = new HashMap<>();
        switch (authority) {
            case AdminAuthority -> {
                map.put("result", "Success");
                map.put(getIds(), lessonService.getAllIds());
            }
            case TeacherAuthority ->{//只能查找到自己参与开的 和 审核通过的课程
                map.put("result", "Success");
                List<Lesson> lessonList = new ArrayList<>(lessonService.getAllBeans());
                lessonList.removeIf(u -> u.getTeacher().stream().map(User::getUserid).toList().contains(userid)
                        && !Objects.equals(u.getStatus(), Lesson.Status.censored));
                List<String> lessonidList = lessonList.stream().map(u -> String.valueOf(u.getLessonid())).collect(Collectors.toList());
                map.put(getIds(), lessonidList);
            }
            case StudentAuthority ->{//只能查找到审核通过的课程
                map.put("result", "Success");
                List<Lesson> lessonList = new ArrayList<>(lessonService.getAllBeans());
                lessonList.removeIf(u -> !Objects.equals(u.getStatus(), Lesson.Status.censored));
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

    /* 2-查--getABean--获取一个实体*/
    @Override
    Map<String, Object> getABean_impl(String authority, String userid, String key) {
        Map<String, Object> map = new HashMap<>();

        switch (authority) {
            case AdminAuthority -> {
                Lesson lesson = lessonService.getABean(key);
                if (lesson != null){
                    map.put("result", "Success");
                    map.put(getBean(), lesson);
                }
                else{
                    map.put("result", "NotFound");
                }
                return map;
            }
            case TeacherAuthority -> {//只能查找到自己参与开的 和 审核通过的课程
                Lesson lesson = lessonService.getABean(key);

                if (lesson != null &&
                        (Objects.equals(lesson.getStatus(), Lesson.Status.censored)
                                || (lesson.getTeacher().stream().map(User::getUserid).toList().contains(userid)))) {
                    map.put("result", "Success");
                    map.put(getBean(), lesson);
                }
                else {
                    map.put("result","NotFound");
                }
                return map;
            }
            case StudentAuthority -> {//只能查找到审核通过的课程
                Lesson lesson = lessonService.getABean(key);
                if (lesson != null && Objects.equals(lesson.getStatus(), Lesson.Status.censored)){
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
    public ResponseEntity<Map<String, Object>> getABean(@RequestHeader(value = "Authentication") String authentication,
                                                        @PathVariable("lessonid") String key) {
        return super.getABean(authentication, key);
    }

    /* 3-查--getAllBeans--获取全部实体*/
    @Override
    Map<String, Object> getAllBeans_impl(String authority, String userid) {
        Map<String, Object> map = new HashMap<>();

        switch (authority) {
            case AdminAuthority -> {
                map.put(getBeans(), lessonService.getAllBeans());
                map.put("result", "Success");
            }
            case TeacherAuthority ->{//只能查找到自己参与开的 和 审核通过的课程
                map.put("result", "Success");
                List<Lesson> lessonList = new ArrayList<>(lessonService.getAllBeans());
                lessonList.removeIf(u -> !u.getTeacher().stream().map(User::getUserid).toList().contains(userid)
                        && !Objects.equals(u.getStatus(), Lesson.Status.censored));
                map.put(getBeans(), lessonList);
            }
            case StudentAuthority ->{//只能查找到审核通过的课程
                map.put("result", "Success");
                List<Lesson> lessonList = new ArrayList<>(lessonService.getAllBeans());
                lessonList.removeIf(u -> !Objects.equals(u.getStatus(), Lesson.Status.censored));
                map.put(getBeans(), lessonList);
            }
            default -> {
                map.put("result", "NoAuth");
            }
        }
        return map;
    }

    @Override
    @RequestMapping(value = "/lessons", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getAllBeans(@RequestHeader(value = "Authentication") String authentication) {
        return super.getAllBeans(authentication);
    }

    /* 4-增--createABean--新增一个实体*/
    @Override
    Map<String, Object> createABean_impl(String authority, String userid, String key, Lesson lesson) {

        Map<String, Object> map = new HashMap<>();
        switch (authority) {
            case AdminAuthority -> {
                map.put("result", lessonService.createABean(userid, lesson));
            }
            case TeacherAuthority -> {
                /*!!!               lesson.setTeacher(name);*/
                lesson.setStatus(Lesson.Status.pending);
                map.put("result", lessonService.createABean(userid, lesson));
            }
            default -> {
                map.put("result", "NoAuth");
            }
        }
        return map;
    }

    @Override
    @RequestMapping(value = "/lesson/{lessonid}", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> createABean(@RequestHeader(value = "Authentication") String authentication,
                                                           @PathVariable("lessonid") String key,
                                                           @RequestBody Lesson lesson) {
        return super.createABean(authentication, key, lesson);
    }

    /* 5-增--createBeans--新增多个实体*/
    @Override
    Map<String, Object> createBeans_impl(String authority, String userid, List<Lesson> beans) {
        Map<String, Object> map = new HashMap<>();
        switch (authority) {
            case AdminAuthority -> {
                map.put("result", lessonService.createBeans(beans));
            }
            default -> {
                map.put("result", "NoAuth");
            }
        }
        return map;
    }

    @Override
    @RequestMapping(value = "/lessons", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> createBeans(@RequestHeader(value = "Authentication") String authentication,
                                                           @RequestBody JSONArray jsonArray, Class<Lesson> clazz) {
        return super.createBeans(authentication, jsonArray, clazz);
    }

    /* 6-改--rewriteABean--重写一个实体,put*/
    @Override
    Map<String, Object> rewriteABean_impl(String authority, String userid, String key, Lesson bean) {
        Map<String, Object> map = new HashMap<>();
        switch (authority) {
            case AdminAuthority -> {
                Lesson lesson_ori = lessonService.getABean(key);
                if (lesson_ori == null) {
                    map.put("result", "NotFound");
                    return map;
                }
                map.put("result", lessonService.changeABean(key, bean));
                return map;
            }
            case TeacherAuthority -> {
                Lesson lesson_ori = lessonService.getABean(key);
                if (lesson_ori == null) {
                    map.put("result", "NotFound");
                    return map;
                }
                if (!lesson_ori.getTeacher().stream().map(User::getUserid).toList().contains(userid)){
                    map.put("result", "NoAuth");
                    return map;
                }
                map.put("result", lessonService.changeABean(key, bean));
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
    public ResponseEntity<Map<String, Object>> rewriteABean(@RequestHeader(value = "Authentication") String authentication,
                                                            @PathVariable("lessonid") String key,
                                                            @RequestBody Lesson lesson) {
        return super.rewriteABean(authentication, key, lesson);
    }

    /* 7-改--modifyABean--修改一个实体,patch*/
    @Override
    Map<String, Object> modifyABean_impl(String authority, String userid, String key, Lesson bean) {

        Map<String, Object> map = new HashMap<>();
        switch (authority) {
            case AdminAuthority -> {
                Lesson bean_ori = lessonService.getABean(key);
                if (bean_ori == null) {
                    map.put("result", "NotFound");
                    return map;
                }
                String[] adminauth = {"lessonname", "school", "hour", "credit", "teacher", "introduction", "arranges", "semester" , "majorallowed" , "capacity", "status"};

                List<String> changeableList = new ArrayList<>(Arrays.asList(adminauth));
                Lesson bean_modified = BeanTools.modify(bean_ori, bean, changeableList);
                map.put("result", lessonService.changeABean(key, bean_modified));
                return map;
            }
            case TeacherAuthority -> {
                Lesson lesson_ori = lessonService.getABean(key);
                if (lesson_ori == null) {
                    map.put("result", "NotFound");
                    return map;
                }
                if (!lesson_ori.getTeacher().stream().map(User::getUserid).toList().contains(userid)){
                    map.put("result", "NoAuth");
                    return map;
                }
                String[] teacherAuth = {"lessonname", "arranges"};

                List<String> changeableList = new ArrayList<>(Arrays.asList(teacherAuth));
                Lesson bean_modified = BeanTools.modify(lesson_ori, bean, changeableList);
                map.put("result", lessonService.changeABean(key, bean_modified));
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
    public ResponseEntity<Map<String, Object>> modifyABean(@RequestHeader(value = "Authentication") String authentication,
                                                           @PathVariable("lessonid") String key,
                                                           @RequestBody Lesson lesson) {
        return super.modifyABean(authentication, key, lesson);
    }

    /* 8-删--deleteABean--删除一个实体*/
    @Override
    Map<String, Object> delBean_impl(String authority, String userid, String key) {
        Map<String, Object> map = new HashMap<>();
        switch (authority) {
            case AdminAuthority -> {
                map.put("result", lessonService.deleteABean(key));
            }
            case TeacherAuthority -> {
                Lesson lesson = lessonService.getABean(key);
                if (lesson == null) {
                    map.put("result", "NotFound");
                    return map;
                }
                if (!lesson.getTeacher().stream().map(User::getUserid).toList().contains(userid)){
                    map.put("result", "NoAuth");
                    return map;
                }
                map.put("result", lessonService.deleteABean(key));
            }
            default -> {
                map.put("result", "NoAuth");
            }
        }
        return map;
    }

    @Override
    @RequestMapping(value = "/lesson/{lessonid}", method = RequestMethod.DELETE)
    public ResponseEntity<Map<String, Object>> delBean(@RequestHeader("Authentication") String authentication,
                                                       @PathVariable("lessonid") String lessonid) {
        return super.delBean(authentication, lessonid);
    }

    /* 9-删--deleteBeans--删除多个实体*/
    @Override
    Map<String, Object> delBeans_impl(String authority, String userid, List<?> ids) {
        Map<String, Object> map = new HashMap<>();
        switch (authority) {
            case AdminAuthority -> {
                map.put("result", lessonService.deleteBeans(ids));
            }
            case TeacherAuthority -> {//先剔除掉无权限的课程再操作
                List<String> lessonidList = (List<String>) ids;
                List<Lesson> lessonList = lessonidList.stream().map(lessonService::getABean).toList();
                lessonList.removeIf(Objects::isNull);
                lessonList.removeIf( u -> !u.getTeacher().stream().map(User::getUserid).toList().contains(userid)
                        && !Objects.equals(u.getStatus(), Lesson.Status.censored));
                lessonidList = lessonList.stream().map(Lesson::getLessonid).toList();
                lessonService.deleteBeans(lessonidList);
            }
            default -> {
                map.put("result", "NoAuth");
            }
        }
        return map;
    }

}
