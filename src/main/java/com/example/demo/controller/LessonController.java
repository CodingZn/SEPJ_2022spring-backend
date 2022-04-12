package com.example.demo.controller;

import com.example.demo.bean.BeanTools;
import com.example.demo.bean.Lesson;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin("http://localhost:3000")
public class LessonController extends BasicController<Lesson> {
    private final UserService userService;

    @Autowired
    public LessonController(UserService userService) {
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
        return "lessonids";
    }

    @Override
    String getId() {
        return "lessonid";
    }

    /* 该类中所有的方法都来自继承 */

    /*查--获取新 lessonid */
    @Override
    String getANewConcreteId() {
        return userService.getANewLessonid();
    }

    @Override
    @RequestMapping(value="/lessonid", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getANewId(@RequestHeader("Authentication") String authentication) {
        return super.getANewId(authentication);
    }


    /*查--返回所有lessonid*/
    @Override
    List<String> getAllConcreteIds(Boolean showall) {
        if (showall)
            return userService.getAllLessonid();
        else
            return userService.getAllLessonid(false);
    }

    @Override
    List<String> getAllConcreteIds(Boolean showall, String name) {
        return userService.getAllLessonid(name, false);
    }

    @Override
    @RequestMapping(value = "/lessons", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getAllIds(@RequestHeader("Authentication") String authentication) {
        return super.getAllIds(authentication);
    }

    /*查--获取一个lesson*/
    @Override
    Lesson getConcreteBean(String id, Boolean showall) {
        if (showall){
            return userService.getALesson(id);
        }
        else{
            Lesson lesson = userService.getALesson(id);
            if (lesson != null && !Objects.equals(lesson.getStatus(), "censored"))
                return null;
            return lesson;
        }
    }

    @Override
    Lesson getConcreteBean(String id, Boolean showall, String name) {
        Lesson lesson = userService.getALesson(id);
        if (lesson != null){
            if (Objects.equals(lesson.getStatus(), "censored") || Objects.equals(lesson.getTeacher(), name))
                return lesson;
        }
        return null;
    }


    @Override
    @RequestMapping(value = "/lesson/{lessonid}", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getABean(@PathVariable("lessonid") String lessonid,
                                                        @RequestHeader("Authentication") String authentication) {
        return super.getABean(lessonid, authentication);
    }

    /*增--新增lesson*/
    @Override
    String createAConcreteBean(String lessonid, Lesson lesson) {
        lesson.setStatus("censored");
        return userService.createALesson(lessonid, lesson);
    }

    @Override
    String createAConcreteBean(String lessonid, Lesson lesson, String name) {
        lesson.setTeacher(name);
        lesson.setStatus("pending");
        return userService.createALesson(lessonid, lesson);

    }

    @Override
    @RequestMapping(value="/lesson/{lessonid}", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> createABean(@PathVariable("lessonid") String lessonid,
                                                           @RequestBody Lesson lesson,
                                                           @RequestHeader(value="Authentication") String authentication) {
        return super.createABean(lessonid, lesson, authentication);
    }

    /*改--重写一个lesson*/
    @Override
    String rewriteConcreteBean(String id, Lesson bean) {
        return null;
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
    String modifyAConcreteBean(String lessonid, Lesson lesson) {
        Lesson lesson_ori = userService.getALesson(lessonid);

        if(lesson_ori == null)
            return "NotFound";

        String [] adminauth = {"lessonname","school","hour","credit","teacher","introduction","period","place","capacity","status"};

        List<String> changeableList = new ArrayList<>(Arrays.asList(adminauth));
        Lesson lesson_modified = BeanTools.modify(lesson_ori, lesson, changeableList);
        return userService.rewriteALesson(lessonid, lesson_modified);
    }

    @Override
    String modifyAConcreteBean(String lessonid, Lesson lesson, String name) {
        Lesson lesson_ori = userService.getALesson(lessonid);

        if(lesson_ori == null)
            return "NotFound";
        if( !lesson_ori.getTeacher().equals(name))
            return "NotFound";

        String [] teacherAuth = {"lessonname","period","place"};

        List<String> changeableList = new ArrayList<>(Arrays.asList(teacherAuth));
        Lesson lesson_modified = BeanTools.modify(lesson_ori, lesson, changeableList);
        return userService.rewriteALesson(lessonid, lesson_modified);
    }

    @Override
    @RequestMapping(value="/lesson/{lessonid}", method = RequestMethod.PATCH)
    public ResponseEntity<Map<String, Object>> modifyABean(@PathVariable("lessonid") String lessonid,
                                                           @RequestBody Lesson lesson,
                                                           @RequestHeader(value="Authentication") String authentication) {
        return super.modifyABean(lessonid, lesson, authentication);
    }


    /*删--删除lesson*/
    @Override
    String delConcreteBean(String keyword) {
        return userService.deleteLesson(keyword);
    }

    @Override
    String delConcreteBean(String keyword, String name) {
        return userService.deleteLesson(keyword, name);
    }

    @Override
    @RequestMapping(value = "/lesson/{lessonid}", method = RequestMethod.DELETE)
    public ResponseEntity<Map<String, Object>> delBean(@PathVariable("lessonid") String lessonid,
                                                       @RequestHeader("Authentication") String authentication) {
        return super.delBean(lessonid, authentication);
    }




}
