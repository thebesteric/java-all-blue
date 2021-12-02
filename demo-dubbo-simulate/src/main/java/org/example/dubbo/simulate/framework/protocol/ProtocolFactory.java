package org.example.dubbo.simulate.framework.protocol;

import org.example.dubbo.simulate.framework.protocol.dubbo.DubboProtocol;
import org.example.dubbo.simulate.framework.protocol.http.HttpProtocol;

public class ProtocolFactory {

    public static Protocol getProtocol(String protocol) {
        if (protocol == null || protocol.equals("")) {
            protocol = "http";
        }
        switch (protocol) {
            case "http":
                return new HttpProtocol();
            case "dubbo":
                return new DubboProtocol();
            default:
                throw new UnsupportedOperationException("不支持的协议");
        }
    }
}
