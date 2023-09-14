package org.example.springboot.controller;

import org.example.springboot.service.HelloService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * HelloController
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-09-14 12:05:15
 */
@RestController
public class HelloController {

    @Resource
    private HelloService helloService;

    @GetMapping("/hello")
    public String hello(String name) {
        return helloService.hello(name);
    }

}
