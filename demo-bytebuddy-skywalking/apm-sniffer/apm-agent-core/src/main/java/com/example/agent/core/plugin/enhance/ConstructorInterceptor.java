package com.example.agent.core.plugin.enhance;

import com.example.agent.core.plugin.load.InterceptorInstanceLoader;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;

/**
 * 构造方法拦截器
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-09-20 23:31:43
 */
@Slf4j
public class ConstructorInterceptor {

    private ConstructorAfterInterceptor constructorAfterInterceptor;

    /**
     * 构造方法
     *
     * @param constructorInterceptor ConstructorOnInterceptor 的实现类
     * @param classLoader            类加载器
     * @author wangweijun
     * @since 2023/9/21 00:24
     */
    public ConstructorInterceptor(String constructorInterceptor, ClassLoader classLoader) {
        try {
            constructorAfterInterceptor = InterceptorInstanceLoader.load(constructorInterceptor, classLoader);
        } catch (Exception e) {
            log.error("Cannot load interceptor: {}", constructorInterceptor, e);
        }
    }

    @RuntimeType
    public void intercept(@This Object obj, @AllArguments Object[] targetMethodArgs) throws Throwable {
        try {
            // 强转
            EnhancedInstance enhancedInstance = (EnhancedInstance) obj;
            constructorAfterInterceptor.afterConstructor(enhancedInstance, targetMethodArgs);
        } catch (Throwable t) {
            log.error("Constructor failed", t);
        }
    }
}
