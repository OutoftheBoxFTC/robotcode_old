package org.ftc7244.robotcontroller.sensor.gyroscope;

import android.support.annotation.Nullable;

import com.kauailabs.navx.ftc.AHRS;
import com.kauailabs.navx.ftc.IDataArrivalSubscriber;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.ftc7244.robotcontroller.hardware.RelicRecoveryWestcoast;
import org.ftc7244.robotcontroller.hardware.VelocityVortexWestcoast;

/**
 * Utilizes the NavX-Micro to get an orientatio and prevents code from executing until it
 * has been fully calibrated.
 */
public class NavXGyroscopeProvider extends GyroscopeProvider implements IDataArrivalSubscriber {
    public static final byte NAVX_DEVICE_UPDATE_RATE_HZ = (byte) 100;
    @Nullable
    private AHRS navxDevice;
    private boolean calibrating;
    private RelicRecoveryWestcoast robot;
    public NavXGyroscopeProvider(RelicRecoveryWestcoast robot){
        this.robot = robot;
    }

    @Override
    public void start(HardwareMap map) {
        navxDevice = robot.getNavX();
        navxDevice.zeroYaw();
        navxDevice.registerCallback(this);
        calibrating = false;
    }

    @Override
    public void calibrate() {
        navxDevice.zeroYaw();
    }

    @Override
    public boolean isCalibrated() {
        return !calibrating;
    }

    @Override
    public void stop() {
        navxDevice.close();
        navxDevice = null;
    }

    @Override
    public void untimestampedDataReceived(long l, Object o) {
    }

    @Override
    public void timestampedDataReceived(long systemTimestamp, long sensorTimestamp, Object o) {
        setX(navxDevice.getRoll() - 180);
        setY(navxDevice.getPitch() - 180);
        setZ(navxDevice.getYaw() - 180);
        setTimestamp(systemTimestamp);
        calibrating = navxDevice.isCalibrating();

    }

    @Override
    public void yawReset() {

    }
}
