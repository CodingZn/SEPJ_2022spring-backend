package com.example.demo.service.serviceImpl;

import com.example.demo.bean.Lesson;
import com.example.demo.bean.Major;
import com.example.demo.mapper.LessonMapper;
import com.example.demo.mapper.MajorMapper;
import com.example.demo.mapper.straightMappers.ClassroomMapper;
import com.example.demo.service.UserService;
import com.example.demo.bean.UserBean;
import com.example.demo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final MajorMapper majorMapper;
    private final LessonMapper lessonMapper;
    DependValueVerify dependValueVerify;

    //将DAO(Mapper)层注入Service层
    @Autowired
    public UserServiceImpl(UserMapper userMapper, MajorMapper majorMapper, LessonMapper lessonMapper, ClassroomMapper classroomMapper) {
        this.userMapper = userMapper;
        this.majorMapper = majorMapper;
        this.lessonMapper = lessonMapper;
        dependValueVerify = new DependValueVerify(majorMapper, userMapper, lessonMapper, classroomMapper);
    }

    @Override
    public void createAdmin() {//判断数据库中是否有管理员，若无，自动生成
        UserBean userbean = userMapper.findByUsertype("admin");
        if (userbean == null) {
            UserBean admin = new UserBean();
            admin.setUsertype("admin");
            admin.setSchoolnumber("10001");
            admin.setName("admin");
            admin.setPassword("admin666");
            admin.setIdentitynumber("310101197001010011");
            userMapper.save(admin);
            System.out.println("Successfully created initial administrator!");
        }
    }

    @Override
    public UserBean login(String schoolnumber, String password) {
        return userMapper.findBySchoolnumberAndPassword(schoolnumber, password);
    }

    @Override
    public String createAUser(UserBean userBean) {
        String schoolnumber, newidentitynumber;
        UserBean a, b;
        schoolnumber = userBean.getSchoolnumber();
        newidentitynumber = userBean.getIdentitynumber();
        a = userMapper.findBySchoolnumber(schoolnumber);
        b = userMapper.findByIdentitynumber(newidentitynumber);
        if (a != null)
            return "Conflict";
        else if (b != null)
            return "Conflict";
        else {
            userBean.setPassword("fDu666666");//统一设置初始密码
            //检查数据格式与依赖属性
            if (!userBean.verifyform()) {
                return "FormError";
            }
            else if (!dependValueVerify.userDependCheck(userBean)){
                return "DependError";
            }
            else{
                userMapper.save(userBean);
                return "Success";
            }


        }


    }

    @Override
    public List<String> getAllSchoolnumbers() {
        List<UserBean> userBeanList = userMapper.findAll();
        return userBeanList.stream().map(UserBean::getSchoolnumber).toList();
    }

    @Override
    public UserBean getAUser(String schoolnumber) {
        return userMapper.findBySchoolnumber(schoolnumber);
    }

    @Override
    public String rewriteUser(UserBean userBean){

        String id_old = userMapper.findBySchoolnumber(userBean.getSchoolnumber()).getId();
        userBean.setId(id_old);//保证id不变，是修改而非新增
        if (!userBean.verifyform()) {
            return "FormError";
        }
        else if (!dependValueVerify.userDependCheck(userBean)){
            return "DependError";
        }
        else{
            userMapper.save(userBean);
            return "Success";
        }
    }

    @Override
    public String deleteUser(String schoolnumber) {
        UserBean userBean = userMapper.findBySchoolnumber(schoolnumber);

        if (userBean != null) {
            userMapper.delete(userBean);
            return "Success";
        } else {
            return "NotFound";
        }
    }


    @Override
    public String getANewMajornumber() {//返回一个可用的majornumber(str)
        List<Major> majorList = majorMapper.findAll();
        Major maxmajor;
        if (majorList.stream().max(Comparator.comparing(Major::getMajornumber)).isPresent()){
            maxmajor = majorList.stream().max(Comparator.comparing(Major::getMajornumber)).get();
            System.out.println("getMaxMajornumber=");
            System.out.println(maxmajor.getMajornumber());
            return String.valueOf(maxmajor.getMajornumber() + 1);
        }
        else return "1";



    }

    @Override
    public List<String> getAllMajornumbers() {

        List<Major> majorList = majorMapper.findAll();

        List<String> a = majorList.stream().map( u -> String.valueOf(u.getMajornumber())).toList();

        System.out.println("a"+a);

        return a;
    }

    @Override
    public Major getAMajor(int majornumber) {
        return majorMapper.findByMajornumber(majornumber);
    }

    @Override
    public Major getAMajor(String majornumber_str) {
        Major major;
        try {
            int majornumber = Integer.parseInt(majornumber_str);
            major = getAMajor(majornumber);
            return major;
        } catch (NumberFormatException e) {//传入majornumber格式不对
            return null;
        }
    }

    @Override
    public String createAMajor(String majornumber_str, Major major) {

        Major major1 = getAMajor(majornumber_str);
        if (major1 == null) {
            try{
                major.setMajornumber(Integer.parseInt(majornumber_str));
            }
            catch (Exception e){
                return "FormError";
            }
            if (major.getName().equals("") || major.getSchool().equals(""))  return "FormError";
            majorMapper.save(major);
            return "Success";

        }
        else{
            return "Conflict";
        }


    }

    @Override
    public String rewriteAMajor(String majornumber_str, Major major) {//创建一个对象，若主键已存在，则修改对象的值
        Major major1 = getAMajor(majornumber_str);
        if (major1 == null) {
            return "NotFound";
        }

        if (major.getName().equals("") || !major.getSchool().equals(""))
            return "FormError";

        major.setMajornumber(major1.getMajornumber());
        majorMapper.save(major);
        return "Success";
    }

    @Override
    public String deleteMajor(int majornumber) {//删除一个对象

        Major major = majorMapper.findByMajornumber(majornumber);

        if (major != null) {
            majorMapper.delete(major);
            return "Success";
        } else {
            return "NotFound";
        }
    }

    @Override
    public String deleteMajor(String majornumber_str) {

        int majornumber;
        try {
            majornumber = Integer.parseInt(majornumber_str);
        } catch (NumberFormatException e) {//传入majornumber格式不对
            return "NotFound";
        }
        Major major = majorMapper.findByMajornumber(majornumber);
        if (major != null) {
            majorMapper.delete(major);
            return "Success";
        } else {
            return "NotFound";
        }
    }


    @Override
    public String getANewLessonid() {
        List<Lesson> lessonList = lessonMapper.findAll();

        Lesson maxlesson;
        if (lessonList.stream().max(Comparator.comparing(Lesson::getLessonid)).isPresent()){
            maxlesson = lessonList.stream().max(Comparator.comparing(Lesson::getLessonid)).get();
            System.out.println("getMaxMajornumber=");
            System.out.println(maxlesson.getLessonid());

            return String.valueOf(maxlesson.getLessonid() + 1);
        }
        else
            return "1";

    }

    @Override
    public List<String> getAllLessonid() {
        List<Lesson> lessonList = lessonMapper.findAll();

        List<String> a = lessonList.stream().map( u -> String.valueOf(u.getLessonid())).toList();

        System.out.println("a"+a);

        return a;
    }

    @Override
    public List<String> getAllLessonid(String teacherName, boolean showall) {
        List<Lesson> lessonList = lessonMapper.findAllByTeacherOrStatus(teacherName, "censored");
        return lessonList.stream().map(u -> String.valueOf(u.getLessonid())).toList();
    }

    @Override
    public List<String> getAllLessonid(boolean showAll) {
        List<Lesson> lessonList = lessonMapper.findAllByStatus("censored");
        return lessonList.stream().map(u -> String.valueOf(u.getLessonid())).toList();
    }

    @Override
    public Lesson getALesson(int lessonid) {
        return lessonMapper.findByLessonid(lessonid);
    }

    @Override
    public Lesson getALesson(String lessonid_str) {
        Lesson lesson;
        try {
            int lessonid = Integer.parseInt(lessonid_str);
            lesson = getALesson(lessonid);
            return lesson;
        } catch (NumberFormatException e) {//传入lessonid格式不对
            return null;
        }
    }

    @Override
    public String createALesson(String lessonid_str, Lesson lesson) {
        Lesson lesson1 = getALesson(lessonid_str);
        if (lesson1 == null){
            lesson.setLessonid(Integer.parseInt(lessonid_str));
            if ((lesson.getLessonname().equals("") || lesson.getSchool().equals("")))
                return "FormError";
            else if (!dependValueVerify.lessonDependCheck(lesson))
                return "DependError";
            else{
                lessonMapper.save(lesson);
                return "Success";
            }
        }
        else return "Conflict";
    }

    @Override
    public String rewriteALesson(String lessonid_str, Lesson lesson) {

        Lesson lesson1 = getALesson(lessonid_str);
        if (lesson1 == null)
            return "NotFound";
        else{
            lesson.setLessonid(Integer.parseInt(lessonid_str));
            if ((lesson.getLessonname().equals("") || lesson.getSchool().equals("")))
                return "FormError";
            else if (!dependValueVerify.lessonDependCheck(lesson))
                return "DependError";
            else{
                lessonMapper.save(lesson);
                return "Success";
            }
        }
    }

    @Override
    public String deleteLesson(int lessonid) {
        Lesson lesson = lessonMapper.findByLessonid(lessonid);

        if (lesson != null) {
            lessonMapper.delete(lesson);
            return "Success";
        } else {
            return "NotFound";
        }
    }

    @Override
    public String deleteLesson(String lessonid_str) {
        int lessonid;
        try {
            lessonid = Integer.parseInt(lessonid_str);
        } catch (NumberFormatException e) {//传入 lessonid_str 格式不对
            return "NotFound";
        }
        Lesson lesson = lessonMapper.findByLessonid(lessonid);
        if (lesson != null) {
            lessonMapper.delete(lesson);
            return "Success";
        } else {
            return "NotFound";
        }
    }

    @Override
    public String deleteLesson(String lessonid_str, String name) {
        int lessonid;
        try {
            lessonid = Integer.parseInt(lessonid_str);
        } catch (NumberFormatException e) {//传入 lessonid_str 格式不对
            return "NotFound";
        }
        Lesson lesson = lessonMapper.findByLessonid(lessonid);
        if (lesson != null && Objects.equals(lesson.getTeacher(), name)) {
            lessonMapper.delete(lesson);
            return "Success";
        } else {
            return "NotFound";
        }
    }


}