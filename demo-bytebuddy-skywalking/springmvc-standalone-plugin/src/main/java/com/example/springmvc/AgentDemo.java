package com.example.springmvc;

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.instrument.Instrumentation;

/**
 * AgentDemo
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-09-15 11:06:05
 */
@Slf4j
public class AgentDemo {

    static final String CONTROLLER_NAME = "org.springframework.stereotype.Controller";
    static final String REST_CONTROLLER_NAME = "org.springframework.web.bind.annotation.RestController";

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
                .ignore(ElementMatchers.nameStartsWith("net.bytebuddy").or(ElementMatchers.nameStartsWith("org.springframework")))
                .type(
                        ElementMatchers.isAnnotatedWith(
                                ElementMatchers.named(CONTROLLER_NAME).or(ElementMatchers.named(REST_CONTROLLER_NAME)))
                )
                .transform(new MyAgentTransformer())
                .with(new ByteBuddyListener());

        builder.installOn(instrumentation);
    }
}
