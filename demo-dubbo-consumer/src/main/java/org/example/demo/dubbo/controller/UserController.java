package org.example.demo.dubbo.controller;

import org.apache.dubbo.config.annotation.DubboReference;
import org.example.mall.comm.R;
import org.example.mall.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @DubboReference
    private UserService userService;

    @GetMapping("/list")
    public R list() {
        return R.success().setData(userService.list());
    }

}
