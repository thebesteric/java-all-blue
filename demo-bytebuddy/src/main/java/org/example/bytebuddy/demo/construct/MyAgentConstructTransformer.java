package org.example.bytebuddy.demo.construct;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.SuperMethodCall;
import net.bytebuddy.utility.JavaModule;

import static net.bytebuddy.matcher.ElementMatchers.any;

/**
 * MyAgentConstructTransformer
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-09-18 11:47:55
 */
public class MyAgentConstructTransformer implements AgentBuilder.Transformer {
    @Override
    public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader, JavaModule javaModule) {
        return builder
                .constructor(any())
                .intercept(
                        SuperMethodCall.INSTANCE.andThen(MethodDelegation.to(new MyConstructInterceptor()))
                );
    }
}
