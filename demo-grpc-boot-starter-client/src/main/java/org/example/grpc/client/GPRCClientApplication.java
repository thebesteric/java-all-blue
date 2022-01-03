package org.example.grpc.client;

import io.grpc.ClientInterceptor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.interceptor.GrpcGlobalClientInterceptor;
import org.example.grpc.client.interceptor.GRPCLogInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@Slf4j
@EnableDiscoveryClient
public class GPRCClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(GPRCClientApplication.class, args);
    }

    @GrpcGlobalClientInterceptor
    public ClientInterceptor logServerInterceptor() {
        return new GRPCLogInterceptor();
    }
}
