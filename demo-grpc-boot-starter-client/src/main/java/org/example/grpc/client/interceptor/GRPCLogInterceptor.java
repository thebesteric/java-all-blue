package org.example.grpc.client.interceptor;

import io.grpc.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GRPCLogInterceptor implements ClientInterceptor {

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> methodDescriptor, CallOptions callOptions, Channel channel) {
        log.info("客户端 interceptor: " + methodDescriptor.getFullMethodName());
        return channel.newCall(methodDescriptor, callOptions);
    }
}
