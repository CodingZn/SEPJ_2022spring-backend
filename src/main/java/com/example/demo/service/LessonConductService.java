package com.example.demo.service;

import com.alibaba.fastjson.JSON;
import com.example.demo.bean.Classarrange;
import com.example.demo.bean.Classtime;
import com.example.demo.bean.Lesson;
import com.example.demo.bean.User;
import com.example.demo.mapper.LessonMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.mapper.straightMappers.UltimatecontrolMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.example.demo.bean.specialBean.Ultimatectrl.*;

@Service
public class LessonConductService {
    private final UserMapper userMapper;
    private final LessonMapper lessonMapper;
    private final UltimatecontrolMapper controls;

    @Autowired
    public LessonConductService(UserMapper userMapper, LessonMapper lessonMapper, UltimatecontrolMapper controls) {
        this.userMapper = userMapper;
        this.lessonMapper = lessonMapper;
        this.controls = controls;
    }

    //学生选课--此方法返回 "Success" 或提示信息
    public String selectALesson(String userid, String lessonid) {
        User user = userMapper.findByUserid(userid);
        Lesson lesson = lessonMapper.findByLessonid(Integer.parseInt(lessonid));
        String now_semester = controls.findByName(KEY_SEMESTER_CONTROL).getValue();
        String classcontrol = controls.findByName(KEY_CLASS_CONTROL).getValue();
        if (user == null)
            return "无此用户！";
        if (lesson == null || lesson.getStatus() == Lesson.Status.pending)
            return "课程不存在！";
        if (classcontrol.equals(VALUE_CLASS_CONTROL_DISABLED))
            return "选课未开放！";
        if (!Objects.equals(lesson.getSemester(), now_semester))
            return "本学期不开放此课程！";
        if (!checkTakingConstraint(user, lesson))
            return "不能重复选择课程代码相同的课程！";
        if (!checkTakenConstraint(user, lesson))
            return "不能选择已经修过的课程！";
        if (!checkTimeArrangeConstraint(user, lesson))
            return "该课与其他课程存在时间冲突！";
        if (!checkMajorConstraint(user, lesson))
            return "您所在的年级专业不可选此课程！";

        switch (classcontrol) {
            case VALUE_CLASS_CONTROL_FIRST -> {

            }
            case VALUE_CLASS_CONTROL_SECOND -> {
                if (!checkCapacityConstraint(user, lesson))
                    return "课程容量已满，请关注课程余量！";
            }
            default -> {
                return "选课未开放！";
            }
        }
        lesson.getClassmates().add(user);
        lessonMapper.save(lesson);
        return "Success";
    }

    //学生退课--此方法返回 "Success" 或提示信息
    public String quitALesson(String userid, String lessonid) {
        User user = userMapper.findByUserid(userid);
        Lesson lesson = lessonMapper.findByLessonid(Integer.parseInt(lessonid));
        String now_semester = controls.findByName(KEY_SEMESTER_CONTROL).getValue();
        String classcontrol = controls.findByName(KEY_CLASS_CONTROL).getValue();
        if (user == null)
            return "无此用户！";
        if (lesson == null || lesson.getStatus() == Lesson.Status.pending)
            return "课程不存在！";
        if (classcontrol.equals(VALUE_CLASS_CONTROL_DISABLED))
            return "退课未开放！";
        if (!Objects.equals(lesson.getSemester(), now_semester))
            return "本学期不开放此课程！";
        if (!user.getLessonsTaking().contains(lesson))
            return "您没有选上此课程！";
        return "Success";
    }

    //学生查看所有已修课程
    public List<Lesson> getAllLessonsTaken(String userid){
        User user = userMapper.findByUserid(userid);
        return user.getLessonsTaken();
    }

    //学生查看所有已选课程
    public List<Lesson> getAllLessonsTaking(String userid){
        User user = userMapper.findByUserid(userid);
        return user.getLessonsTaking();
    }

    //管理员查看选课名单
    public List<String> getAllStudentIdsTakingLesson(String lessonid){
        Lesson lesson = lessonMapper.findByLessonid(Integer.parseInt(lessonid));
        return lesson.getClassmates().stream().map(User::getUserid).toList();
    }

    //教师查看选课名单，id不匹配时返回null
    public List<String> getAllStudentIdsTakingLesson(String lessonid, String teacherid){
        Lesson lesson = lessonMapper.findByLessonid(Integer.parseInt(lessonid));
        if (!lesson.getTeacher().stream().map(User::getUserid).toList().contains(teacherid))
            return null;
        return lesson.getClassmates().stream().map(User::getUserid).toList();
    }

    //管理员通过选课申请时，系统自动给学生选课--此方法返回 "Success" 或提示信息
    public String autoSelectALesson(User user, Lesson lesson) {
        lesson.getClassmates().add(user);
        lesson.setCapacity(lesson.getCapacity() + 1);
        lessonMapper.save(lesson);
        //具体逻辑改天再写，有关于选课申请通过时的合法性验证

        return "Success";
    }

    //一轮结束时，将超出课程容量的人踢掉
    public void kickAllExceededClassmates(){
        List<Lesson> lessonList = lessonMapper.findAll();

        for(Lesson lesson : lessonList){
            kickExceededClassmates(lesson);
        }
    }

    private void kickExceededClassmates(Lesson lesson) {
        System.out.println(JSON.toJSONString(lesson));System.out.println(1);
        if (!lesson.getMajorallowed().equals("all")) {
            lesson.getClassmates().removeIf(user -> {
                String a = user.getGrade()+"-"+user.getMajor().getMajorid();
                return !lesson.getMajorallowed().contains(a);
            });
        }//首先踢掉不符合专业限制条件的学生（正常情况应该没有）

        int amount = lesson.getClassmates().size();
        System.out.println(JSON.toJSONString(lesson));System.out.println(2);
        System.out.println("amount and capacity: ");
        System.out.println(amount);System.out.println(lesson.getCapacity());
        if (amount <= lesson.getCapacity()) {
            System.out.println(JSON.toJSONString(lesson));System.out.println(4);
            lessonMapper.save(lesson);
            return;
        }

        //X年级数量
        int[] amount_grade = new int[23];

        for (int grade = 22; grade >= 0; grade--) {

            amount_grade[grade] = 0;//初始化X年级学生数量
            String pat;
            if (grade < 10)
                pat = "0" + grade;
            else
                pat = "" + grade;

            for (User user : lesson.getClassmates()) {//获得X年级学生数量
                if (user.getGrade().equals(pat))
                    amount_grade[grade]++;
            }

            if ((amount - amount_grade[grade]) > lesson.getCapacity()) {//该年级全删完仍数量大于容量
                lesson.getClassmates().removeIf(user -> user.getGrade().equals(pat));

                amount = lesson.getClassmates().size();
            } else {//只需删除该年级部分学生

                //统计专业数
                List<String> majorname = new ArrayList<>();
                for (User user : lesson.getClassmates()) {

                    //仅统计X年级数据
                    if (!user.getGrade().equals(pat))
                        continue;

                    if (majorname.contains(user.getMajor().getMajorid()))
                        continue;
                    else
                        majorname.add(user.getMajor().getMajorid());
                }
                int amount_major = majorname.size();

                int numbersToDelete = amount - lesson.getCapacity();//待删除人数
                double p = 1.0 * numbersToDelete / amount_grade[grade];//被踢概率


                //统计各专业人数
                int[] numInMajor = new int[amount_major];
                for (User user : lesson.getClassmates()) {
                    //仅统计X年级数据
                    if (!user.getGrade().equals(pat))
                        continue;

                    numInMajor[majorname.indexOf(user.getMajor().getMajorid())]++;
                }

                //按专业删除
                Random r = new Random();
                for (int i = 0; i < amount_major; i++) {
                    int delnumInMajor = (int) (p * numInMajor[i]);//该专业删除人数
                    for (int j = 0; j < delnumInMajor; j++) {
                        int x = r.nextInt(numInMajor[i]);

//                        lesson.getClassmates().remove(x);
                        int flag = 0;
                        for (int k = 0; k < amount; k++) {
                            if (lesson.getClassmates().get(k).getGrade().equals(pat)
                                    && lesson.getClassmates().get(k).getMajor().getMajorid().equals(majorname.get(i))) {
                                if (flag == x) {
                                    lesson.getClassmates().remove(k);
                                    k--;
                                    amount--;
                                }
                                flag++;
                            }
                        }
                        numInMajor[i]--;
                    }
                }

                //上面计算每个专业要删除人数时数据类型转换，每个专业可能要删掉不足平均一个人，现从头开始每个专业删一个人至 数量 = 专业人数
                int i = 0;
                for (int k = 0; k < amount; k++) {

                    if (amount <= lesson.getCapacity())
                        break;

                    if (lesson.getClassmates().get(k).getGrade().equals(pat)
                            && lesson.getClassmates().get(k).getMajor().getMajorid().equals(majorname.get(i))) {

                        lesson.getClassmates().remove(k);
                        k--;

                        amount--;
                        i++;
                    }
                }
                lessonMapper.save(lesson);
                return;
            }
        }







        /*
         * 将某门课超过课程容量的学生踢掉
         *
         * 踢人原则：
         * 首先踢掉不符合专业限制条件的学生（正常情况应该没有）
         * 之后，如果课程容量小于学生人数，则按照下面规则踢人：
         * 优先保留高年级学生，即只在最低年级的学生中间踢，若踢光最低年级学生仍超量，则再从剩余学生中重复上述操作。
         * 课程设定仅有一种专业可选时，踢人时，年级相同的同学被踢掉的概率相等
         * 课程设定有多种专业可选或所有专业可选时，年级之间踢人的规则不变，同年级内踢人时，要保证每个专业同学被踢掉的概率近似相等
         * 若某门课没有专业限制，其对应属性用 "all" 表示
         *
         * 踢完人后应保证学生数量等于课程容量
         *
         * */

    }

    private boolean checkMajorConstraint(User user, Lesson lesson) {
        String user_majorid = user.getMajor().getMajorid();
        String user_grade = user.getGrade();
        String lesson_majorallowed = lesson.getMajorallowed();
        String user_info = user_grade + "-" + user_majorid;
        return lesson_majorallowed.contains(user_info);
    }

    private boolean checkCapacityConstraint(User user, Lesson lesson) {
        return (lesson.getClassmates().size() < lesson.getCapacity());
    }

    private boolean checkTakenConstraint(User user, Lesson lesson) {
        String lessoncode = lesson.getLessoncode();
        return (!user.getLessonsTaken().stream().map(Lesson::getLessoncode).toList().contains(lessoncode));
    }

    private boolean checkTakingConstraint(User user, Lesson lesson) {
        String lessoncode = lesson.getLessoncode();
        return (!user.getLessonsTaking().stream().map(Lesson::getLessoncode).toList().contains(lessoncode));
    }

    private boolean checkTimeArrangeConstraint(User user, Lesson lesson) {
        List<Classtime> classtimes_occupied = new ArrayList<>();
        List<Classtime> classtimes_required = new ArrayList<>();
        List<Lesson> lessonsTaking = user.getLessonsTaking();
        for (Lesson lesson_taking : lessonsTaking) {
            classtimes_occupied.addAll(lesson_taking.getArranges().stream().map(Classarrange::getClasstime).toList());
        }
        classtimes_required = lesson.getArranges().stream().map(Classarrange::getClasstime).toList();
        return Collections.disjoint(classtimes_occupied, classtimes_required);
    }

}
