package com.example.demo.bean;

import java.util.List;

public class BeanTools {
    public static <T> T modify(T originBean, T modifiedBean, List<String> changableKeys){
        /*
        * 此方法将修改传入的实体的成员
        * 其具体逻辑为：
        * 1.考察 originBean 中所有的成员，当该成员在 changableKeys 列表中时，才可修改
        * 2.当一个成员可修改，但对应 modifiedBean 中对应成员为null时，不做修改，即只能将成员修改为非空值（Notnull
        * 3.返回修改好的实体*/
        return null;
    }
}
