package com.hapirobo.apk_template;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.robotemi.sdk.BatteryData;
import com.robotemi.sdk.Robot;
import com.robotemi.sdk.TtsRequest;
import com.robotemi.sdk.listeners.OnDetectionStateChangedListener;
import com.robotemi.sdk.listeners.OnGoToLocationStatusChangedListener;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity implements
        OnGoToLocationStatusChangedListener,
        OnDetectionStateChangedListener {

    public static final String LOCATION_HOME_BASE = "home base";
    public static final String LOCATION_RECEPTION = "reception";
    public static final int BATTERY_PERCENTAGE_THRESHOLD_LOW = 20;
    public static final int BATTERY_PERCENTAGE_THRESHOLD_HIGH = 80;

    // battery monitor - periodic timer
    // https://stackoverflow.com/questions/6242268/repeat-a-task-with-a-time-delay/6242292#6242292
    private static final int BATTERY_MONITOR_INTERVAL = 5000; // timer interval [msec]
    private Handler mBatteryMonitorHandler;

    private Robot robot;

    @Override
    // onCreate vs onStart
    // https://stackoverflow.com/questions/6812003/difference-between-oncreate-and-onstart
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize robot
        robot = Robot.getInstance();

        // initialize battery-monitor
        mBatteryMonitorHandler = new Handler();
        startBatteryMonitor();

        // run state machine
        stateMachine();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopBatteryMonitor();
    }

    /**
     * Battery monitor
     * Runs periodically
     * https://stackoverflow.com/questions/6242268/repeat-a-task-with-a-time-delay/6242292#6242292
     */
    Runnable mBatteryMonitor = new Runnable() {
        @Override
        public void run() {
            try {
                // read battery capacity
                BatteryData batteryData = robot.getBatteryData();

                // if battery capacity is low, return to home base
                if (batteryData.getBatteryPercentage() <= BATTERY_PERCENTAGE_THRESHOLD_LOW) {
                    robot.goTo(LOCATION_HOME_BASE);
                }

                // if battery capacity is high, return to reception location
                if (batteryData.getBatteryPercentage() >= BATTERY_PERCENTAGE_THRESHOLD_HIGH) {
                    robot.goTo(LOCATION_RECEPTION);
                }

            } finally {
                // 100% guarantee that this always happens, even if
                // your update method throws an exception
                mBatteryMonitorHandler.postDelayed(mBatteryMonitor, BATTERY_MONITOR_INTERVAL);
            }
        }
    };

    /**
     * Start battery monitor
     */
    void startBatteryMonitor() {
        mBatteryMonitor.run();
    }

    /**
     * Stop battery monitor
     */
    void stopBatteryMonitor() {
        mBatteryMonitorHandler.removeCallbacks(mBatteryMonitor);
    }

    /**
     * Add listeners
     */
    protected void onStart() {
        super.onStart();
        robot.addOnGoToLocationStatusChangedListener(this);
        robot.addOnDetectionStateChangedListener(this);
    }

    /**
     * Remove listeners
     */
    protected void onStop() {
        super.onStop();
        robot.removeOnGoToLocationStatusChangedListener(this);
        robot.removeDetectionStateChangedListener(this);
    }

    @Override
    public void onDetectionStateChanged(int state) {
        Log.d("onDetectionStateChanged", "state= " + state);

        switch(state) {
            case DETECTED: // human body detected
                robot.speak(TtsRequest.create("Hello! Welcome to hapi-robo. How may I help you today?", false));
                robot.beWithMe();
//                robot.constraintBeWith();
                // TODO show user some options
                break;
            case LOST: // target lost
            case IDLE: // 10 seconds have passed since last detection
                robot.goTo(LOCATION_RECEPTION);
                break;
        }
    }

    @Override
    public void onGoToLocationStatusChanged(@NotNull String location, @NotNull String status, int descriptionId, @NotNull String description) {
        Log.d("GoToStatusChanged", "descriptionId=" + descriptionId + ", description=" + description);
        switch (status) {
            case "start":
                // start navigating to the location
                break;

            case "calculating":
                // calculating path to the location
                break;

            case "going":
                // moving to the location
                break;

            case "complete":
                // arrived at the location
                robot.speak(TtsRequest.create("Arrived at " + location, false));
                break;

            case "abort":
                // aborted navigation
                robot.speak(TtsRequest.create("Go-To Aborted", false));
                Log.d("GoToLocation", "descriptionId=" + descriptionId + "| description=" + description);
                break;
        }
    }

    /**
     * State machine
     */
    private void stateMachine() {
        // TODO replace code

        // wait until user is detected

        robot.goTo("1");
        // wait until it has arrived at the location
        // say/show something

        robot.goTo(LOCATION_RECEPTION);
    }

    // TODO if charging and battery is fully charged, go to LOCATION_RECEPTION and wait

}
