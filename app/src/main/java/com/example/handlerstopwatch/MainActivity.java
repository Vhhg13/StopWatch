package com.example.handlerstopwatch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.stopButton);
        TextView time = findViewById(R.id.time);
        Button resetButton = findViewById(R.id.resetButton);

        MainActivityViewModel viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        viewModel.stopWatchState.observe(this, time::setText);
        viewModel.isCounting.observe(this, bool -> {
            if(bool) button.setText(R.string.stop);
            else button.setText(R.string.go);
        });

        button.setOnClickListener(view -> viewModel.togglePause());
        resetButton.setOnClickListener(view -> viewModel.reset());
    }
}