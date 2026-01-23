package com.example.metaos.app.network.grpc;

import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import Agent.AgentService.ExecuteShellRequest;
import Agent.AgentService.ExecuteShellResponse;
import Agent.ShellControllerServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

public class ShellGrpcClient {

    private static final String TAG = "ShellGrpcClient";

    private ManagedChannel channel;

    private ShellControllerServiceGrpc.ShellControllerServiceStub asyncStub;

    private final ExecutorService executor = Executors.newSingleThreadExecutor(); //Background thread for network operations

    private StreamObserver<ExecuteShellRequest> requestObserver;


    public void connect(String host, int port, ShellOutputListener listener) {
        Log.d(TAG, "Connecting to " + host + ":" + port);

        channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .executor(executor)
                .build();


        asyncStub = ShellControllerServiceGrpc.newStub(channel);


        //Incoming data handler
        StreamObserver<ExecuteShellResponse> responseObserver = new StreamObserver<ExecuteShellResponse>() {

            @Override
            public void onNext(ExecuteShellResponse value) {
                listener.onOutput(value.getOutput());
            }

            @Override
            public void onError(Throwable t) {
                Log.e(TAG, "Connection error", t);
                listener.onError(t);
                shutdown();
            }

            @Override
            public void onCompleted() {
                Log.i(TAG, "Server closed session");
                listener.onDisconnected();
                shutdown();
            }
        };

        //Opening the stream
        requestObserver = asyncStub.executeShell(responseObserver);

        listener.onConnected();
    }

    public void sendCommand(String command) {
        if (requestObserver != null) {
            Log.d(TAG, "Sending command: " + command);

            //Building Protobuf message
            ExecuteShellRequest request = ExecuteShellRequest.newBuilder()
                    .setCommand(command)
                    .build();

            requestObserver.onNext(request);
        } else {
            Log.e(TAG, "Failed sending the command. Connection closed.");
        }
    }

    public void shutdown() {
        //Closing the stream
        if (requestObserver != null) {
            try {
                requestObserver.onCompleted();
            } catch (Exception e) {
                Log.w(TAG, "Error during stream closure" + e.getMessage());
            }
            requestObserver = null;
        }

        if (channel != null && !channel.isShutdown()) {
            try {
                channel.shutdown().awaitTermination(1, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public interface ShellOutputListener {
        void onConnected();

        void onDisconnected();

        void onOutput(String output);

        void onError(Throwable t);
    }
}