package org.ftc7244.robotcontroller.sensor;

import com.kauailabs.navx.ftc.AHRS;
import com.kauailabs.navx.ftc.IDataArrivalSubscriber;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cDevice;

/**
 * Created by brandon on 2/6/17.
 */

public class NavXGyroscopeProvider extends GyroscopeProvider implements IDataArrivalSubscriber {

    private static final byte NAVX_DEVICE_UPDATE_RATE_HZ = 50;

    private AHRS navxDevice;

    @Override
    public void start(HardwareMap map, int msSamplingPeriod) {
        final I2cDevice navx = map.i2cDevice.get("navx");
        navxDevice = AHRS.getInstance((DeviceInterfaceModule) navx.getController(), navx.getPort(), AHRS.DeviceDataType.kProcessedData, NAVX_DEVICE_UPDATE_RATE_HZ);
        navxDevice.zeroYaw();
        navxDevice.registerCallback(this);
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
    }

    @Override
    public void untimestampedDataReceived(long l, Object o) {

    }

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
