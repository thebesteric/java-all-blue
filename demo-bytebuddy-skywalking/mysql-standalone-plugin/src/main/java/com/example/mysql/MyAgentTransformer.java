package com.example.mysql;

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;
import net.bytebuddy.utility.nullability.MaybeNull;

import java.security.ProtectionDomain;

/**
 * MyAgentTransform
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-09-15 11:16:15
 */
@Slf4j
public class MyAgentTransformer implements AgentBuilder.Transformer {

    /**
     * 当要被拦截的类（也就是 type() 方法设置的类），第一次加载的时候会进入此方法
     */
    @Override
    public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader, JavaModule javaModule, @MaybeNull ProtectionDomain protectionDomain) {
        System.out.println("========== transform ==========");
        return transform(builder, typeDescription, classLoader, javaModule);
    }

    public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader, JavaModule javaModule) {
        String actualClassName = typeDescription.getActualName();
        log.info("========== actualClassName to transform: {} ==========", actualClassName);

        DynamicType.Builder.MethodDefinition.ReceiverTypeDefinition<?> intercept = builder.method(
                ElementMatchers.named("execute")
                        .or(ElementMatchers.named("executeUpdate"))
                        .or(ElementMatchers.named("executeQuery"))
        ).intercept(MethodDelegation.to(new MyMySQLInterceptor()));


        // "((PreparedQuery)this.query).getOriginalSql()"

        // 不能直接返回 builder，因为 bytebuddy 库中的类基本都是不可变的，修改之后需要返回修改之后的对象
        return intercept;
    }
}
