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
     *
     * @param args            表示传给 javaagent 的参数
     * @param instrumentation 插桩
     */
    public static void premain(String args, Instrumentation instrumentation) {
        log.info("进入到 premain， args = {}", args);
        instrumentation.addTransformer(new MyClassFileTransformer());
    }
}
