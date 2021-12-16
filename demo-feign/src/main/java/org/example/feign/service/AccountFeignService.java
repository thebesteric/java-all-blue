package org.example.feign.service;

import org.example.mall.comm.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(value = "mall-account", path = "/account")
public interface AccountFeignService {

    @GetMapping("/{id}")
    R get(@PathVariable String id);

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    R save(@RequestBody Map<String, Object> params);

}
