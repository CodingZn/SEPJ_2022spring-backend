package com.example.demo.bean;

import jdk.jshell.execution.Util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class BeanTools {
    public static <T> T modify(T originBean, T modifiedBean, List<String> changableKeys){//可变参数数量

        try{
            for(String name:changableKeys){
                if(BeanTools.get(modifiedBean,
                        "get"+name.substring(0, 1).toUpperCase() + name.substring(1))==null){
                }
                else{
                    BeanTools.set(originBean,"set"+name.substring(0, 1).toUpperCase() + name.substring(1),
                            BeanTools.get(modifiedBean,"get"+name.substring(0, 1).toUpperCase() + name.substring(1)));
                }
            }

            return originBean;
        }catch (Exception e){
            System.out.println("Exception: can't modify!");
            return null;
        }

    }

    private static <T> void set(T bean,String name,Object value) throws Exception {
        Method method = bean.getClass().getMethod(name,Class.forName(value.getClass().getCanonicalName()));
        method.invoke(bean, value);


    }
    //"set"+name.substring(0, 1).toUpperCase() + name.substring(1)

    private static<T> Object get(T bean,String name) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = bean.getClass().getMethod(name);
        return method.invoke(bean);
    }
    //"get"+name.substring(0, 1).toUpperCase() + name.substring(1)


}
