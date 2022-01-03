package org.example.protobuf.proto;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 * <pre>
 * 服务接口，定义请求参数和响应结果
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.43.0)",
    comments = "Source: helloworld.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class RPCDataServiceGrpc {

  private RPCDataServiceGrpc() {}

  public static final String SERVICE_NAME = "org.example.protobuf.proto.RPCDataService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<RPCDataRequest,
      RPCDataResponse> getGetDataMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getData",
      requestType = RPCDataRequest.class,
      responseType = RPCDataResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<RPCDataRequest,
      RPCDataResponse> getGetDataMethod() {
    io.grpc.MethodDescriptor<RPCDataRequest, RPCDataResponse> getGetDataMethod;
    if ((getGetDataMethod = RPCDataServiceGrpc.getGetDataMethod) == null) {
      synchronized (RPCDataServiceGrpc.class) {
        if ((getGetDataMethod = RPCDataServiceGrpc.getGetDataMethod) == null) {
          RPCDataServiceGrpc.getGetDataMethod = getGetDataMethod =
              io.grpc.MethodDescriptor.<RPCDataRequest, RPCDataResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getData"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  RPCDataRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  RPCDataResponse.getDefaultInstance()))
              .setSchemaDescriptor(new RPCDataServiceMethodDescriptorSupplier("getData"))
              .build();
        }
      }
    }
    return getGetDataMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static RPCDataServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<RPCDataServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<RPCDataServiceStub>() {
        @Override
        public RPCDataServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new RPCDataServiceStub(channel, callOptions);
        }
      };
    return RPCDataServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static RPCDataServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<RPCDataServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<RPCDataServiceBlockingStub>() {
        @Override
        public RPCDataServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new RPCDataServiceBlockingStub(channel, callOptions);
        }
      };
    return RPCDataServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static RPCDataServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<RPCDataServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<RPCDataServiceFutureStub>() {
        @Override
        public RPCDataServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new RPCDataServiceFutureStub(channel, callOptions);
        }
      };
    return RPCDataServiceFutureStub.newStub(factory, channel);
  }

  /**
   * <pre>
   * 服务接口，定义请求参数和响应结果
   * </pre>
   */
  public static abstract class RPCDataServiceImplBase implements io.grpc.BindableService {

    /**
     * <pre>
     * API
     * </pre>
     */
    public void getData(RPCDataRequest request,
                        io.grpc.stub.StreamObserver<RPCDataResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetDataMethod(), responseObserver);
    }

    @Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getGetDataMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                RPCDataRequest,
                RPCDataResponse>(
                  this, METHODID_GET_DATA)))
          .build();
    }
  }

  /**
   * <pre>
   * 服务接口，定义请求参数和响应结果
   * </pre>
   */
  public static final class RPCDataServiceStub extends io.grpc.stub.AbstractAsyncStub<RPCDataServiceStub> {
    private RPCDataServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected RPCDataServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new RPCDataServiceStub(channel, callOptions);
    }

    /**
     * <pre>
     * API
     * </pre>
     */
    public void getData(RPCDataRequest request,
                        io.grpc.stub.StreamObserver<RPCDataResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetDataMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * <pre>
   * 服务接口，定义请求参数和响应结果
   * </pre>
   */
  public static final class RPCDataServiceBlockingStub extends io.grpc.stub.AbstractBlockingStub<RPCDataServiceBlockingStub> {
    private RPCDataServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected RPCDataServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new RPCDataServiceBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * API
     * </pre>
     */
    public RPCDataResponse getData(RPCDataRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetDataMethod(), getCallOptions(), request);
    }
  }

  /**
   * <pre>
   * 服务接口，定义请求参数和响应结果
   * </pre>
   */
  public static final class RPCDataServiceFutureStub extends io.grpc.stub.AbstractFutureStub<RPCDataServiceFutureStub> {
    private RPCDataServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected RPCDataServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new RPCDataServiceFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * API
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<RPCDataResponse> getData(
        RPCDataRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetDataMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_DATA = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final RPCDataServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(RPCDataServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_DATA:
          serviceImpl.getData((RPCDataRequest) request,
              (io.grpc.stub.StreamObserver<RPCDataResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @Override
    @SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class RPCDataServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    RPCDataServiceBaseDescriptorSupplier() {}

    @Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return RPCDataServiceApi.getDescriptor();
    }

    @Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("RPCDataService");
    }
  }

  private static final class RPCDataServiceFileDescriptorSupplier
      extends RPCDataServiceBaseDescriptorSupplier {
    RPCDataServiceFileDescriptorSupplier() {}
  }

  private static final class RPCDataServiceMethodDescriptorSupplier
      extends RPCDataServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    RPCDataServiceMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (RPCDataServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new RPCDataServiceFileDescriptorSupplier())
              .addMethod(getGetDataMethod())
              .build();
        }
      }
    }
    return result;
  }
}
