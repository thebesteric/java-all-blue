package com.example.agent.core.plugin.enhance;

import com.example.agent.core.plugin.load.InterceptorInstanceLoader;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * 静态方法拦截器
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-09-20 00:53:26
 */
@Slf4j
public class StaticMethodsInterceptor {

    private StaticMethodsAroundInterceptor staticMethodsAroundInterceptor;

    /**
     * 构造方法
     *
     * @param methodInterceptor StaticMethodsAroundInterceptor 的实现类
     * @param classLoader 类加载器
     * @author wangweijun
     * @since 2023/9/21 00:24
     */
    public StaticMethodsInterceptor(String methodInterceptor, ClassLoader classLoader) {
        try {
            staticMethodsAroundInterceptor = InterceptorInstanceLoader.load(methodInterceptor, classLoader);
        } catch (Exception e) {
            log.error("Cannot load interceptor: {}", methodInterceptor, e);
        }
    }

    @RuntimeType
    public Object intercept(@Origin Class<?> clazz,
                            @Origin Method targetMethod,
                            @AllArguments Object[] targetMethodArgs,
                            @SuperCall Callable<?> call) throws Throwable {
        // 前置通知
        try {
            staticMethodsAroundInterceptor.beforeMethod(clazz, targetMethod, targetMethodArgs, targetMethod.getParameterTypes());
        } catch (Exception ex) {
            log.error("class {} before static method {} interceptor failure", clazz, targetMethod.getName(), ex);
        }

        Object result = null;
        try {
            result = call.call();
        } catch (Throwable t) {
            // 异常通知
            try {
                staticMethodsAroundInterceptor.handleException(clazz, targetMethod, targetMethodArgs, targetMethod.getParameterTypes(), result, t);
            } catch (Exception ex) {
                log.error("class {} execute static method {} interceptor failure", clazz, targetMethod.getName(), ex);
            }
            throw t;
        } finally {
            // 最终通知
            try {
                result = staticMethodsAroundInterceptor.afterMethod(clazz, targetMethod, targetMethodArgs, targetMethod.getParameterTypes(), result);
            } catch (Exception ex) {
                log.error("class {} after static method {} interceptor failure", clazz, targetMethod.getName(), ex);
            }
        }

        return result;
    }
}
