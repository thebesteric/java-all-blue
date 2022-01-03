package org.example.protobuf.grpc;

import io.grpc.stub.StreamObserver;
import org.example.protobuf.proto.Buyer;
import org.example.protobuf.proto.Order;
import org.example.protobuf.proto.OrderServiceGrpc;

import java.util.ArrayList;
import java.util.List;

public class OrderServiceImpl extends OrderServiceGrpc.OrderServiceImplBase {
    @Override
    public void listOrders(Buyer request, StreamObserver<Order> responseObserver) {
        for (Order order : mockOrders()) {
            responseObserver.onNext(order);
        }
        responseObserver.onCompleted();
    }

    private List<Order> mockOrders() {
        List<Order> orders = new ArrayList<>();
        Order.Builder orderBuild = Order.newBuilder();
        for (int i = 0; i < 10; i++) {
            orders.add(orderBuild.setOrderId(i)
                    .setProductId(1000 + i)
                    .setOrderTime(System.currentTimeMillis() / 1000)
                    .setBuyerMarker("remark" + i)
                    .build());
        }
        return orders;
    }
}
