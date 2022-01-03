package org.example.grpc.server.service;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.example.protobuf.proto.HelloRequest;
import org.example.protobuf.proto.HelloResponse;
import org.example.protobuf.proto.HelloWorldServiceGrpc;

@GrpcService
public class HelloWorldServiceImpl extends HelloWorldServiceGrpc.HelloWorldServiceImplBase {
    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {
        String name = request.getName();
        HelloResponse response = HelloResponse.newBuilder().setMessage("hello " + name).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
