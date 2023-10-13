package com.example.mysql;

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
public class MyMySQLInterceptor {

    @RuntimeType
    public Object intercept(@This Object targetObj,
                            @Origin Method targetMethod,
                            @AllArguments Object[] targetMethodArgs,
                            @Super Object superObj,
                            @SuperCall Callable<?> call) {

        Class<?> declaringClass = targetMethod.getDeclaringClass();
        System.out.println("declaringClass = " + declaringClass);

        try {
            Method toString = declaringClass.getDeclaredMethod("toString");
            toString.setAccessible(true);
            Object queryResult = toString.invoke(targetObj);
            log.info("queryResult: {}", queryResult);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        String methodName = targetMethod.getName();
        log.info("before mysql exec methodName = {}, args = {}", methodName, Arrays.toString(targetMethodArgs));
        long startTime = System.currentTimeMillis();
        Object result = null;
        try {
            result = call.call();
            log.info("after mysql exec result = {}", result);
        } catch (Exception ex) {
            log.error("mysql: {} exec error", targetMethod, ex);
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("final mysql: {} exec in {} ms", targetMethod, endTime - startTime);
        }
        return result;

    }

}
