package org.example;

import com.example.calculator.Multiplier;
import com.example.calculator.MultiplierServiceGrpc;
import io.grpc.stub.StreamObserver;

public class MultiplierImpl extends MultiplierServiceGrpc.MultiplierServiceImplBase {

    @Override
    public StreamObserver<Multiplier.Request> multiplyNumber(StreamObserver<Multiplier.Response> responseObserver) {

        return new StreamObserver<Multiplier.Request>() {

            private double sum = 0;
            private int count = 0;
            private String currentClientID = null;

            @Override
            public void onNext(Multiplier.Request request) {
                double number = request.getNumber();
                String clientID = request.getClientId();

                if (currentClientID == null) {
                    currentClientID = clientID;
                    System.out.println(currentClientID + " connected");
                }

                //Updating the variables
                double multipliedNumber = number * number;
                sum += multipliedNumber;
                count++;

                Multiplier.Response result = Multiplier.Response.newBuilder()
                        .setMultipliedValue(multipliedNumber)
                        .setCount(count)
                        .setSum(sum)
                        .setClientId(currentClientID)
                        .build();

                try {
                    responseObserver.onNext(result);
                } catch (Exception e) {
                    System.err.println("Error sending response to " + currentClientID);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                System.err.println("Error from client " + currentClientID + throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println(currentClientID + " disconnected");

                responseObserver.onCompleted(); //Telling the client that the server finished sending responses
            }
        };
    }
}