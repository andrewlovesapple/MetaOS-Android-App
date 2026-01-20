package com.example.metaos.app.service.managers;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.metaos.app.model.ConnectionState;
import com.example.metaos.app.network.grpc.ShellGrpcClient;

public class ShellDaemonManager implements ShellGrpcClient.ShellOutputListener {

    private static final String TAG = "ShellDaemonManager";

    private final ShellGrpcClient grpcClient;

    private final MutableLiveData<ConnectionState> connectionState = new MutableLiveData<>(ConnectionState.IDLE);
    private final MutableLiveData<String> commandOutput = new MutableLiveData<>();

    private Handler mainHandler = new Handler(Looper.getMainLooper()); //Threading bridge

    //Reconnection variables
    private int retryCount = 0;
    private static final int MAX_RETRIES = 3;
    private String currentHost;

    private int currentPort;
    private boolean explicitDisconnect = false;

    public ShellDaemonManager() {
        this.grpcClient = new ShellGrpcClient();
    }


    public void connect(String host, int port) {
        this.currentHost = host;
        this.currentPort = port;
        this.explicitDisconnect = false;
        this.retryCount = 0;

        attemptConnection();
    }

    private void attemptConnection() {
        updateState(ConnectionState.CONNECTING);
        grpcClient.connect(currentHost, currentPort, this);
    }

    public void sendCommand(String command) {
        if (connectionState.getValue() == ConnectionState.CONNECTED) {
            grpcClient.sendCommand(command);
        } else {
            Log.w(TAG, "Cannot send command. State: " + connectionState.getValue());
        }
    }

    public void disconnect() {
        explicitDisconnect = true;
        grpcClient.shutdown();
        updateState(ConnectionState.IDLE);
    }

    @Override
    public void onConnected() {
        retryCount = 0;
        connectionState.postValue(ConnectionState.CONNECTED);
    }

    @Override
    public void onDisconnected() {
        if (!explicitDisconnect) {
            handleConnectionFailure();
        }
    }

    @Override
    public void onOutput(String output) {
        commandOutput.postValue(output);
    }

    @Override
    public void onError(Throwable t) {
        Log.e(TAG, "Manager error", t);
        handleConnectionFailure();
    }

    private void handleConnectionFailure() {
        if (explicitDisconnect) return;

        if (retryCount < MAX_RETRIES) {
            retryCount++;

            long delayMs = (long) Math.pow(2, retryCount) * 1000;

            Log.w(TAG, "Connection lost. Retrying in " + delayMs + " ms");
            connectionState.postValue(ConnectionState.RECONNECTING);

            mainHandler.postDelayed(() -> attemptConnection(), delayMs);
        } else {
            Log.e(TAG, "Max retries reached!");
            connectionState.postValue(ConnectionState.ERROR);
        }
    }

    private void updateState(ConnectionState state) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            connectionState.setValue(state);
        } else {
            connectionState.postValue(state);
        }

    }

    public LiveData<ConnectionState> getState() {
        return connectionState;
    }

    public LiveData<String> getOutput() {
        return commandOutput;
    }
}



