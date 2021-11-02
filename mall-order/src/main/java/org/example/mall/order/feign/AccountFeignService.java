package org.example.mall.order.feign;

import org.example.mall.comm.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "mall-account", path = "/account")
public interface AccountFeignService {

    @GetMapping("/{id}")
    R get(@PathVariable String id);

}
