package org.ftc7244.robotcontroller.sensor.accerometer;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.kauailabs.navx.ftc.AHRS;
import com.kauailabs.navx.ftc.IDataArrivalSubscriber;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.ftc7244.robotcontroller.hardware.Westcoast;

/**
 * Uses the NavX-Micro and depends on the navx to determine if the robot is moved or rotated. The
 * login behind if it is moving or not is outsourced to its arm processor.
 */
public class NavXAccelerometerProvider extends AccelerometerProvider implements IDataArrivalSubscriber {

    @Nullable
    private AHRS navxDevice;
    private boolean moving;
    private Westcoast robot;
    public NavXAccelerometerProvider(Westcoast robot){
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
        navxDevice.zeroYaw();
        navxDevice.registerCallback(this);
    }

    @Override
    public void stop() {
        navxDevice.deregisterCallback(this);
        navxDevice = null;
    }

    @Override
    public void untimestampedDataReceived(long l, Object o) {

    }

    @Override
    public void timestampedDataReceived(long l, long l1, Object o) {
        moving = navxDevice.isMoving();
    }

    @Override
    public void yawReset() {

    }
}
