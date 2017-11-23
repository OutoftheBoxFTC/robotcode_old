package org.ftc7244.robotcontroller.sensor.accerometer;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.qualcomm.hardware.kauailabs.NavxMicroNavigationSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.ftc7244.robotcontroller.sensor.gyroscope.NavxRobot;

/**
 * Uses the NavX-Micro and depends on the navx to determine if the robot is moved or rotated. The
 * login behind if it is moving or not is outsourced to its arm processor.
 */
public class NavXAccelerometerProvider extends AccelerometerProvider {

    @Nullable
    private NavxMicroNavigationSensor navxDevice;
    private boolean moving;
    private NavxRobot robot;

    public NavXAccelerometerProvider(NavxRobot robot) {
        this.robot = robot;
    }

    @NonNull
    @Override
    public Status getStatus() {
        return moving ? Status.MOVING : Status.STOPPED;
    }

    @Override
    public void start(HardwareMap map) {
        moving = false;
        navxDevice = robot.getNavX();
    }

    @Override
    public void stop() {
        navxDevice.close();
        navxDevice = null;
    }
}
