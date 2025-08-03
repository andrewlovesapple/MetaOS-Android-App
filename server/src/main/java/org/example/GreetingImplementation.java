package org.example;

import com.example.grpc.Greeting;
import com.example.grpc.GreetingServiceGrpc;
import io.grpc.stub.StreamObserver;

public class GreetingImplementation extends GreetingServiceGrpc.GreetingServiceImplBase {
    @Override
    public void greeting(Greeting.Request request, StreamObserver<Greeting.Response> responseObserver) {
        System.out.println(request.getName() + " connected");

        Greeting.Response response = Greeting.Response.newBuilder()
                .setGreeting(request.getName() + ", hello from server!")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();

    }
}
