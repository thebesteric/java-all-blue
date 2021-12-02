package org.example.dubbo.simulate.framework.protocol.dubbo;

import org.example.dubbo.simulate.framework.Invocation;
import org.example.dubbo.simulate.framework.URL;
import org.example.dubbo.simulate.framework.protocol.Protocol;

public class DubboProtocol implements Protocol {

    @Override
    public void startup(URL url) {
        NettyServer nettyServer = new NettyServer();
        nettyServer.start(url.getHostname(), url.getPort());

    }

    @Override
    public String send(URL url, Invocation invocation) {
        NettyClient nettyClient = new NettyClient();
        return nettyClient.send(url.getHostname(),url.getPort(), invocation);
    }
}
