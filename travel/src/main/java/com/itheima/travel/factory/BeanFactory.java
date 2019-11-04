package com.itheima.travel.factory;

import java.util.ResourceBundle;

/**
 * 包名:com.itheima.factory
 * 作者:Leevi
 * 日期2019-06-06  17:58
 */
public class BeanFactory {
    /**
     * 根据类的全限定名创建对象
     * @param
     * @return
     */
    /*public static Object getBean(String className){
        try {
            Class<?> clazz = Class.forName(className);
            return clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }*/
    public static Object getBean(String key){
        //解析beans.properties,根据key获取全限定名
        ResourceBundle bundle = ResourceBundle.getBundle("beans");
        String className = bundle.getString(key);
        try {
            Class<?> clazz = Class.forName(className);
            return clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
}
