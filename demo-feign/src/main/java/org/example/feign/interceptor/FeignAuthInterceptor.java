package org.example.feign.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public class FeignAuthInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        String accessToken = UUID.randomUUID().toString();
        requestTemplate.header("Authorization", accessToken);

        String method = requestTemplate.method();
        log.info("method =" + method);

        String apiUrl = requestTemplate.url();
        log.info("apiUrl =" + apiUrl);

        requestTemplate.headers().forEach((key, value) -> {
            log.info("key = {}, value = {}", key, value);
        });

        if ("POST".equalsIgnoreCase(method) && requestTemplate.body() != null) {
            JSONObject requestBody = (JSONObject) JSON.parse(new String(requestTemplate.body(), requestTemplate.requestCharset()));
            log.info("body = {}", requestBody.toJSONString());
        }


    }
}
