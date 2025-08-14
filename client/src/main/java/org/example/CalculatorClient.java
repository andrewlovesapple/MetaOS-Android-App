package org.example;

import com.example.calculator.Calculator;
import com.example.calculator.CalculatorServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class CalculatorClient {
    public static void main(String[] args) throws InterruptedException {

        ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:8080")
                .usePlaintext()
                .build();

        CalculatorServiceGrpc.CalculatorServiceStub asyncStub = CalculatorServiceGrpc.newStub(channel);//Using async stub for sending multiple requests

        CountDownLatch finishLatch = new CountDownLatch(1);//For waiting

        StreamObserver<Calculator.Response> responseObserver = new StreamObserver<Calculator.Response>() {

            @Override
            public void onNext(Calculator.Response response) {
                System.out.println("Statistic from the server:");
                System.out.println("Count: " + response.getCount());
                System.out.println("Sum: " + response.getSum());
                System.out.println("Average: " + response.getAverage());
                System.out.println("Min: " + response.getMin());
                System.out.println("Max: " + response.getMax());
            }

            @Override
            public void onError(Throwable throwable) {
                System.err.println(throwable.getMessage());
                finishLatch.countDown();//Stop waiting
            }

            @Override
            public void onCompleted() {
                System.out.println("Server finished sending response");
                finishLatch.countDown();//Stop waiting
            }
        };
        //Passing responseObserver to the server and receiving requestObserver from it
        StreamObserver<Calculator.Request> requestObserver = asyncStub.getStatistics(responseObserver);

        try {
            double[] numbers = {7.8, 3.4, 3.2, 5.9, 8.5, 1.44, 6.8, 3.45, 9.99};

            for (double number : numbers) {
                System.out.println("Sending: " + number);

                //Building each number sending request
                Calculator.Request request = Calculator.Request.newBuilder()
                        .setNumber(number)
                        .build();

                requestObserver.onNext(request);

                Thread.sleep(1000);
            }

            System.out.println("Finished sending all numbers\n");
            requestObserver.onCompleted();

        } catch (Exception e) {
            System.err.println("Error sending numbers: " + e.getMessage());
            requestObserver.onError(e);
        }

        if (!finishLatch.await(30, TimeUnit.SECONDS)) {
            System.err.println("Timeout waiting for server response");
        }
        channel.shutdown();
    }
}
