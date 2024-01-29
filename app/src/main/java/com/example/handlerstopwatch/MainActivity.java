package com.example.handlerstopwatch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModelProvider;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button button = findViewById(R.id.stopButton);
        TextView time = findViewById(R.id.time);
        Button resetButton = findViewById(R.id.resetButton);

//        MainActivityViewModel viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

//        viewModel.isCounting.observe(this, bool -> {
//            if (bool) button.setText(R.string.stop);
//            else button.setText(R.string.go);
//        });

//        button.setOnClickListener(view -> viewModel.togglePause());
//        resetButton.setOnClickListener(view -> viewModel.reset());
        final StopWatchService[] service = new StopWatchService[1];
        ServiceConnection connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
//                viewModel.stopWatchState = Transformations.map(((StopWatchService.StopWatchServiceBinder) iBinder).getService().timeElapsed, String::valueOf);
//                viewModel.stopWatchState.observe(MainActivity.this, time::setText);
//                ((App)getApplicationContext()).service = ((StopWatchService.StopWatchServiceBinder) iBinder).getService();
                service[0] = ((StopWatchService.StopWatchServiceBinder) iBinder).getService();
                service[0].timeElapsed.observe(MainActivity.this, val -> {
                    time.setText(String.valueOf(val));
                });
                button.setOnClickListener(view -> service[0].togglePause());
                resetButton.setOnClickListener(view -> service[0].reset());
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {}
        };
        bindService(new Intent(this, StopWatchService.class), connection, Context.BIND_AUTO_CREATE);
    }
}