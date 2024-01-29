package com.example.handlerstopwatch;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Objects;

class MyThread extends Thread{

    private final MutableLiveData<Long> _currentTime = new MutableLiveData<>(0L);
    public final LiveData<Long> currentTime = _currentTime;

    long lastPause = SystemClock.elapsedRealtime();
    long timePassed = 0;
    Handler handler;
    public boolean isPaused = false;
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
                _currentTime.postValue((timePassed + SystemClock.elapsedRealtime() - lastPause)/1000);
                handler.postDelayed(this, 100);
            }
        });
    }

    public void reset(){
        timePassed = 0;
        lastPause = SystemClock.elapsedRealtime();
        _currentTime.postValue(0L);
    }
}