package com.hutu.shortlinkcommon.util;

import com.hutu.shortlinkcommon.enums.BizCodeEnum;
import com.hutu.shortlinkcommon.exception.BizException;

import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;

/**
 * 断言工具类，用于参数校验等场景，校验失败抛出 BizException
 */
public final class AssertUtils {

    private AssertUtils() {
        // 工具类禁止实例化
    }

    /**
     * 断言表达式为true
     * @param expression 布尔表达式
     * @param errorCode 错误码枚举
     */
    public static void isTrue(boolean expression, BizCodeEnum errorCode) {
        if (!expression) {
            throw new BizException(errorCode);
        }
    }

    /**
     * 断言表达式为true
     * @param expression 布尔表达式
     * @param messageSupplier 错误消息提供者
     */
    public static void isTrue(boolean expression, Supplier<String> messageSupplier) {
        if (!expression) {
            throw new BizException(null, nullSafeGet(messageSupplier));
        }
    }

    /**
     * 断言表达式为false
     * @param expression 布尔表达式
     * @param errorCode 错误码枚举
     */
    public static void isFalse(boolean expression, BizCodeEnum errorCode) {
        if (expression) {
            throw new BizException(errorCode);
        }
    }

    /**
     * 断言对象为null
     * @param object 对象
     * @param errorCode 错误码枚举
     */
    public static void isNull(Object object, BizCodeEnum errorCode) {
        if (object != null) {
            throw new BizException(errorCode);
        }
    }

    /**
     * 断言对象不为null
     * @param object 对象
     * @param errorCode 错误码枚举
     */
    public static void notNull(Object object, BizCodeEnum errorCode) {
        if (object == null) {
            throw new BizException(errorCode);
        }
    }

    /**
     * 断言字符串不为空
     * @param text 字符串
     * @param errorCode 错误码枚举
     */
    public static void notEmpty(String text, BizCodeEnum errorCode) {
        if (text == null || text.isEmpty()) {
            throw new BizException(errorCode);
        }
    }

    /**
     * 断言字符串包含内容（不为空且非空白）
     * @param text 字符串
     * @param errorCode 错误码枚举
     */
    public static void hasText(String text, BizCodeEnum errorCode) {
        if (text == null || text.trim().isEmpty()) {
            throw new BizException(errorCode);
        }
    }

    /**
     * 断言集合不为空
     * @param collection 集合
     * @param errorCode 错误码枚举
     */
    public static void notEmpty(Collection<?> collection, BizCodeEnum errorCode) {
        if (collection == null || collection.isEmpty()) {
            throw new BizException(errorCode);
        }
    }

    /**
     * 断言Map不为空
     * @param map Map
     * @param errorCode 错误码枚举
     */
    public static void notEmpty(Map<?, ?> map, BizCodeEnum errorCode) {
        if (map == null || map.isEmpty()) {
            throw new BizException(errorCode);
        }
    }

    /**
     * 断言数组不为空
     * @param array 数组
     * @param errorCode 错误码枚举
     */
    public static void notEmpty(Object[] array, BizCodeEnum errorCode) {
        if (array == null || array.length == 0) {
            throw new BizException(errorCode);
        }
    }

    /**
     * 断言对象是给定类型的实例
     * @param type 类型
     * @param obj 对象
     * @param errorCode 错误码枚举
     */
    public static void isInstanceOf(Class<?> type, Object obj, BizCodeEnum errorCode) {
        notNull(type, BizCodeEnum.PARAM_ERROR);
        if (!type.isInstance(obj)) {
            throw new BizException(errorCode);
        }
    }

    /**
     * 断言两个对象相等
     * @param expected 期望值
     * @param actual 实际值
     * @param errorCode 错误码枚举
     */
    public static void equals(Object expected, Object actual, BizCodeEnum errorCode) {
        if (expected == null && actual == null) {
            return;
        }
        if (expected != null && expected.equals(actual)) {
            return;
        }
        throw new BizException(errorCode);
    }

    private static String nullSafeGet(Supplier<String> messageSupplier) {
        return (messageSupplier != null ? messageSupplier.get() : null);
    }
}