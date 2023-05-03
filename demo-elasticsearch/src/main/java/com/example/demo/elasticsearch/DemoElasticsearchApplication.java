package com.example.demo.elasticsearch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class DemoElasticsearchApplication {

    public static void main(String[] args) {
        log.error("Elasticsearch test error log");
        SpringApplication.run(DemoElasticsearchApplication.class, args);
    }

}
