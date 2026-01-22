package com.example.metaos.app.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.metaos.app.R;
import com.example.metaos.app.service.managers.ShellDaemonManager;

import javax.annotation.Nullable;

public class MetaDaemonService extends Service {

    private static final String TAG = "MetaDaemonService";
    private static final String CHANNEL_ID = "metaos_service_channel";
    private static final int NOTIFICATION_ID = 1;

    private ShellDaemonManager shellDaemonManager;

    private final IBinder binder = new LocalBinder();

    public class LocalBinder extends Binder {
        public MetaDaemonService getService() {
            return MetaDaemonService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "Creating Service...");

        this.shellDaemonManager = new ShellDaemonManager();

        createNotificationChannel();
    }

    @Override
    public int onStartCommand(Intent intent, int flag, int startId) {

        Log.i(TAG, "Starting foreground execution...");

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("MetaOS Client")
                .setContentText("Daemon connection active")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();

        startForeground(NOTIFICATION_ID, notification);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "Destroying service...");

        if (shellDaemonManager != null) {
            shellDaemonManager.disconnect();
        }
        super.onDestroy();
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public ShellDaemonManager getShellDaemonManager() {
        return shellDaemonManager;
    }


    public void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "MetaOS Connection Service",
                    NotificationManager.IMPORTANCE_LOW
            );
            serviceChannel.setDescription("Keeps the MetaOS daemon connection active");

            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null)
                manager.createNotificationChannel(serviceChannel);
        }
    }
}
