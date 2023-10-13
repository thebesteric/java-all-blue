package org.example.bytebuddy.demo.staticmethod;

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;

/**
 * MyAgentTransform
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-09-15 11:16:15
 */
@Slf4j
public class MyAgentStaticMethodTransformer implements AgentBuilder.Transformer {

    static final String MAPPING_PKG_PREFIX = "org.springframework.web.bind.annotation";
    static final String MAPPING_SUFFIX = "Mapping";

    /**
     * 当要被拦截的类（也就是 type() 方法设置的类），第一次加载的时候会进入此方法
     *
     * @param builder
     * @param typeDescription 表示要被加载的类的信息
     * @param classLoader
     * @param javaModule
     * @return Builder<?>
     */
    @Override
    public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader, JavaModule javaModule) {
        String actualClassName = typeDescription.getActualName();
        log.info("========== actualClassName to transform: {} ==========", actualClassName);

        return builder.method(
                // 拦截静态方法
               ElementMatchers.<MethodDescription>isStatic()
        ).intercept(MethodDelegation.to(new MyStaticMethodInterceptor()));
    }

}
