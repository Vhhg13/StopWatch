package com.example.handlerstopwatch;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import java.util.Objects;

class MyThread extends Thread{
    final StopWatchContainer stopWatchContainer;
    MyThread(StopWatchContainer stopWatchContainer){
        this.stopWatchContainer = stopWatchContainer;
    }
    final Handler mainHandler = new Handler(Looper.getMainLooper());

    long lastPause = SystemClock.elapsedRealtime();
    long timePassed = 0;
    Handler handler;
    boolean isPaused = false;
    @Override
    public void run() {
        Looper.prepare();
        handler = new Handler(Objects.requireNonNull(Looper.myLooper()));
        unpause();
        Looper.loop();
    }
    public void pause(){
        isPaused = true;
        handler.removeCallbacksAndMessages(null);
        timePassed += SystemClock.elapsedRealtime() - lastPause;
        lastPause = SystemClock.elapsedRealtime();
    }
    public void unpause(){
        timePassed -= SystemClock.elapsedRealtime() - lastPause;
        isPaused = false;
        handler.post(new Runnable() {
            @Override
            public void run() {
                mainHandler.post(() -> stopWatchContainer.updateStopWatch(String.valueOf((timePassed + SystemClock.elapsedRealtime() - lastPause)/1000)));
                handler.postDelayed(this, 100);
            }
        });
    }

    public void reset(){
        timePassed = 0;
        lastPause = SystemClock.elapsedRealtime();
        mainHandler.post(() -> stopWatchContainer.updateStopWatch("0"));
    }
}