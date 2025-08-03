package org.example;

import com.example.grpc.Greeting;
import com.example.grpc.GreetingServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class Client {
    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:8080")
                .usePlaintext()
                .build();

        GreetingServiceGrpc.GreetingServiceBlockingStub stub = GreetingServiceGrpc.newBlockingStub(channel);

        Greeting.Request request = Greeting.Request.newBuilder().setName("Andrew").build();

        Greeting.Response response = stub.greeting(request);
        System.out.println(response.getGreeting());

        channel.shutdown();
    }
}
