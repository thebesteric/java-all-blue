package org.example.dubbo.simulate.provider.impl;

import org.example.dubbo.simulate.provider.api.HelloService;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HelloServiceImpl implements HelloService {

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public String sayHello(String name) {
        return dateFormat.format(new Date()) + ": hello " + name;
    }
}
