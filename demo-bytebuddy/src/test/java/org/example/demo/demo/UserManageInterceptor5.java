package org.example.demo.demo;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;

import java.lang.reflect.Constructor;
import java.util.Arrays;

/**
 * 构造方法拦截器
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-08-30 23:31:06
 */
public class UserManageInterceptor5 {

    /**
     * 被 @RuntimeType 注解标注的方法就是拦截方法，此时方法签名或返回值可以与被拦截方法不一致
     *
     * @param targetObj   @This Object 表示被拦截的目标对象，只有拦截实例方法、构造方法时可以用
     * @param constructor @Origin Constructor<?> 表示被拦截的构造方法
     * @param args        @AllArguments Object[] 表示被拦截的目标方法的参数，实例方法、构造方法、静态方法均可以用
     */
    @RuntimeType
    public void interceptor(@This Object targetObj, @Origin Constructor<?> constructor, @AllArguments Object[] args) throws Exception {
        System.out.println("targetObj 被实例化了 = " + targetObj);
        System.out.println("constructor = " + constructor);
        System.out.println("args = " + Arrays.toString(args));
        System.out.println("Constructor intercepted with arguments: " + Arrays.toString(args));

    }
}
