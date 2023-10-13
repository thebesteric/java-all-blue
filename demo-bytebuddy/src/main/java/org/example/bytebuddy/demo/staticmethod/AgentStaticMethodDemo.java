package org.example.bytebuddy.demo.staticmethod;

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.NamedElement;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import org.example.bytebuddy.demo.ByteBuddyListener;

import java.lang.instrument.Instrumentation;

/**
 * AgentDemo
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-09-15 11:06:05
 */
@Slf4j
public class AgentStaticMethodDemo {

    public static final String CLASS_NAME = "org.example.springboot.util.StringUtils";

    /**
     * 在 main 方法执行之前会被自动调用，也是插桩的入口
     * 使用：java -javaagent:/xxx/demo-javaagent-0.0.1-SNAPSHOT-jar-with-dependencies.jar=k1:v1,k2:v2 -jar xxx.jar
     *
     * @param args            表示传给 javaagent 的参数
     * @param instrumentation 插桩
     */
    public static void premain(String args, Instrumentation instrumentation) {
        log.info("进入到 premain， args = {}", args);

        AgentBuilder builder = new AgentBuilder.Default()
                // 忽略要拦截的包
                .ignore(ElementMatchers.nameStartsWith("net.bytebuddy").or(ElementMatchers.nameStartsWith("org.springframework")))
                // 配置哪些类需要拦截
                .type(
                        // 拦截某一个具体的类
                        getTypeMatcher()
                )
                .transform(new MyAgentStaticMethodTransformer())
                .with(new ByteBuddyListener());

        builder.installOn(instrumentation);
    }


    public static ElementMatcher<? super TypeDescription> getTypeMatcher() {
        // 方式一：判断名称相等的
        // return ElementMatchers.named(CLASS_NAME);

        // 方式二：判断名称相等的
        return new ElementMatcher.Junction.AbstractBase<NamedElement>() {
            @Override
            public boolean matches(NamedElement target) {
                return CLASS_NAME.equals(target.getActualName());
            }
        };
    }
}
