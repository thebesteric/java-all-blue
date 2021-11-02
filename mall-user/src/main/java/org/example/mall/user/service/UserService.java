package org.example.mall.user.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.apache.skywalking.apm.toolkit.trace.Tag;
import org.apache.skywalking.apm.toolkit.trace.Tags;
import org.apache.skywalking.apm.toolkit.trace.Trace;
import org.springframework.stereotype.Component;

@Component
public class UserService {

    @Trace
    @Tag(key = "get", value = "returnedObj")
    public String get(String id) {
        return "user-" + id;
    }

    @SentinelResource(value = "getById", blockHandler = "handleException")
    @Trace
    @Tags({@Tag(key = "param", value = "arg[0]"), @Tag(key = "user", value = "returnedObj")})
    public String getById(String id) {
        return "user-" + id;
    }

    public String handleException(String id, BlockException exception) {
        return "==被限流拉==" + id;
    }

}
