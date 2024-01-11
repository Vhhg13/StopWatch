package com.example.handlerstopwatch;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainActivityViewModel extends ViewModel implements StopWatchContainer {
    private final MutableLiveData<String> _stopWatchState = new MutableLiveData<String>("0");
    private final MutableLiveData<Boolean> _isCounting = new MutableLiveData<>(true);
    public LiveData<String> stopWatchState = _stopWatchState;
    public LiveData<Boolean> isCounting = _isCounting;
    MyThread thread = new MyThread(this);

    MainActivityViewModel(){
        thread.start();
    }
    public void togglePause(){
        if(_isCounting.getValue()) thread.pause();
        else thread.unpause();
        _isCounting.setValue(!_isCounting.getValue());
    }

    public void reset(){
        thread.reset();
    }

    @Override
    public void updateStopWatch(String str) {
        _stopWatchState.setValue(str);
    }
}
