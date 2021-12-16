package org.example.mall.order.controller;

import org.example.mall.comm.R;
import org.example.mall.order.feign.AccountFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountFeignService accountFeignService;

    @GetMapping("/get/{id}")
    public R getOrder(@PathVariable String id, @RequestParam String name, @RequestParam int age) {
        return accountFeignService.get(id, name, age);
    }

}
