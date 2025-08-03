package com.example.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.63.0)",
    comments = "Source: greeting.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class GreetingServiceGrpc {

  private GreetingServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "com.example.grpc.GreetingService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.example.grpc.Greeting.Request,
      com.example.grpc.Greeting.Response> getGreetingMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "greeting",
      requestType = com.example.grpc.Greeting.Request.class,
      responseType = com.example.grpc.Greeting.Response.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.example.grpc.Greeting.Request,
      com.example.grpc.Greeting.Response> getGreetingMethod() {
    io.grpc.MethodDescriptor<com.example.grpc.Greeting.Request, com.example.grpc.Greeting.Response> getGreetingMethod;
    if ((getGreetingMethod = GreetingServiceGrpc.getGreetingMethod) == null) {
      synchronized (GreetingServiceGrpc.class) {
        if ((getGreetingMethod = GreetingServiceGrpc.getGreetingMethod) == null) {
          GreetingServiceGrpc.getGreetingMethod = getGreetingMethod =
              io.grpc.MethodDescriptor.<com.example.grpc.Greeting.Request, com.example.grpc.Greeting.Response>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "greeting"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.grpc.Greeting.Request.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.grpc.Greeting.Response.getDefaultInstance()))
              .setSchemaDescriptor(new GreetingServiceMethodDescriptorSupplier("greeting"))
              .build();
        }
      }
    }
    return getGreetingMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static GreetingServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<GreetingServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<GreetingServiceStub>() {
        @java.lang.Override
        public GreetingServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new GreetingServiceStub(channel, callOptions);
        }
      };
    return GreetingServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static GreetingServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<GreetingServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<GreetingServiceBlockingStub>() {
        @java.lang.Override
        public GreetingServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new GreetingServiceBlockingStub(channel, callOptions);
        }
      };
    return GreetingServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static GreetingServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<GreetingServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<GreetingServiceFutureStub>() {
        @java.lang.Override
        public GreetingServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new GreetingServiceFutureStub(channel, callOptions);
        }
      };
    return GreetingServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void greeting(com.example.grpc.Greeting.Request request,
        io.grpc.stub.StreamObserver<com.example.grpc.Greeting.Response> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGreetingMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service GreetingService.
   */
  public static abstract class GreetingServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return GreetingServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service GreetingService.
   */
  public static final class GreetingServiceStub
      extends io.grpc.stub.AbstractAsyncStub<GreetingServiceStub> {
    private GreetingServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected GreetingServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new GreetingServiceStub(channel, callOptions);
    }

    /**
     */
    public void greeting(com.example.grpc.Greeting.Request request,
        io.grpc.stub.StreamObserver<com.example.grpc.Greeting.Response> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGreetingMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service GreetingService.
   */
  public static final class GreetingServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<GreetingServiceBlockingStub> {
    private GreetingServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected GreetingServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new GreetingServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.example.grpc.Greeting.Response greeting(com.example.grpc.Greeting.Request request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGreetingMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service GreetingService.
   */
  public static final class GreetingServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<GreetingServiceFutureStub> {
    private GreetingServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected GreetingServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new GreetingServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.grpc.Greeting.Response> greeting(
        com.example.grpc.Greeting.Request request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGreetingMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GREETING = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GREETING:
          serviceImpl.greeting((com.example.grpc.Greeting.Request) request,
              (io.grpc.stub.StreamObserver<com.example.grpc.Greeting.Response>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getGreetingMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.example.grpc.Greeting.Request,
              com.example.grpc.Greeting.Response>(
                service, METHODID_GREETING)))
        .build();
  }

  private static abstract class GreetingServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    GreetingServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.example.grpc.Greeting.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("GreetingService");
    }
  }

  private static final class GreetingServiceFileDescriptorSupplier
      extends GreetingServiceBaseDescriptorSupplier {
    GreetingServiceFileDescriptorSupplier() {}
  }

  private static final class GreetingServiceMethodDescriptorSupplier
      extends GreetingServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    GreetingServiceMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (GreetingServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new GreetingServiceFileDescriptorSupplier())
              .addMethod(getGreetingMethod())
              .build();
        }
      }
    }
    return result;
  }
}
