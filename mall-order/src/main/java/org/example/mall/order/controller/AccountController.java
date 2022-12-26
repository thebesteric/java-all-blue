package org.example.mall.order.controller;

import org.example.mall.comm.R;
import org.example.mall.order.feign.AccountFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountFeignService accountFeignService;

    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/get/{id}")
    public R getOrder(@PathVariable String id, @RequestParam String name, @RequestParam int age) {
        return accountFeignService.get(id, name, age);
    }

    @GetMapping("/v1/get/{id}")
    public R getOrderV1(@PathVariable String id, @RequestParam String name, @RequestParam int age) {
        String url = "http://mall-account/" + id;
        return restTemplate.getForObject(url, R.class);
    }
}
