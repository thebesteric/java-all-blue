package org.example.springboot.service;

import org.springframework.stereotype.Service;

/**
 * HelloService
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-09-14 12:06:46
 */
@Service
public class HelloService {

    public String hello(String name) {
        return "hello " + name;
    }
}
