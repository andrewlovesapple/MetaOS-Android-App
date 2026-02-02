package com.example.metaos.app.repository.transport;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.util.Log;

import java.net.InetAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class NetworkServiceDiscovery {

    private static final String TAG = "MetaOS_Discovery";
    private static final String SERVICE_TYPE = "_metaos._tcp.";
    private static final int DISCOVERY_TIMEOUT = 2000;

    private final NsdManager nsdManager;

    public NetworkServiceDiscovery(Context context) {
        this.nsdManager = (NsdManager) context.getSystemService(Context.NSD_SERVICE);
    }

    //Finding server IP
    public CompletableFuture<String> discoverLocalIp() {

        CompletableFuture<String> futureResult = new CompletableFuture<>();

        NsdManager.DiscoveryListener discoveryListener = new NsdManager.DiscoveryListener() {

            @Override
            public void onDiscoveryStarted(String regType) {
                Log.d(TAG, "Scanning for server...");
            }

            @Override
            public void onServiceFound(NsdServiceInfo service) {
                Log.d(TAG, "Server found: " + service.getServiceName());
                nsdManager.resolveService(service, new NsdManager.ResolveListener() {
                    @Override
                    public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
                        Log.e(TAG, "Failed to get IP. Error: " + errorCode);
                    }

                    @Override
                    public void onServiceResolved(NsdServiceInfo serviceInfo) {
                        Log.d(TAG, "Resolved IP: " + serviceInfo.getHost());
                        InetAddress host = serviceInfo.getHost();

                        if (!futureResult.isDone()) {
                            futureResult.complete(host.getHostAddress());
                        }
                    }
                });
            }

            @Override
            public void onServiceLost(NsdServiceInfo service) {
                Log.e(TAG, "Server lost: " + service);
            }

            @Override
            public void onDiscoveryStopped(String serviceType) {
                Log.i(TAG, "Discovery stopped.");
            }

            @Override
            public void onStartDiscoveryFailed(String serviceType, int errorCode) {
                Log.e(TAG, "Failed to start discovery: Error code:" + errorCode);
                nsdManager.stopServiceDiscovery(this);
                if (!futureResult.isDone()) futureResult.complete(null);
            }

            @Override
            public void onStopDiscoveryFailed(String serviceType, int errorCode) {
                nsdManager.stopServiceDiscovery(this);
            }
        };

        try {
            nsdManager.discoverServices(SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, discoveryListener);
        } catch (Exception e) {
            Log.e(TAG, "Error starting discovery", e);
            return CompletableFuture.completedFuture(null);
        }

        //Cancelling the discovery after timeout
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.schedule(() -> {
            if (!futureResult.isDone()) {
                Log.w(TAG, "Timeout: No local server found");
                try {
                    nsdManager.stopServiceDiscovery(discoveryListener);
                } catch (Exception e) {
                    Log.e(TAG, "Error stopping discovery ");
                }
                futureResult.complete(null);
            }
            scheduler.shutdown();
        }, DISCOVERY_TIMEOUT, TimeUnit.MILLISECONDS);

        return futureResult.thenApply(result -> {
            try {
                nsdManager.stopServiceDiscovery(discoveryListener);
            } catch (Exception e) {
                Log.e(TAG, "Error stopping discovery: " + e.getMessage());
            }
            return result;
        });
    }
}
