package org.example.bytebuddy.demo.staticmethod;

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.Callable;

/**
 * MyStaticMethodInterceptor
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-09-15 18:19:13
 */
@Slf4j
public class MyStaticMethodInterceptor {

    @RuntimeType
    public Object intercept(@Origin Class<?> targetClass,
                            @Origin Method targetMethod,
                            @AllArguments Object[] targetMethodArgs,
                            @SuperCall Callable<?> call) {
        String methodName = targetMethod.getName();
        log.info("before StringUtils exec methodName = {}, args = {}", methodName, Arrays.toString(targetMethodArgs));
        long startTime = System.currentTimeMillis();
        Object result = null;
        try {
            result = call.call();
            log.info("after StringUtils exec result = {}", result);
        } catch (Exception ex) {
            log.error("error StringUtils: {} exec error", targetMethod, ex);
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("final StringUtils: {} exec in {} ms", targetMethod, endTime - startTime);
        }
        return result;

    }

}
