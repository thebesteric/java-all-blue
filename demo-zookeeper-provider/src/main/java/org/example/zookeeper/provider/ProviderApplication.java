package org.example.zookeeper.provider;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class ProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProviderApplication.class, args);
    }

    @Value("${server.port}")
    private String port;

    @Value("${spring.application.name}")
    private String name;

    @GetMapping("/getInfo")
    public String getServerPortAndName() {
        return this.name + " : " + this.port;
    }

}
