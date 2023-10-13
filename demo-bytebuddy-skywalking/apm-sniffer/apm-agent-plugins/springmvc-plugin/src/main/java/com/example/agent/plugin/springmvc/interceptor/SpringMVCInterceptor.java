package com.example.agent.plugin.springmvc.interceptor;

import com.example.agent.core.plugin.enhance.EnhancedInstance;
import com.example.agent.core.plugin.enhance.InstanceMethodsAroundInterceptor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * SpringMVCInterceptor
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-09-19 00:27:21
 */
@Slf4j
public class SpringMVCInterceptor implements InstanceMethodsAroundInterceptor {
    @Override
    public void beforeMethod(EnhancedInstance instance, Method method, Object[] args, Class<?>[] argTypes) {
        log.info("before SpringMVC method: {}, args: {}", method.getName(), Arrays.toString(args));
    }

    @Override
    public Object afterMethod(EnhancedInstance instance, Method method, Object[] args, Class<?>[] argTypes, Object result) {
        log.info("after SpringMVC method: {}, args: {}, result: {}", method.getName(), Arrays.toString(args), result);
        return result;
    }

    @Override
    public void handleException(EnhancedInstance instance, Method method, Object[] args, Class<?>[] argTypes, Object result, Throwable throwable) {
        log.error("exception SpringMVC error", throwable);
    }
}
