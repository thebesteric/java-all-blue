package org.example.demo.dubbo.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.example.mall.service.UserService;

import java.util.Collections;
import java.util.List;

@DubboService
@Slf4j
public class UserServiceImpl implements UserService {

    @Override
    public List<String> list() {
        log.info("调用 UserService => list");
        return Collections.singletonList("hello dubbo");
    }
}
