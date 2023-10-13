package org.example.bytebuddy.demo.construct;

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;

import java.lang.reflect.Constructor;
import java.util.Arrays;

/**
 * MyConstructInterceptor
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-09-18 11:49:55
 */
@Slf4j
public class MyConstructInterceptor {

    @RuntimeType
    public void intercept(@This Object targetObj, @Origin Constructor<?> constructor, @AllArguments Object[] args) {
        log.info("targetObj 被实例化了 = {}, constructor = {}, args = {}", targetObj, constructor,Arrays.toString(args));
    }

}
