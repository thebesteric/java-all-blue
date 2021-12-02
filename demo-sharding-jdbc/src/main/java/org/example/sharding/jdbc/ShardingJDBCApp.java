package org.example.sharding.jdbc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("org.example.sharding.jdbc.mapper")
@SpringBootApplication
public class ShardingJDBCApp {
    public static void main(String[] args) {
        SpringApplication.run(ShardingJDBCApp.class, args);
    }
}
