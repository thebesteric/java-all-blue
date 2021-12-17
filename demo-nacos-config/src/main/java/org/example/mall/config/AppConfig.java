package org.example.mall.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class AppConfig {

    public static void main(String[] args) throws InterruptedException {
        ConfigurableApplicationContext context = SpringApplication.run(AppConfig.class, args);

        while (true) {
            Map<String, String> xxx = context.getEnvironment().getProperty("xxx", Map.class);
            String name = context.getEnvironment().getProperty("commons.name");
            String age = context.getEnvironment().getProperty("commons.age");
            System.out.println("name=" + name + " age=" + age);
            TimeUnit.SECONDS.sleep(5);
        }
    }

    @RestController
    @RefreshScope // 动态刷新
    public static class TestController {
        @Value("${commons.name}")
        private String name;

        @Value("${commons.age}")
        private String age;

        @GetMapping("/commons")
        public String commons() {
            return name + ":" + age;
        }
    }

}
