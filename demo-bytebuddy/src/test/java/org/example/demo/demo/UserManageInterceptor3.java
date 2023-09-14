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
public class UserManageInterceptor3 {

    /**
     * 被 @RuntimeType 注解标注的方法就是拦截方法，此时方法签名或返回值可以与被拦截方法不一致
     *
     * @param id
     * @return String
     */
    // @RuntimeType
    // public String intercept1(Long id) {
    //     return "UserManageInterceptor3-intercept1 用户 ID：" + id + " 的名字是：" + UUID.randomUUID();
    // }

    /**
     * 被 @RuntimeType 注解标注的方法就是拦截方法，此时方法签名或返回值可以与被拦截方法不一致
     *
     * @param targetObj        @This Object 表示被拦截的目标对象，只有拦截实例方法、构造方法时可以用
     * @param targetMethod     @Origin Method 表示被拦截的目标方法，实例方法、静态方法均可以用
     * @param targetMethodArgs @AllArguments Object[] 表示被拦截的目标方法的参数，实例方法、构造方法、静态方法均可以用
     * @param superObj         @Super Object 表示被拦截的目标对象的父类，只有拦截实例方法或构造方法时可以用，若确定父类也可以用父类接收
     * @param call             @SuperCall Callable<?> 用于调用目标方法，指定是原始的方法，配合 rebase 或 subclass 的方式增强实例方式时使用
     * @return String
     */
    @RuntimeType
    public Object intercept2(@This Object targetObj,
                             @Origin Method targetMethod,
                             @AllArguments Object[] targetMethodArgs,
                             @Super Object superObj,
                             @SuperCall Callable<?> call) {
        // targetObj = a.b.SubObject@4ba302e0
        System.out.println("targetObj = " + targetObj);
        // targetMethod = public java.lang.String org.example.demo.demo.UserManager.getUsername(java.lang.Long)
        System.out.println("targetMethod = " + targetMethod);
        // targetMethodArgs = [1]
        System.out.println("targetMethodArgs = " + Arrays.toString(targetMethodArgs));
        // superObj = a.b.SubObject@4ba302e0
        System.out.println("superObj = " + superObj);
        Object result = null;
        try {
            // 不能使用 targetMethod.invoke，会产生递归调用，最终 OOM
            // result = targetMethod.invoke(targetObj, targetMethodArgs);

            // 调用目标方法
            // userManager.getUsername() = 用户 ID：1 的名字是：b16523d6-45d9-4e3d-8560-c8358b5c971d
            result = call.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
