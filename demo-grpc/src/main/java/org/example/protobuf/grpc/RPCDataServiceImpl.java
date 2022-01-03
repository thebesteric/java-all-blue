package org.example.protobuf.grpc;

import io.grpc.stub.StreamObserver;
import org.example.protobuf.proto.RPCDataRequest;
import org.example.protobuf.proto.RPCDataResponse;
import org.example.protobuf.proto.RPCDataServiceGrpc;

import java.util.Date;

public class RPCDataServiceImpl extends RPCDataServiceGrpc.RPCDataServiceImplBase {
    @Override
    public void getData(RPCDataRequest request, StreamObserver<RPCDataResponse> responseObserver) {
        // 1、获取请求参数
        String userName = request.getUserName();
        // 2、业务处理
        // ...
        String result = "hello " + userName + ", " + new Date();
        // 3、封装结果
        RPCDataResponse.Builder responseBuilder = RPCDataResponse.newBuilder();
        responseBuilder.setCode(200).setServerData(result).setMessage("succeed");
        RPCDataResponse response = responseBuilder.build();
        // 4、返回结果
        responseObserver.onNext(response);
        // 5、关闭
        responseObserver.onCompleted();
    }
}
