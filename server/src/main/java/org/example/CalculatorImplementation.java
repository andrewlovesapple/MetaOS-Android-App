package org.example;

import com.example.calculator.Calculator;
import com.example.calculator.CalculatorServiceGrpc;
import io.grpc.stub.StreamObserver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CalculatorImplementation extends CalculatorServiceGrpc.CalculatorServiceImplBase {

    @Override
    public StreamObserver<Calculator.Request> getStatistics(StreamObserver<Calculator.Response> responseObserver) {
        return new StreamObserver<Calculator.Request>() {

            private final List<Double> numbers = new ArrayList<>();//List for collecting numbers from the client

            @Override
            public void onNext(Calculator.Request request) {
                double number = request.getNumber();
                numbers.add(number);

                System.out.println("Received number: " + number);
            }

            @Override
            public void onError(Throwable throwable) {
                System.err.println("Error receiving number " + throwable.getMessage());
            }

            @Override
            public void onCompleted() {

                //When the client is done with sending numbers
                System.out.println("All numbers received");

                if (numbers.isEmpty()) {
                    //Handling case when no number was sent
                    Calculator.Response response = Calculator.Response.newBuilder()
                            .setCount(0)
                            .setSum(0)
                            .setAverage(0)
                            .setMin(0)
                            .setMax(0)
                            .build();

                    responseObserver.onNext(response);
                    responseObserver.onCompleted();
                    return;
                }

                int count = numbers.size();
                double sum = numbers.stream().mapToDouble(Double::doubleValue).sum();
                double average = sum / count;
                double min = Collections.min(numbers);
                double max = Collections.max(numbers);

                Calculator.Response response = Calculator.Response.newBuilder()
                        .setCount(count)
                        .setSum(sum)
                        .setAverage(average)
                        .setMin(min)
                        .setMax(max)
                        .build();

                responseObserver.onNext(response);
                responseObserver.onCompleted();
            }
        };
    }
}