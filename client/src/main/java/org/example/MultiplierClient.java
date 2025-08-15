package org.example;


import com.example.calculator.Multiplier;
import com.example.calculator.MultiplierServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

import static java.util.concurrent.TimeUnit.SECONDS;

public class MultiplierClient {
    public static void main(String[] args) throws InterruptedException {

        //Getting client ID
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the client ID: ");
        String ClientID = scanner.nextLine();

        ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:8080")
                .usePlaintext()
                .build();

        MultiplierServiceGrpc.MultiplierServiceStub stub = MultiplierServiceGrpc.newStub(channel);

        CountDownLatch finishedLatch = new CountDownLatch(1);


        StreamObserver<Multiplier.Response> responseObserver = new StreamObserver<Multiplier.Response>() {

            @Override
            public void onNext(Multiplier.Response response) {
                System.out.println("\nServer response for " + response.getClientId() + "\nSquared number: " + response.getMultipliedValue()
                        + " \nSum: " + response.getSum() + " \nCount: " + response.getCount());
                System.out.println("\nEnter a number: ");
            }

            @Override
            public void onError(Throwable throwable) {
                System.err.println("Error getting response " + throwable.getMessage());
                finishedLatch.countDown();
            }

            @Override
            public void onCompleted() {
                System.out.println("Server finished sending responses");
                finishedLatch.countDown();
            }
        };

        StreamObserver<Multiplier.Request> requestObserver = stub.multiplyNumber(responseObserver);

        System.out.println("Enter a number: ");

        try {
            String input;

            while (!(input = scanner.nextLine()).equals("exit")) {
                try {
                    double number = Double.parseDouble(input);

                    Multiplier.Request request = Multiplier.Request.newBuilder()
                            .setNumber(number)
                            .setClientId(ClientID)
                            .build();

                    System.out.println("Sending: " + number);
                    requestObserver.onNext(request);

                } catch (Exception e) {
                    System.out.println("Please enter a valid number or exit");
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading input: " + e.getMessage());
            requestObserver.onError(e);
        }

        requestObserver.onCompleted();

        try {

            if (!finishedLatch.await(20, SECONDS)) {
                System.out.println("Timeout waiting for the server ");
            }
        } catch (InterruptedException e) {
            System.out.println("Interrupted while waiting ");
        }

        channel.shutdown();
        scanner.close();
    }
}
