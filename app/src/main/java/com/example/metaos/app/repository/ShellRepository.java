package com.example.metaos.app.repository;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.metaos.app.BuildConfig;
import com.example.metaos.app.model.ConnectionState;
import com.example.metaos.app.repository.transport.NetworkServiceDiscovery;
import com.example.metaos.app.service.MetaDaemonService;
import com.example.metaos.app.service.managers.ShellDaemonManager;

public class ShellRepository {

    private static final String TAG = "ShellRepository";
    private final NetworkServiceDiscovery serviceDiscovery;
    private MetaDaemonService metaService;
    private boolean isBound = false;

    private final MutableLiveData<ConnectionState> connectionState = new MutableLiveData<>(ConnectionState.INACTIVE);
    private final MutableLiveData<String> commandOutput = new MutableLiveData<>();


    public ShellRepository(Context context) {
        this.serviceDiscovery = new NetworkServiceDiscovery(context);

        Intent intent = new Intent(context, MetaDaemonService.class);
        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    //Smart transport selection
    public void initiateConnection() {
        updateState(ConnectionState.CONNECTING);

        serviceDiscovery.discoverLocalIp().thenAccept(localIp -> {
            String targetIp;
            int targetPort = BuildConfig.GRPC_PORT;

            if (localIp != null) {
                Log.i(TAG, "Direct mode selected: " + localIp);
                targetIp = localIp;
            } else {
                targetIp = BuildConfig.GRPC_HOST;
                Log.i(TAG, "Remote mode selected: " + targetIp);
            }

            if (isBound && metaService != null) {
                metaService.getShellDaemonManager().connect(targetIp, targetPort);
            } else {
                Log.e(TAG, "Cannot connect: Service not bound yet.");
                updateState(ConnectionState.ERROR);
            }

        });
    }

    public void sendCommand(String command) {
        if (isBound && metaService != null) {
            metaService.getShellDaemonManager().sendCommand(command);
        }
    }

    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder binder) {
            Log.d(TAG, "Service bounded successfully");

            MetaDaemonService.LocalBinder localBinder = (MetaDaemonService.LocalBinder) binder;
            metaService = localBinder.getService();
            isBound = true;


            ShellDaemonManager manager = metaService.getShellDaemonManager();

            manager.getState().observeForever(state -> {
                connectionState.postValue(state);
            });

            manager.getOutput().observeForever(text -> {
                commandOutput.postValue(text);
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName serviceName) {
            Log.w(TAG, "Service unbounded, crashed");
            isBound = false;
            metaService = null;
        }
    };

    private void updateState(ConnectionState state) {
        connectionState.postValue(state);
    }

    public LiveData<ConnectionState> getConnectionState() {
        return connectionState;
    }

    public LiveData<String> getCommandOutput() {
        return commandOutput;
    }

    public void cleanup(Context context) {
        if (isBound) {
            context.unbindService(serviceConnection);
            isBound = false;
        }
    }
}
