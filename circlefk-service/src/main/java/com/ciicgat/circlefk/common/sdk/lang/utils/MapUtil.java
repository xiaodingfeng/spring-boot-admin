package com.ciicgat.circlefk.common.sdk.lang.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class MapUtil {

    public static Map<String,Object> beanToMap(Object object){
        if (object == null) {
            return null;
        }
        Map<String,Object> map = new HashMap<>();
        Field[] fields =object.getClass().getDeclaredFields();

        for (Field field:fields){
            field.setAccessible(true);
            try {
                map.put(field.getName(),field.get(object));
            } catch (IllegalAccessException e) {
                throw new RuntimeException();
            }
        }
        return map;
    }

    public static Object mapToObject(Map<String,Object> map,Class<?> clazz){
        if(map == null || map.isEmpty()){
            return null;
        }
        Object obj = null;
        try {
            //使用newInstance来创建对象
            obj= clazz.newInstance();
            //获取类中的所有字段
            Field[] fields = obj.getClass().getDeclaredFields();

            for (Field field:fields){
                int modifiers = field.getModifiers();
                //判断是否拥有某个修饰符
                if (Modifier.isFinal(modifiers) || Modifier.isStatic(modifiers)){
                    continue;
                }
                field.setAccessible(true);
                Class<?> type = field.getType();
                if (type==String.class){
                    field.set(obj,map.get(field.getName()));
                }
                if (type== int.class){
                    BigDecimal b=(BigDecimal) map.get(field.getName());
                    field.set(obj,b.intValue());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }
        return obj;
    }
}
