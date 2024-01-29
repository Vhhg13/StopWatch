package com.example.handlerstopwatch;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LifecycleService;
import androidx.lifecycle.LiveData;

public class StopWatchService extends LifecycleService {
    private final MyThread thread = new MyThread();
    private RemoteViews remoteViews;
    public final LiveData<Long> timeElapsed = thread.currentTime;
    private Notification.Builder notificationBuilder;


    @Override
    public void onCreate() {
        super.onCreate();
        prepareNotifications();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            startForeground(1, formNotification(0));
        }
        timeElapsed.observe(this, time -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).notify(1, formNotification(time));
            }
        });
        thread.start();
    }

    private void prepareNotifications() {
        Intent intent = new Intent(this, NotificationReceiver.class);
        intent.setAction("pause_unpause_action");
        PendingIntent togglePauseIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        Intent intent1 = new Intent(this, NotificationReceiver.class);
        intent1.setAction("stop");
        PendingIntent resetIntent = PendingIntent.getBroadcast(this, 1, intent1, PendingIntent.FLAG_IMMUTABLE);

        remoteViews = new RemoteViews(getApplicationContext().getPackageName(), R.layout.custom_notification_layout);
        remoteViews.setOnClickPendingIntent(R.id.playpause, togglePauseIntent);
        remoteViews.setOnClickPendingIntent(R.id.reset, resetIntent);
        remoteViews.setTextViewText(R.id.tw, "0");
        notificationBuilder = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            notificationBuilder = new Notification.Builder(this, getString(R.string.n_channel_id))
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Stop Watch")
                    .setContentText("Notification Text")
                    .setCustomContentView(remoteViews)
                    .setForegroundServiceBehavior(Notification.FOREGROUND_SERVICE_IMMEDIATE)
                    .setCategory(Notification.CATEGORY_STOPWATCH);
        }
        assert notificationBuilder != null;
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    private Notification formNotification(long time){
        remoteViews.setTextViewText(R.id.tw, String.valueOf(time));
        return notificationBuilder.build();
    }

    private final StopWatchServiceBinder binder = new StopWatchServiceBinder();

    public void reset() {
        thread.reset();
    }

    class StopWatchServiceBinder extends Binder{
        public StopWatchService getService(){
            return StopWatchService.this;
        }
    }
    @Nullable
    @Override
    public IBinder onBind(@NonNull Intent intent) {
        super.onBind(intent);
        return binder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

//    public void pause(){
//        thread.pause();
//    }
//
//    public void unpause(){
//        thread.unpause();
//    }
    public void togglePause(){
        if(thread.isPaused)
            thread.unpause();
        else
            thread.pause();
    }
}
