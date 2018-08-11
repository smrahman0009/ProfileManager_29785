package com.example.musfiq.profilemanager_29785;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btnStart,btnStop;
    Intent in;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStart = findViewById(R.id.button);
        btnStop = findViewById(R.id.button2);

        in = new Intent(this,SensorsService.class);
    }

    public void startService(View v){
        startService(in);
    }

    public void stopService(View v){
        stopService(in);
    }

}
