package com.example.agent.core.plugin.enhance;

import java.lang.reflect.Method;

/**
 * 实例方法的的拦截器必须实现这个接口
 *
 * @author wangweijun
 * @since 2023/9/20 01:05
 */
public interface InstanceMethodsAroundInterceptor {

    /**
     * 前置通知
     *
     * @param instance
     * @param method
     * @param args
     * @param argTypes
     * @author wangweijun
     * @since 2023/9/20 01:10
     */
    void beforeMethod(EnhancedInstance instance, Method method, Object[] args, Class<?>[] argTypes);

    /**
     * 后置通知，无论是否出现异常都会执行
     *
     * @param instance
     * @param method
     * @param args
     * @param argTypes
     * @param result
     * @return Object
     * @author wangweijun
     * @since 2023/9/20 01:10
     */
    Object afterMethod(EnhancedInstance instance, Method method, Object[] args, Class<?>[] argTypes, Object result);

    /**
     * 前置通知
     *
     * @param instance
     * @param method
     * @param args
     * @param argTypes
     * @param result
     * @param throwable
     * @author wangweijun
     * @since 2023/9/20 01:10
     */
    void handleException(EnhancedInstance instance, Method method, Object[] args, Class<?>[] argTypes, Object result, Throwable throwable);

}
