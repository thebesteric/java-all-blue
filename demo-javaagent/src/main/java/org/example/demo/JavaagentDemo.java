package org.example.demo;

import lombok.extern.slf4j.Slf4j;

import java.lang.instrument.Instrumentation;

/**
 * Hello world!
 */
@Slf4j
public class JavaagentDemo {

    /**
     * 在 main 方法执行之前会被自动调用，也是插桩的入口
     * 使用：java -javaagent:/xxx/demo-javaagent-0.0.1-SNAPSHOT-jar-with-dependencies.jar=k1:v1,k2:v2 -jar xxx.jar
     *
     * @param args            表示传给 javaagent 的参数
     * @param instrumentation 插桩
     */
    public static void premain(String args, Instrumentation instrumentation) {
        log.info("进入到 premain， args = {}", args);
        instrumentation.addTransformer(new MyClassFileTransformer());
    }
}
