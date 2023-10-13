package org.example.springboot.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "baiduFeign", url = "https://www.baidu.com")
public interface BaiduFeign {

    @GetMapping
    String baidu();

}
