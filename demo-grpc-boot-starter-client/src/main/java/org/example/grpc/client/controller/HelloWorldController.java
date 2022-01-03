package org.example.grpc.client.controller;

import org.example.grpc.client.service.HelloWorldClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

    @Autowired
    private HelloWorldClientService helloWorldClientService;

    @GetMapping("/hello")
    public Object hello(String name) {
        return helloWorldClientService.sendMessage(name);
    }

}
