package com.example.demo.service;

import com.example.demo.bean.specialBean.LessonQuery;
import com.example.demo.mapper.LessonMapper;

public class LessonQueryService {
    private final LessonMapper lessonMapper;

    public LessonQueryService(LessonMapper lessonMapper) {
        this.lessonMapper = lessonMapper;
    }

    public LessonQuery processAQuery(LessonQuery query){
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
        return null;
    }

}
