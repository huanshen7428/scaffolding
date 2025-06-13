package com.huanshen.scaffolding.common.utils;

import cn.hutool.core.util.StrUtil;

import java.lang.reflect.Method;

/**
 * ReflectUtil
 *
 * @description 反射工具类
 * @date 2023-09-27 17:48
 */
public class ReflectUtil {
    private ReflectUtil() {
    }

    /**
     * 查询类的成员变量的getter方法<br>
     * 不存在则查询父类，直至Object, 返回null
     *
     * @param clazz
     * @param fieldName
     * @return
     */
    public static Method getGetterMethodByField(Class<?> clazz, String fieldName) {
        Method tMethod = null;
        while (clazz != Object.class) {
            try {
                tMethod = clazz.getDeclaredMethod(StrUtil.genGetter(fieldName));
            } catch (NoSuchMethodException ignore) {
            }
            if (tMethod != null) {
                return tMethod;
            }
            clazz = clazz.getSuperclass();
        }
        return tMethod;
    }

    public static Object getValueByField(Object obj, String fieldName) {
        Class<?> clazz = obj.getClass();
        Method tMethod = getGetterMethodByField(clazz, fieldName);
        if (tMethod == null) {
            return null;
        }
        Object tmpValue;
        try {
            tmpValue = tMethod.invoke(obj);
        } catch (Exception e) {
            return null;
        }
        return tmpValue;
    }
}
