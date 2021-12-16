package org.example.feign.demo;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.example.mall.comm.R;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

public interface FeignServiceDemo {
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @RequestLine("GET /account/{id}")
    R getAccount(@Param("id") String id);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @RequestLine("POST /account/save")
    R saveAccount(@RequestBody Map<String, Object> body);
}
