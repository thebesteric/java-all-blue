package org.example.dubbo.simulate.framework.protocol.http;

import org.example.dubbo.simulate.framework.Invocation;
import org.example.dubbo.simulate.framework.URL;
import org.example.dubbo.simulate.framework.protocol.Protocol;

public class HttpProtocol implements Protocol {
    @Override
    public void startup(URL url) {
        HttpServer httpServer = new HttpServer();
        httpServer.start(url.getHostname(), url.getPort());
    }

    @Override
    public String send(URL url, Invocation invocation) {
        HttpClient httpClient = new HttpClient();
        return httpClient.send(url.getProtocol() + "://" + url.getHostname(), url.getPort(), invocation);
    }
}
