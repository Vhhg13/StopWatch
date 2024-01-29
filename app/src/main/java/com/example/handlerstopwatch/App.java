package com.example.handlerstopwatch;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

public class App extends Application {

    NotificationManager nManager;
    public StopWatchService service;

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    public void createNotificationChannel(){
        String name = getString(R.string.n_channel_name);
        String description = getString(R.string.n_channel_desc);
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(getString(R.string.n_channel_id), name, importance);
        channel.setDescription(description);

        nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nManager.createNotificationChannel(channel);
    }
}
