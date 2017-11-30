package org.ftc7244.robotcontroller.sensor.gyroscope;

import android.hardware.Sensor;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.ftc7244.robotcontroller.sensor.SensorProvider;

/**
 * The orientation provider that delivers the relative orientation from the {@link Sensor#TYPE_GYROSCOPE
 * Gyroscope}. This sensor does not deliver an absolute orientation (with respect to magnetic north and gravity) but
 * only a relative measurement starting from the point where it started.
 */
public abstract class GyroscopeProvider extends SensorProvider {
    /**
     * The time-stamp being used to record the time when the last gyroscope event occurred.
     */
    private long timestamp;

    private volatile double x, y, z;

    /**
     * Will offset the value of the gyroscope by the value specified which can be used as a way of
     * zeroing the offset and changing the heading.
     */
    private volatile double xOffset, zOffset;

    public GyroscopeProvider() {
        xOffset = 0;
        zOffset = 0;
        x = 0;
        y = 0;
        z = 0;
        timestamp = 0;
    }


    /**
     * Begin running the sensor and based off the sampling period update the values. It will
     * automatically execute ${@link #calibrate()}
     *
     * @param map the hardware map to obtain access of the sensor
     */
    public abstract void start(HardwareMap map);

    /**
     * Tell the robot to begin calibrating or re-calibrate the sensor on the robot
     */
    public abstract void calibrate();

    /**
     * A status that will notify the user when the device is calibrated
     *
     * @return whether or not it has finished calibration
     */
    public abstract boolean isCalibrated();

    /**
     * Shutoff and reset the robots gyroscope
     */
    public abstract void stop();

    /**
     * Get the current value of the X with the offset specified in ${@link #setXOffset(double)}
     * By default the values range from -180 to 180 degrees
     *
     * @return value of in degrees
     */
    public double getX() {
        return offsetNumber(x, zOffset);
    }

    protected synchronized void setX(double x) {
        this.x = x;
    }

    /**
     * The output of this is from -90 to 90 degrees but this cannot be offsetted because of its
     * limitation of range
     *
     * @return value in degrees
     */
    public double getY() {
        return y;
    }

    protected synchronized void setY(double y) {
        this.y = y;
    }

    /**
     * Get the current value of the Y with the offset specified in ${@link #setZOffset(double)}
     *
     * @return value of in degrees
     */
    public double getZ() {
        return offsetNumber(z, zOffset);
    }

    protected synchronized void setZ(double z) {
        this.z = z;
    }

    /**
     * The last time the robot was updated with new values.
     *
     * @return timestamp in milliseconds
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Set the timestamp in milliseconds
     *
     * @param timestamp new timestamp in milliseconds
     */
    protected synchronized void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Depends on the ${@link #setXOffset(double)} to set the current gyroscopic value to equal zero
     */
    public void setXToZero() {
        setXOffset(this.x + xOffset);
    }

    /**
     * Depends on the ${@link #setZOffset(double)} (double)} to set the current gyroscopic value to equal zero
     */
    public void setZToZero() {
        setZOffset(getZ() + getZOffset());
    }

    protected double offsetNumber(double orientation, double offset) {
        return ((orientation + 540 - offset) % 360) - 180;
    }

    public double getXOffset() {
        return this.xOffset;
    }

    public void setXOffset(double xOffset) {
        this.xOffset = xOffset;
    }

    public double getZOffset() {
        return this.zOffset;
    }

    public void setZOffset(double zOffset) {
        this.zOffset = zOffset;
    }
}