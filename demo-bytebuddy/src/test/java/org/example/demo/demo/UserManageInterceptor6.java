package org.example.demo.demo;

import net.bytebuddy.implementation.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.Callable;

/**
 * UserManageInterceptor1
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-08-30 23:31:06
 */
public class UserManageInterceptor6 {

    /**
     * 被 @RuntimeType 注解标注的方法就是拦截方法，此时方法签名或返回值可以与被拦截方法不一致
     *
     * @param clazz            @Origin Class<?> 表示类对象
     * @param targetMethod     @Origin Method 表示被拦截的目标方法，实例方法、静态方法均可以用
     * @param targetMethodArgs @AllArguments Object[] 表示被拦截的目标方法的参数，实例方法、构造方法、静态方法均可以用
     * @param call             @SuperCall Callable<?> 用于调用目标方法
     * @return String
     */
    @RuntimeType
    public Object intercept(@Origin Class<?> clazz,
                            @Origin Method targetMethod,
                            @AllArguments Object[] targetMethodArgs,
                            @SuperCall Callable<?> call) {
        System.out.println("clazz = " + clazz);
        System.out.println("targetMethod = " + targetMethod);
        System.out.println("targetMethodArgs = " + Arrays.toString(targetMethodArgs));
        Object result = null;
        try {
            result = call.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
