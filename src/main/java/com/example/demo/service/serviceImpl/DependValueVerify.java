package com.example.demo.service.serviceImpl;

import com.example.demo.bean.Lesson;
import com.example.demo.bean.User;
import com.example.demo.mapper.LessonMapper;
import com.example.demo.mapper.MajorMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.mapper.straightMappers.ClassroomMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DependValueVerify {

    /* 类功能：对实体成员中的依赖属性进行验证，对不合法的数据做出拦截 */

    private final MajorMapper majorMapper;
    private final UserMapper userMapper;
    private final LessonMapper lessonMapper;
    private final ClassroomMapper classroomMapper;

    @Autowired
    public DependValueVerify(MajorMapper majorMapper, UserMapper userMapper, LessonMapper lessonMapper, ClassroomMapper classroomMapper) {
        this.majorMapper = majorMapper;
        this.userMapper = userMapper;
        this.lessonMapper = lessonMapper;
        this.classroomMapper = classroomMapper;
    }


    /*************课程检查****************/

    public boolean lessonDependCheck(Lesson lesson){
        return true;
    }

    private  boolean lessonSchoolVerify(String school){
        System.out.println(majorMapper.findFirstBySchool(school));
        return majorMapper.findFirstBySchool(school) != null;
    }

    private boolean lessonTeacherVerify(String teacher){
        System.out.println(userMapper.findFirstByUsertypeAndName("teacher", teacher));
        return userMapper.findFirstByUsertypeAndName("teacher", teacher) != null;
    }

    private  boolean lessonPlaceVerify(String place){
        System.out.println(place);
        System.out.println(classroomMapper.findByNameAndStatus(place, "enabled"));
        return classroomMapper.findByNameAndStatus(place, "enabled") != null;
    }

    private boolean lessonPeriodVerify(String period){
        final String REGEX_LESSON_PERIOD = "([一二三四五六日]([1-9]|1[0-4]),)*([一二三四五六日]([1-9]|1[0-4]))+";
        System.out.println(period.matches(REGEX_LESSON_PERIOD));

        return period.matches(REGEX_LESSON_PERIOD);
    }


    /*************用户检查****************/

    public boolean userDependCheck(User user){
        return true;
    }

    private boolean userMajorVerify(String major){
        return majorMapper.findFirstByName(major) != null;
    }

    private boolean userSchoolVerify(String school){
        return majorMapper.findFirstBySchool(school) != null;
    }

}
