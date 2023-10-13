package com.example.springmvc;

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.Callable;

/**
 * MyInterceptor
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-09-15 13:51:04
 */
@Slf4j
public class MySpringMVCInterceptor {

    @RuntimeType
    public Object intercept(@This Object targetObj,
                            @Origin Method targetMethod,
                            @AllArguments Object[] targetMethodArgs,
                            @Super Object superObj,
                            @SuperCall Callable<?> call) {
        String methodName = targetMethod.getName();
        log.info("before controller exec methodName = {}, args = {}", methodName, Arrays.toString(targetMethodArgs));
        long startTime = System.currentTimeMillis();
        Object result = null;
        try {
            result = call.call();
            log.info("after controller exec result = {}", result);
        } catch (Exception ex) {
            log.error("controller: {} exec error", targetMethod, ex);
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("final controller: {} exec in {} ms", targetMethod, endTime - startTime);
        }
        return result;

    }

}
