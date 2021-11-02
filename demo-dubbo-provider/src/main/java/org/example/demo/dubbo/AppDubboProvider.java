package org.example.demo.dubbo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class AppDubboProvider {
    public static void main(String[] args) {
        SpringApplication.run(AppDubboProvider.class, args);
    }
}
