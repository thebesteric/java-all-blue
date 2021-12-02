package org.example.demo.dubbo.provider.service;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.protocol.rest.support.ContentType;
import org.example.dubbo.iface.DemoService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

@DubboService(version = "rest", protocol = "p3")
@Path("rest/demo")
public class RestDemoService implements DemoService {

    @GET
    @Path("sayHello")
    @Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
    @Override
    public String sayHello(@QueryParam("name") String name) {
        System.out.println(RestDemoService.class.getSimpleName() + " 执行了任务: " + name);
        URL url = RpcContext.getContext().getUrl();
        return String.format("%s, %s, Hello %s", url.getProtocol(), url.getPort(), name);
    }
}
