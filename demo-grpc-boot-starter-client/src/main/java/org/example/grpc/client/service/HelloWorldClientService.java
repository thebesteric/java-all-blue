package org.example.grpc.client.service;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.example.protobuf.proto.HelloRequest;
import org.example.protobuf.proto.HelloResponse;
import org.example.protobuf.proto.HelloWorldServiceGrpc;
import org.springframework.stereotype.Service;

@Service
public class HelloWorldClientService {

    @GrpcClient("local-grpc-server")
    private HelloWorldServiceGrpc.HelloWorldServiceBlockingStub helloWorldServiceBlockingStub;

    public String sendMessage(String name) {
        HelloRequest request = HelloRequest.newBuilder().setName(name).build();
        HelloResponse response = helloWorldServiceBlockingStub.sayHello(request);
        return response.getMessage();
    }

}
