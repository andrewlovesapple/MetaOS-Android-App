package org.example;


import MetaOS.ShellControllerServiceGrpc;
import MetaOS.System.ExecuteShellRequest;
import MetaOS.System.ExecuteShellResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


public class Client {
    public static void main(String[] args) {

        int serverPort = Integer.parseInt(System.getenv("GRPC_SERVER_PORT"));

        String serverIp = System.getenv("GRPC_SERVER_HOST");
        if (serverIp == null || serverIp.isEmpty()) {
            serverIp = "localhost";
        }

        ManagedChannel channel = ManagedChannelBuilder.forAddress(serverIp, serverPort)
                .usePlaintext()
                .build();

        try {
            ShellControllerServiceGrpc.ShellControllerServiceStub stub = ShellControllerServiceGrpc.newStub(channel);

            CountDownLatch latch = new CountDownLatch(1);

            StreamObserver<ExecuteShellResponse> responseObserver = new StreamObserver<ExecuteShellResponse>() {

                @Override
                public void onNext(ExecuteShellResponse response) {
                    System.out.println(response.getOutput());
                }

                @Override
                public void onError(Throwable throwable) {
                    System.out.println("Error: " + throwable.getMessage());
                    latch.countDown();
                }

                @Override
                public void onCompleted() {
                    System.out.println("Disconnected");
                    latch.countDown();
                }
            };

            StreamObserver<ExecuteShellRequest> requestObserver = stub.executeShell(responseObserver);

            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter the command: ");

            while (true) {
                String command = scanner.nextLine();

                if ("exit".equalsIgnoreCase(command)) {
                    requestObserver.onCompleted();
                    break;
                }

                if (!command.trim().isEmpty()) {
                    requestObserver.onNext(ExecuteShellRequest.newBuilder()
                            .setCommand(command + "\n")
                            .build());
                }
            }
            latch.await(5, TimeUnit.SECONDS);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            channel.shutdown();
        }
    }
}