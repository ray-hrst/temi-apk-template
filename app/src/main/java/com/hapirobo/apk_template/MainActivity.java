package com.hapirobo.apk_template;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.robotemi.sdk.Robot;
import com.robotemi.sdk.TtsRequest;
import com.robotemi.sdk.listeners.OnRobotReadyListener;

public class MainActivity extends AppCompatActivity implements
        OnRobotReadyListener {
    private static final String TAG_DEBUG = "TEMPLATE-DEBUG";
    private Robot robot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize robot
        robot = Robot.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        robot.addOnRobotReadyListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        robot.removeOnRobotReadyListener(this);
    }

    @Override
    public void onRobotReady(boolean isReady) {
        if (isReady) {
            robot.toggleNavigationBillboard(true);
            robotTask();
        }
        else {
            Log.d(TAG_DEBUG, "Robot was not ready");
        }
    }

    /**
     * Robot's main task
     * Code in here will be auto-generated
     */
    private void robotTask() {
        // insert code here
    }
}
