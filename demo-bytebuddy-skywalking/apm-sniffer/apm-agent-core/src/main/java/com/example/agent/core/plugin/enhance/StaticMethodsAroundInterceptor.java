package com.example.agent.core.plugin.enhance;

import java.lang.reflect.Method;

/**
 * 静态方法的的拦截器必须实现这个接口
 *
 * @author wangweijun
 * @since 2023/9/20 01:05
 */
public interface StaticMethodsAroundInterceptor {

    /**
     * 前置通知
     *
     * @param clazz
     * @param method
     * @param args
     * @param argTypes
     * @author wangweijun
     * @since 2023/9/20 01:10
     */
    void beforeMethod(Class<?> clazz, Method method, Object[] args, Class<?>[] argTypes);

    /**
     * 后置通知，无论是否出现异常都会执行
     *
     * @param clazz
     * @param method
     * @param args
     * @param argTypes
     * @param result
     * @return Object
     * @author wangweijun
     * @since 2023/9/20 01:10
     */
    Object afterMethod(Class<?> clazz, Method method, Object[] args, Class<?>[] argTypes, Object result);

    /**
     * 前置通知
     *
     * @param clazz
     * @param method
     * @param args
     * @param argTypes
     * @param result
     * @param throwable
     * @author wangweijun
     * @since 2023/9/20 01:10
     */
    void handleException(Class<?> clazz, Method method, Object[] args, Class<?>[] argTypes, Object result, Throwable throwable);

}
