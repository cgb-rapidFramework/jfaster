package com.abocode.jfaster.core.common.util;

import com.abocode.jfaster.core.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.PropertyUtilsBean;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/***
 * Bean copy
 */
@Slf4j
public class BeanPropertyUtils extends PropertyUtilsBean {

    /**
     * 对象拷贝
     * 数据对象空值不拷贝到目标对象
     *
     * @param source
     * @param target
     * @throws NoSuchMethodException
     */
    public static void copyObjectToObject(Object source, Object target) {
        PropertyDescriptor[] origDescriptors = PropertyUtils.getPropertyDescriptors(source);
        for (int i = 0; i < origDescriptors.length; i++) {
            String name = origDescriptors[i].getName();
            if ("class".equals(name)) {
                continue;
            }
            if (PropertyUtils.isReadable(source, name) && PropertyUtils.isWriteable(target, name)) {
                try {
                    Object value = PropertyUtils.getSimpleProperty(source, name);
                    if (value != null) {
                        getInstance().setSimpleProperty(target, name, value);
                    }
                } catch (Exception e) {
                    throw new BusinessException("copy fail", e);
                }
            }
        }
    }


    /**
     * 自动转Map key值大写
     * 将Map内的key与Bean中属性相同的内容复制到BEAN中
     *
     * @param source Object
     * @param map    Map
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static void copyObjectToMap(Object source, Map<String, Object> map) throws
            IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        if ((source == null) || (map == null)) {
            return;
        }
        Iterator<String> names = map.keySet().iterator();
        while (names.hasNext()) {
            String name = names.next();
            Object value = map.get(name);
            Class clazz = PropertyUtils.getPropertyType(source, name);
            if (value == null || null == clazz) {
                continue;
            }
            String className = clazz.getName();
            if (className.equalsIgnoreCase("java.util.Date")) {
                value = new java.util.Date(((java.sql.Timestamp) value).getTime());// wait to do：貌似有时区问题, 待进一步确认
            }
            getInstance().setSimpleProperty(source, name, value);
        }
    }


    public static <T> T toEntityList(List<?> objects, Class<?> clas, String... labels) {
        // 返回实体列表
        List<T> entitys = new ArrayList<>();
        for (int i = 0; i < objects.size(); i++) {
            // 结果集对象
            Object[] data = (Object[]) objects.get(i);
            T entity;
            try {
                entity = toEntity(clas, data, labels);
                entitys.add(entity);
            } catch (Exception e) {
                log.error(e.getMessage());
            }

        }
        return (T) entitys;
    }

    public static <T> T toEntity(Class<?> entity, Object[] data, String... labels) throws NoSuchFieldException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // 每个字段进行赋值
        for (int j = 0; j < labels.length; j++) {
            if (null == data[j] || 0 == data[j].toString().trim().length()) {
                continue;
            }
            Field field = entity.getClass().getDeclaredField(labels[j]);
            // 得到字段类型
            String parameterType = field.getType().getSimpleName();
            String methodName = "set"
                    + ((labels[j].charAt(0) + "").toUpperCase()) + labels[j].substring(1);
            Method method = entity.getClass().getDeclaredMethod(methodName,
                    field.getType());
            String val = data[j].toString();
            invoke(entity, parameterType, method, val);
        }
        return (T) entity;
    }

    private static <T> void invoke(T entity, String parameterType, Method method, String val) throws InvocationTargetException, IllegalAccessException {

        if (parameterType.equals("String")) {
            method.invoke(entity, val);
        } else if (parameterType.equals("Character")) {
            method.invoke(entity, val.charAt(0));
        } else if (parameterType.equals("Boolean")) {
            method.invoke(entity, val.equals("true") || val.equals("1"));
        } else if (parameterType.equals("Short")) {
            method.invoke(entity, Short.parseShort(val));
        } else if (parameterType.equals("Integer")) {
            method.invoke(entity, Integer.parseInt(val));
        } else if (parameterType.equals("Float")) {
            method.invoke(entity, Float.parseFloat(val));
        } else if (parameterType.equals("Long")) {
            method.invoke(entity, Long.parseLong(val));
        } else if (parameterType.equals("Double")) {
            method.invoke(entity, Double.parseDouble(val));
        }
    }
}
