package com.example.demo.utils;



import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class BeanTools {
    public static <T> T modify(T originBean, T modifiedBean, List<String> changableKeys){//可变参数数量
        //System.out.println("method");
        try{
            for(String name:changableKeys){
                String methodname_get = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
                //System.out.println(methodname_get);
                //System.out.println(BeanTools.get(modifiedBean,methodname_get));
                //System.out.println("set"+name.substring(0, 1).toUpperCase() + name.substring(1));
                if(!(BeanTools.get(modifiedBean,
                        methodname_get) instanceof Integer)
                        && BeanTools.get(modifiedBean,
                        methodname_get) == null){
                }
                else{
                    if(BeanTools.get(modifiedBean,
                            methodname_get) instanceof Integer){
                        BeanTools.setProperty(originBean,name,BeanTools.get(modifiedBean, methodname_get));
                        continue;
                    }

                    if(BeanTools.get(modifiedBean,
                            methodname_get) instanceof Enum){
                        BeanTools.setProperty(originBean,name,BeanTools.get(modifiedBean, methodname_get));
                        continue;
                    }

                    if(BeanTools.get(modifiedBean,
                            methodname_get) instanceof List)
                    {
                        BeanTools.setProperty(originBean,name,BeanTools.get(modifiedBean, methodname_get));
                        continue;
                    }

                    //System.out.println("run!");
                    BeanTools.set(originBean,"set"+name.substring(0, 1).toUpperCase() + name.substring(1),
                            BeanTools.get(modifiedBean, methodname_get));
                    //System.out.println("run:"+"set"+name.substring(0, 1).toUpperCase() + name.substring(1));
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




    public  static void setProperty(Object obj, String propertyName, Object value) throws Exception{
        Class<? extends Object> clazz = obj.getClass();

        /*//防止字段为private时，无法修改字段值的情况
        Field field = clazz.getDeclaredField(propertyName);//暴力反射获取字段
        field.setAccessible(true);//去除权限

        field.set(obj, value);*/

        /*
         * 内省机制
         */
        PropertyDescriptor propertyDescriptor = new PropertyDescriptor(propertyName, clazz);
        Method setMethod = propertyDescriptor.getWriteMethod();
        setMethod.invoke(obj, value);
        //System.out.println("内省机制: " );
    }




    //
    /*String className = "com.runqianapp.ngr.alias.example.FunClass";
    String methodName = "sayHello";
    Class clz = Class.forName(className);
    //
    Object obj = clz.newInstance();
    //获取方法
    Method m = obj.getClass().getDeclaredMethod(methodName, String.class);
    //调用方法
    String  result = (String) m.invoke(obj, "aaaaa");
  System.out.println(result);*/
}
