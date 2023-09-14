package org.example.demo.demo;

import net.bytebuddy.implementation.bind.annotation.*;

import java.lang.reflect.Method;

/**
 * UserManageInterceptor1
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-08-30 23:31:06
 */
public class UserManageInterceptor4 {

    /**
     * 被 @RuntimeType 注解标注的方法就是拦截方法，此时方法签名或返回值可以与被拦截方法不一致
     *
     * @param targetObj        @This Object 表示被拦截的目标对象，只有拦截实例方法、构造方法时可以用
     * @param targetMethod     @Origin Method 表示被拦截的目标方法，实例方法、静态方法均可以用
     * @param targetMethodArgs @AllArguments Object[] 表示被拦截的目标方法的参数，实例方法、构造方法、静态方法均可以用
     * @param superObj         @Super Object 表示被拦截的目标对象的父类，只有拦截实例方法或构造方法时可以用，若确定父类也可以用父类接收
     * @param morphCallable    @Morph MorphCallable 自定义的接口，可以传递参数，用于调用目标方法
     * @return String
     */
    @RuntimeType
    public Object intercept(@This Object targetObj, @Origin Method targetMethod,
                             @AllArguments Object[] targetMethodArgs, @Super Object superObj,
                             @Morph MorphCallable morphCallable) {
        Object result = null;
        try {
            if (morphCallable != null && targetMethodArgs.length > 0) {
                targetMethodArgs[0] = Long.parseLong(targetMethodArgs[0].toString()) + 100;
            }
            assert morphCallable != null;
            result = morphCallable.call(targetMethodArgs);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
