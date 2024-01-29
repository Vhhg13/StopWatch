package com.example.handlerstopwatch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(action != null && action.equals("pause_unpause_action")){
            Toast.makeText(context, action, Toast.LENGTH_SHORT).show();
        }
    }
}
