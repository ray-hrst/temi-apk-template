package com.hapirobo.apk_template;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.robotemi.sdk.Robot;
import com.robotemi.sdk.TtsRequest;

public class MainActivity extends AppCompatActivity {
    private Robot robot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize robot
        robot = Robot.getInstance();

        // run task
        robotTask();
    }

    private void robotTask() {
        // insert code here
    }
}
