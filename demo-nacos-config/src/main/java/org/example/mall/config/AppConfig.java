package org.example.mall.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class AppConfig {

    public static void main(String[] args) throws InterruptedException {
        ConfigurableApplicationContext context = SpringApplication.run(AppConfig.class, args);

        Props props = context.getBean(Props.class);

        while (true) {
            // context.getEnvironment().getProperty 无法从 nacos 中读取普通对象
            String name = context.getEnvironment().getProperty("commons.name");
            String age = context.getEnvironment().getProperty("commons.age");
            System.out.println("name=" + name + " age=" + age);

            // 利用 ConfigurationProperties 读取
            props.getRouteType().forEach((k, v) -> System.out.println(k + " = " + v));

            // 读取
            String sharedName = context.getEnvironment().getProperty("shared.name");
            System.out.println("sharedName = " + sharedName);

            TimeUnit.SECONDS.sleep(5);
        }
    }

    @Getter
    @Setter
    @Component
    @ConfigurationProperties(prefix = "configuration")
    @RefreshScope // 这里需要加动态刷新，否则旧值会出问题
    public static class Props {
        private Map<Integer, String> routeType = new HashMap<>();
    }

    @RestController
    @RefreshScope // 动态刷新
    public static class TestController {
        @Value("${commons.name}")
        private String name;

        @Value("${commons.age}")
        private int age;

        @Value("${shared.name}")
        private String sharedName;

        @GetMapping("/config")
        public String commons() {
            return String.format("common.name=%s, common.age=%d, shared.name=%s", name, age, sharedName);
        }
    }

}
