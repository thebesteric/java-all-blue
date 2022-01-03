package org.example.protobuf.demo;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.example.protobuf.proto.*;

import java.util.Iterator;

public class GPRCClient {
    public static void main(String[] args) {
        // 1、构建 channel
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9999)
                // 明文传输，不加密
                .usePlaintext()
                .build();
        try {
            // getRPCData(channel);
            listOrders(channel);
        }finally {
            // 5、关闭 channel
            channel.shutdown();
        }

    }

    public static void getRPCData(ManagedChannel channel) {
        // 2、通过 channel 创建 stub
        RPCDataServiceGrpc.RPCDataServiceBlockingStub blockingStub = RPCDataServiceGrpc.newBlockingStub(channel);
        // 3、构建请求消息
        RPCDataRequest request = RPCDataRequest.newBuilder().setUserName("张三").build();
        // 4、发送请求
        RPCDataResponse response = blockingStub.getData(request);

        System.out.println(response.getCode());
        System.out.println(response.getServerData());
        System.out.println(response.getMessage());
    }

    public static void listOrders(ManagedChannel channel) {
        OrderServiceGrpc.OrderServiceBlockingStub blockingStub = OrderServiceGrpc.newBlockingStub(channel);
        Buyer buyer = Buyer.newBuilder().setBuyerId(1).build();
        Iterator<Order> orderIterator = blockingStub.listOrders(buyer);

        while (orderIterator.hasNext()) {
            Order order = orderIterator.next();
            System.out.println(order.getOrderId());
            System.out.println(order.getProductId());
            System.out.println(order.getOrderTime());
            System.out.println(order.getBuyerMarker());
            System.out.println("====================");
        }
    }
}
