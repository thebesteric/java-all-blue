package org.example.mall.account;

import org.example.mall.comm.BaseController;
import org.example.mall.comm.R;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableDiscoveryClient
public class AccountAppStandby {
    public static void main(String[] args) {
        SpringApplication.run(AccountAppStandby.class, args);
    }

    @RestController
    @RequestMapping("/account")
    public static class AccountController extends BaseController {

        @GetMapping("/{id}")
        public R get(@PathVariable String id) {
            return R.success().setMessage(getHost()).setData(100.00);
        }
    }
}
