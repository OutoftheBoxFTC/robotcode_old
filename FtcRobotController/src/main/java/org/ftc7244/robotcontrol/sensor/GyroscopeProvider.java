package org.ftc7244.robotcontrol.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.qualcomm.robotcore.util.RobotLog;

import java.util.Arrays;

/**
 * The orientation provider that delivers the relative orientation from the {@link Sensor#TYPE_GYROSCOPE
 * Gyroscope}. This sensor does not deliver an absolute orientation (with respect to magnetic north and gravity) but
 * only a relative measurement starting from the point where it started.
 * 
 * @author Alexander Pacha
 * 
 */
public abstract class GyroscopeProvider implements SensorEventListener {

    /**
     * Constant specifying the factor between a Nano-second and a second
     */
    private static final float NS2S = 1.0f / 1000000000.0f;

    /**
     * This is a filter-threshold for discarding Gyroscope measurements that are below a certain level and
     * potentially are only noise and not real motion. Values from the gyroscope are usually between 0 (stop) and
     * 10 (rapid rotation), so 0.1 seems to be a reasonable threshold to filter noise (usually smaller than 0.1) and
     * real motion (usually > 0.1). Note that there is a chance of missing real motion, if the use is turning the
     * device really slowly, so this value has to find a balance between accepting noise (threshold = 0) and missing
     * slow user-action (threshold > 0.5). 0.1 seems to work fine for most applications.
     *
     */
    private static final double EPSILON = 0.1f;

    /**
     * The quaternion that stores the difference that is obtained by the gyroscope.
     * Basically it contains a rotational difference encoded into a quaternion.
     * 
     * To obtain the absolute orientation one must add this into an initial position by
     * multiplying it with another quaternion
     */
    private final Quaternion deltaQuaternion;

    /**
     * The time-stamp being used to record the time when the last gyroscope event occurred.
     */
    private long timestamp;

    /**
     * The quaternion that holds the current rotation
     */
    private final Quaternion currentOrientationQuaternion;

    private SensorManager sensorManager;

    private double x, y, z;
    private double xOffset, zOffset;


    /**
     * Temporary variable to save allocations.
     */
    private Quaternion correctedQuaternion;

    public GyroscopeProvider() {
        currentOrientationQuaternion = new Quaternion();
        deltaQuaternion = new Quaternion();
        correctedQuaternion = new Quaternion();
        x = 0;
        y = 0;
        z = 0;
        xOffset = 0;
        zOffset = 0;
    }

    public void start(SensorManager sensorManager, int samplingPeriod) {
        this.sensorManager = sensorManager;
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), samplingPeriod);
    }

    public void stop() {
        sensorManager.unregisterListener(this);
    }

    public abstract void onUpdate();

    @Override
    public void onSensorChanged(SensorEvent event) {

        // we received a sensor event. it is a good practice to check
        // that we received the proper event
        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {

            // This timestamps delta rotation to be multiplied by the current rotation
            // after computing it from the gyro sample data.
            if (timestamp != 0) {
                final float dT = (event.timestamp - timestamp) * NS2S;
                // Axis of the rotation sample, not normalized yet.
                float axisX = event.values[0];
                float axisY = event.values[1];
                float axisZ = event.values[2];

                // Calculate the angular speed of the sample
                double gyroscopeRotationVelocity = Math.sqrt(axisX * axisX + axisY * axisY + axisZ * axisZ);

                // Normalize the rotation vector if it's big enough to get the axis
                if (gyroscopeRotationVelocity > EPSILON) {
                    axisX /= gyroscopeRotationVelocity;
                    axisY /= gyroscopeRotationVelocity;
                    axisZ /= gyroscopeRotationVelocity;
                }

                // Integrate around this axis with the angular speed by the timestep
                // in order to get a delta rotation from this sample over the timestep
                // We will convert this axis-angle representation of the delta rotation
                // into a quaternion before turning it into the rotation matrix.
                double thetaOverTwo = gyroscopeRotationVelocity * dT / 2.0f;
                double sinThetaOverTwo = Math.sin(thetaOverTwo);
                double cosThetaOverTwo = Math.cos(thetaOverTwo);
                deltaQuaternion.setX((float) (sinThetaOverTwo * axisX));
                deltaQuaternion.setY((float) (sinThetaOverTwo * axisY));
                deltaQuaternion.setZ((float) (sinThetaOverTwo * axisZ));
                deltaQuaternion.setW(-(float) cosThetaOverTwo);

                // Matrix rendering in CubeRenderer does not seem to have this problem.
                    // Move current gyro orientation if gyroscope should be used
                deltaQuaternion.multiplyByQuat(currentOrientationQuaternion, currentOrientationQuaternion);

                correctedQuaternion.set(currentOrientationQuaternion);
                // We inverted getW in the deltaQuaternion, because currentOrientationQuaternion required it.
                // Before converting it back to matrix representation, we need to revert this process
                correctedQuaternion.setW(-correctedQuaternion.getW());


                // Set the rotation matrix as well to have both representations

                float[] matrix = new float[16];
                SensorManager.getRotationMatrixFromVector(matrix, correctedQuaternion.array());
                float[] orientation = new float[3];
                SensorManager.getOrientation(matrix, orientation);
                x = offsetNumber(Math.toDegrees(orientation[0]), xOffset);
                y = Math.toDegrees(orientation[1]);
                z = offsetNumber(Math.toDegrees(orientation[2]), zOffset);
            }

            onUpdate();
            timestamp = event.timestamp;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public double getXOffset() {
        return xOffset;
    }

    public void setXOffset(double xOffset) {
        this.xOffset = xOffset;
    }

    public void setXToZero() {
        setXOffset(this.x);
    }

    public double getZOffset() {
        return zOffset;
    }

    public void setZOffset(double zOffset) {
        this.zOffset = zOffset;
    }

    public void setZToZero() {
        setXOffset(this.z);
    }

    private double offsetNumber(double orientation, double offset) {
        return ((orientation + 540 - offset) % 360) - 180;
    }
}