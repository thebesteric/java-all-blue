package org.example.mall.order.feign;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.example.mall.comm.R;
import org.springframework.web.bind.annotation.RequestBody;

public interface FeignServiceDemo {
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @RequestLine("GET /user/get/{id}")
    R getUser(@Param("id") String id);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @RequestLine("POST /user/save")
    R saveUser(@RequestBody UserVo id);
}
