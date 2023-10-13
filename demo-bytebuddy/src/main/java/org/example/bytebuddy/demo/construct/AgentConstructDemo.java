package org.example.bytebuddy.demo.construct;

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.matcher.ElementMatchers;
import org.example.bytebuddy.demo.ByteBuddyListener;

import java.lang.instrument.Instrumentation;

/**
 * AgentConstructDemo
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-09-18 11:42:36
 */
@Slf4j
public class AgentConstructDemo {

    public static final String CLASS_NAME = "org.example.springboot.service.HelloService";


    public static void premain(String args, Instrumentation instrumentation) {
        log.info("进入到 premain， args = {}", args);

        AgentBuilder builder = new AgentBuilder.Default()
                // 忽略要拦截的包
                .ignore(ElementMatchers.nameStartsWith("net.bytebuddy").or(ElementMatchers.nameStartsWith("org.springframework")))
                // 配置哪些类需要拦截
                .type(
                    ElementMatchers.named(CLASS_NAME)
                )
                .transform(new MyAgentConstructTransformer())
                .with(new ByteBuddyListener());

        builder.installOn(instrumentation);
    }

}
