package org.example.ribbon;

import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.LoadBalancerBuilder;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.reactive.LoadBalancerCommand;
import com.netflix.loadbalancer.reactive.ServerOperation;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import rx.Observable;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class TestRibbon {

    @Test
    public void testRibbon() {
        List<Server> serverList = Lists.newArrayList(
                new Server("localhost", 8020),
                new Server("localhost", 8021));

        // 构建负载均衡实例
        ILoadBalancer loadBalancer = LoadBalancerBuilder.newBuilder().buildFixedServerListLoadBalancer(serverList);

        for (int i = 0; i < 5; i++) {
            Observable<Object> observable = LoadBalancerCommand.builder()
                    .withLoadBalancer(loadBalancer).build()
                    .submit(new ServerOperation<Object>() {
                        @Override
                        public Observable<Object> call(Server server) {
                            String serverAddr = "http://" + server.getHost() + ":" + server.getPort() + "/account/1";
                            System.out.println("调用地址为: " + serverAddr);
                            try {
                                URL url = new URL(serverAddr);
                                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                conn.setRequestMethod("GET");
                                conn.connect();

                                InputStream in = conn.getInputStream();
                                byte[] bytes = new byte[in.available()];
                                in.read(bytes);

                                return Observable.just(new String(bytes));
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                            return null;
                        }
                    });
            Object result = observable.toBlocking().first();
            System.out.println("调用结果: " + result);
        }
    }

}
