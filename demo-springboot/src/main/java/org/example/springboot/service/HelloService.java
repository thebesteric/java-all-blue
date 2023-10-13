package org.example.springboot.service;

import io.github.thebesteric.framework.apm.agent.extension.annotation.ApmTracing;
import org.example.springboot.util.StringUtils;
import org.springframework.stereotype.Service;

/**
 * HelloService
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-09-14 12:06:46
 */
@Service
@ApmTracing
public class HelloService {

    public HelloService() {
        System.out.println("helloService construct running...");
    }

    @ApmTracing
    public String hello(String name) {
        // int i = 1 / 0;
        return StringUtils.contact("hello", name, "-");
    }
}
