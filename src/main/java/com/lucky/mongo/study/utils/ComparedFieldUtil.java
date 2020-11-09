package com.lucky.mongo.study.utils;

import com.google.common.collect.Lists;
import com.lucky.mongo.study.annotations.FieldCommentAnnotation;
import com.lucky.mongo.study.domain.SysUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.*;

/**
 * @author: liangtf
 * @description: 对比两个对象属性变化工具
 * @date: 2020/11/5 16:49
 */
public class ComparedFieldUtil {

    private static final Logger logger = LoggerFactory.getLogger(ComparedFieldUtil.class);

    /** 比较两个对象属性的变化 */
    public static Map<String, List<Object>> comparedField(Object before, Object after, String... ignoreFields) {
        Map<String, List<Object>> resultDiffFieldValueMap = new LinkedHashMap<>();
        // 比较对象应为同一对象
        if (before.getClass() != after.getClass()) return resultDiffFieldValueMap;
        List<String> ignoreFieldList = Lists.newLinkedList();
        if (ignoreFields != null && ignoreFields.length > 0) ignoreFieldList.addAll(Arrays.asList(ignoreFields));
        Class clazz = before.getClass();
        try {
            // 获取传入对象的属性: getXxx() 这种Xxx叫属性 不包括Object类中getClass()
            PropertyDescriptor[] pds = Introspector.getBeanInfo(clazz, Object.class).getPropertyDescriptors();
            for (PropertyDescriptor pd : pds) {
                String field = pd.getName();
                // 忽略指定属性比较值得变化
                if (ignoreFieldList.contains(field)) continue;
                // 获取get方法
                Method readMethod = pd.getReadMethod();
                // 在before上调用get方法获得before中属性的值
                Object beforeValue = readMethod.invoke(before);
                // 在after上调用get方法获得after中属性的值
                Object afterValue = readMethod.invoke(after);
                // 校验值得变化
                if (!checkValue(beforeValue, afterValue)) continue;
                resultDiffFieldValueMap.put(field, Arrays.asList(beforeValue, afterValue));
            }
        } catch (Exception e) {
            logger.error("ComparedFieldUtil.comparedField.error:", e);
        }
        return resultDiffFieldValueMap;
    }

    private static boolean checkValue(Object beforeValue, Object afterValue) {
        if (beforeValue instanceof Timestamp) beforeValue = new Date(((Timestamp) beforeValue).getTime());
        if (afterValue instanceof Timestamp) afterValue = new Date(((Timestamp) afterValue).getTime());
        if (beforeValue == null && afterValue != null) return true;
        if (beforeValue != null && !beforeValue.equals(afterValue)) return true;
        return false;
    }

    /** 获取字段中的注解值 */
    public static String getFieldComment(String fieldName, Class clazz) {
        String fieldComment = "";
        try {
            Field field = clazz.getDeclaredField(fieldName);
            FieldCommentAnnotation fieldCommentAnnotation = field.getAnnotation(FieldCommentAnnotation.class);
            if (fieldCommentAnnotation != null) fieldComment = fieldCommentAnnotation.value();
        } catch (NoSuchFieldException e) {
            logger.error("ComparedFieldUtil.getFieldComment.error:", e);
        }
        return fieldComment;
    }

    public static void main(String[] args) {

        SysUser sysUserBefore = new SysUser();

        SysUser sysUserAfter = new SysUser();

        Map<String, List<Object>> comparedField = comparedField(sysUserBefore, sysUserAfter);
        for (Map.Entry<String, List<Object>> entry : comparedField.entrySet()) {
            String field = entry.getKey();
            List<Object> value = entry.getValue();
            String beforeValue = JsonUtil.object2Str(value.get(0));
            String afterValue = JsonUtil.object2Str(value.get(1));
            String fieldComment = getFieldComment(field, SysUser.class);
            logger.info("{}, {}, {}, {}", field, fieldComment, beforeValue, afterValue);
        }

    }
}
