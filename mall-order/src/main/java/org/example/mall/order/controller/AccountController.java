package org.example.mall.order.controller;

import org.example.mall.comm.R;
import org.example.mall.order.feign.AccountFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountFeignService accountFeignService;

    @GetMapping("/get/{id}")
    public R getOrder(@PathVariable String id) {
        return accountFeignService.get(id);
    }

}
