package com.example.agent.core.plugin.enhance;

import com.example.agent.core.plugin.load.InterceptorInstanceLoader;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * 实例方法蓝吉骐
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-09-21 00:34:22
 */
@Slf4j
public class InstanceMethodsInterceptor {

    private InstanceMethodsAroundInterceptor instanceMethodsAroundInterceptor;

    /**
     * 构造方法
     *
     * @param instanceInterceptor InstanceMethodsAroundInterceptor 的实现类
     * @param classLoader         类加载器
     * @author wangweijun
     * @since 2023/9/21 00:34:24
     */
    public InstanceMethodsInterceptor(String instanceInterceptor, ClassLoader classLoader) {
        try {
            instanceMethodsAroundInterceptor = InterceptorInstanceLoader.load(instanceInterceptor, classLoader);
        } catch (Exception e) {
            log.error("Cannot load interceptor: {}", instanceInterceptor, e);
        }
    }

    @RuntimeType
    public Object intercept(@This Object target,
                            @Origin Method targetMethod,
                            @AllArguments Object[] targetMethodArgs,
                            @SuperCall Callable<?> call) throws Throwable {
        // 强转
        EnhancedInstance enhancedInstance = (EnhancedInstance) target;

        // 前置通知
        try {
            instanceMethodsAroundInterceptor.beforeMethod(enhancedInstance, targetMethod, targetMethodArgs, targetMethod.getParameterTypes());
        } catch (Exception ex) {
            log.error("class {} before instance method {} interceptor failure", target.getClass(), targetMethod.getName(), ex);
        }

        Object result = null;
        try {
            result = call.call();
        } catch (Throwable t) {
            // 异常通知
            try {
                instanceMethodsAroundInterceptor.handleException(enhancedInstance, targetMethod, targetMethodArgs, targetMethod.getParameterTypes(), result, t);
            } catch (Exception ex) {
                log.error("class {} execute instance method {} interceptor failure", target.getClass(), targetMethod.getName(), ex);
            }
            throw t;
        } finally {
            // 最终通知
            try {
                result = instanceMethodsAroundInterceptor.afterMethod(enhancedInstance, targetMethod, targetMethodArgs, targetMethod.getParameterTypes(), result);
            } catch (Exception ex) {
                log.error("class {} after instance method {} interceptor failure", target.getClass(), targetMethod.getName(), ex);
            }
        }

        return result;
    }
}
