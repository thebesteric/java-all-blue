package org.example.grpc.server;

import io.grpc.ServerInterceptor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor;
import org.example.grpc.server.interceptor.GRPCLogInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@Slf4j
@EnableDiscoveryClient
public class GPRCServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(GPRCServerApplication.class, args);
    }

    @GrpcGlobalServerInterceptor
    public ServerInterceptor logServerInterceptor() {
        return new GRPCLogInterceptor();
    }
}
