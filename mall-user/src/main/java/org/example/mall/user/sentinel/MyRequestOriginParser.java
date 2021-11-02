package org.example.mall.user.sentinel;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.RequestOriginParser;
import lombok.SneakyThrows;

import javax.servlet.http.HttpServletRequest;

public class MyRequestOriginParser implements RequestOriginParser {

    /**
     *
     */
    @SneakyThrows
    @Override
    public String parseOrigin(HttpServletRequest request) {
        // 解析来源
        final String origin = request.getParameter("serviceName");
        // if (StringUtils.isEmpty(origin)) {
        //     throw new IllegalAccessException("serviceName 未指定");
        // }
        return origin;
    }
}
