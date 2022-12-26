package org.example.mall.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.context.scope.refresh.RefreshScopeRefreshedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
@EnableScheduling
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

    @RestController
    @RefreshScope // 动态刷新
    public static class ScheduledController implements ApplicationListener<RefreshScopeRefreshedEvent> {
        @Value("${commons.name}")
        private String name;

        @GetMapping("/get")
        public String get() {
            return String.format("common.name=%s", name);
        }

        @Scheduled(cron = "*/3 * * * * ?")
        public void execute() {
            System.out.println("定时任务开始执行..." + name);
        }

        // 由于该 bean 是 @RefreshScope，所以当值发生变化时，会清空 bean 缓存，当再次调用当时候，会重新创建
        // 用这个方法可以解决 @Scheduled 方法不执行的问题
        @Override
        public void onApplicationEvent(RefreshScopeRefreshedEvent refreshScopeRefreshedEvent) {
            execute();
        }
    }

}
