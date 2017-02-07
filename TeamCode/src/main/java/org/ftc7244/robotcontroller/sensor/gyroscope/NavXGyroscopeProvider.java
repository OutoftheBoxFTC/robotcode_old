package org.ftc7244.robotcontroller.sensor.gyroscope;

import com.kauailabs.navx.ftc.AHRS;
import com.kauailabs.navx.ftc.IDataArrivalSubscriber;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.ftc7244.robotcontroller.Westcoast;

/**
 * Utilizes the NavX-Micro to get an orientatio and prevents code from executing until it
 * has been fully calibrated.
 */
public class NavXGyroscopeProvider extends GyroscopeProvider implements IDataArrivalSubscriber {


    private AHRS navxDevice;

    @Override
    public void start(HardwareMap map) {
        navxDevice = Westcoast.getNavX(map);
        navxDevice.zeroYaw();
        navxDevice.registerCallback(this);
        calibrate();
    }

    @Override
    public void calibrate() {
        navxDevice.zeroYaw();
    }

    @Override
    public boolean isCalibrated() {
        return !navxDevice.isCalibrating();
    }

    @Override
    public void stop() {
        navxDevice.close();
        navxDevice = null;
    }

    @Override
    public void untimestampedDataReceived(long l, Object o) {}

    @Override
    public void timestampedDataReceived(long systemTimestamp, long sensorTimestamp, Object o) {
        setX(navxDevice.getRoll());
        setY(navxDevice.getPitch());
        setZ(navxDevice.getYaw());
        setTimestamp(systemTimestamp);
    }

    @Override
    public void yawReset() {

    }
}
