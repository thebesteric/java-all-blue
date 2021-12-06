package org.example.demo.dubbo.provider;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@EnableDubbo(scanBasePackages = "org.example.demo.dubbo.provider.service")
public class AppDubboProvider {
    public static void main(String[] args) {
        SpringApplication.run(AppDubboProvider.class, args);
    }
}
