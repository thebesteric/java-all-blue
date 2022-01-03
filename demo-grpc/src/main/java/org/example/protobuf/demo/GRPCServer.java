package org.example.protobuf.demo;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.example.protobuf.grpc.OrderServiceImpl;
import org.example.protobuf.grpc.RPCDataServiceImpl;

public class GRPCServer {
    public static void main(String[] args) throws Exception {
        Server server = ServerBuilder.forPort(9999)
                // 添加服务
                .addService(new RPCDataServiceImpl())
                .addService(new OrderServiceImpl())
                // 创建并启动服务
                .build().start();
        System.out.println("GRPC Server in 9999");
        server.awaitTermination();
    }
}
