package org.example.mall.order.feign;

import io.github.thebesteric.framework.switchlogger.annotation.SwitchLogger;
import org.example.mall.comm.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

// @FeignClient(value = "mall-user", path = "/user", configuration = OrderApp.FeignConfiguration.class)
@FeignClient(value = "mall-user", path = "/user")
@SwitchLogger(tag = "feign")
public interface UserFeignService {

    @GetMapping("/get/{id}")
    R getUser(@PathVariable String id);

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    R getUser(@RequestBody UserVo userVo);
}
