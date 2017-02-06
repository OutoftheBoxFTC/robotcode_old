package org.ftc7244.robotcontroller.sensor;

import android.hardware.Sensor;

import com.qualcomm.robotcore.hardware.HardwareMap;

import lombok.Getter;
import lombok.Setter;

/**
 * The orientation provider that delivers the relative orientation from the {@link Sensor#TYPE_GYROSCOPE
 * Gyroscope}. This sensor does not deliver an absolute orientation (with respect to magnetic north and gravity) but
 * only a relative measurement starting from the point where it started.
 *
 * @author Brandon Barker
 */
public abstract class GyroscopeProvider {
    /**
     * The time-stamp being used to record the time when the last gyroscope event occurred.
     */
    private long timestamp;

    private volatile double x, y, z;
    @Getter
    @Setter
    private volatile double xOffset, zOffset;

    public GyroscopeProvider() {
        xOffset = 0;
        zOffset = 0;
        x = 0;
        y = 0;
        z = 0;
        timestamp = 0;
    }

    public abstract void start(HardwareMap map, int msSamplingPeriod);

    public abstract void calibrate();

    public abstract boolean isCalibrated();

    public abstract void stop();

    public double getX() {
        return offsetNumber(x, zOffset);
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return offsetNumber(z, zOffset);
    }

    protected synchronized void setX(double x) {
        this.x = x;
    }

    protected synchronized void setY(double y) {
        this.y = y;
    }

    protected synchronized void setZ(double z) {
        this.z = z;
    }

    public long getTimestamp() {
        return timestamp;
    }

    protected synchronized void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setXToZero() {
        setXOffset(this.x + xOffset);
    }

    public void setZToZero() {
        setZOffset(this.z + zOffset);
    }

    protected double offsetNumber(double orientation, double offset) {
        return ((orientation + 540 - offset) % 360) - 180;
    }
}