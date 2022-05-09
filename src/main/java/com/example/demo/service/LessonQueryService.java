package com.example.demo.service;

import com.example.demo.bean.Classarrange;
import com.example.demo.bean.Lesson;
import com.example.demo.bean.User;
import com.example.demo.bean.specialBean.LessonQuery;
import com.example.demo.mapper.LessonMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LessonQueryService {
    private final LessonMapper lessonMapper;

    public LessonQueryService(LessonMapper lessonMapper) {
        this.lessonMapper = lessonMapper;
    }

    public LessonQuery processAQuery(LessonQuery query) {
        List<Lesson> lessonList = lessonMapper.findAll();
        List<Lesson> lessonList1 = new ArrayList<>();
        List<Lesson> lessonList2 = new ArrayList<>();

        for (Lesson lesson : lessonList) {
            if (query.getSemester() == null || lesson.getSemester().equals(query.getSemester())) {
                int flag = 0;
                for (Classarrange classarrange : lesson.getArranges()) {
                    if (classarrange.getClassroom().getName().equals(query.getClassroom_name())
                            || query.getClassroom_name() == null) {
                        if (classarrange.getClasstime().getName().equals(query.getClasstime_name())
                                || query.getClasstime_name() == null) {
                            flag = 1;
                        }
                    }
                }
                if (1 == flag) {
                    lessonList1.add(lesson);
                }
            }
        }

        for (Lesson lesson : lessonList) {
            boolean a = query.getFuzzyLessonCode().equals(lesson.getLessoncode());//judgeStrings(query.getFuzzyLessonCode(), lesson.getLessoncode());
            boolean b = judgeStrings(query.getFuzzyLessonName(), lesson.getLessonname());
            boolean c = false;
            for (User teacher : lesson.getTeacher()) {
                if (judgeStrings(query.getFuzzyLessonTeacherName(), teacher.getName()))
                    c = true;
            }
            if (a && b && c)
                lessonList2.add(lesson);
        }

        lessonList1.retainAll(lessonList2);

        query.setResult(lessonList1);

        return query;
        /*
         * 要求：
         * 处理传入的query，按照要求对数据库进行搜索，将搜索结果放到 result 里面，返回整个对象
         *
         * 说明：
         * 实体类中，各String字段为查询字段，
         * 成员名前带"fuzzy"的代表模糊搜索（子字符串匹配或近似搜索）！
         * 不带fuzzy代表精确搜索（整个字符串完全匹配）！
         * String字段为null，代表不搜索该字段
         *
         * 模糊搜索：自行理解其具体如何“模糊”，也可以引入相关算法的代码
         * （我个人的理解，对于课程代号要用精确匹配子字符串，对于名称类的可以使用近似匹配，
         * 近似匹配的搜索要设定相关度评判）
         *
         * 条件关系：
         * 对于所有采用字符串（或子字符串）完全匹配来进行搜索的字段，其之间应该严格取交集
         * 对于采用模糊近似搜索的选项，按照相关度排序，设定一个阈值，只返回固定比例的结果
         * 最终的结果应该是两种类型搜索的交集
         *
         * 未详细规定的部分可以灵活处理，记得及时沟通
         *
         * */

    }

    public boolean judgeStrings(String source, String target) {
        char[] sources = source.toCharArray();
        char[] targets = target.toCharArray();
        int sourceLen = sources.length;
        int targetLen = targets.length;
        int[][] d = new int[sourceLen + 1][targetLen + 1];
        for (int i = 0; i <= sourceLen; i++) {
            d[i][0] = i;
        }
        for (int i = 0; i <= targetLen; i++) {
            d[0][i] = i;
        }

        for (int i = 1; i <= sourceLen; i++) {
            for (int j = 1; j <= targetLen; j++) {
                if (sources[i - 1] == targets[j - 1]) {
                    d[i][j] = d[i - 1][j - 1];
                } else {
                    //插入
                    int insert = d[i][j - 1] + 1;
                    //删除
                    int delete = d[i - 1][j] + 1;
                    //替换
                    int replace = d[i - 1][j - 1] + 1;
                    d[i][j] = Math.min(Math.min(insert, delete), Math.min(delete, replace));
                }
            }
        }

        int distance = d[sourceLen][targetLen];
        double score = (double) distance / Math.max(source.length(), target.length());
        return score < 0.4;
    }

}
