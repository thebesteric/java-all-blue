package org.example.mall.comm;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;

import java.net.InetAddress;

public class BaseController {
    @Value("${server.port}")
    private int port;

    @SneakyThrows
    public String getHost() {
        return InetAddress.getLocalHost().getHostAddress() + ":" + port;
    }
}
