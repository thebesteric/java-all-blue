package org.example.mall.account;

import org.example.mall.comm.BaseController;
import org.example.mall.comm.R;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@SpringBootApplication
@EnableDiscoveryClient
public class AccountApp extends BaseController {
    public static void main(String[] args) {
        SpringApplication.run(AccountApp.class, args);
    }

    @RestController
    @RequestMapping("/account")
    public static class AccountController extends BaseController {

        @GetMapping("/{id}")
        public R get(@PathVariable String id) {
            System.out.println("id=" + id);
            return R.success().setData(id).setMessage(getHost());
        }

        @PostMapping("/save")
        public R save(@RequestBody Map<String, Object> params) {
            return R.success().setData(params).setMessage(getHost());
        }
    }
}
