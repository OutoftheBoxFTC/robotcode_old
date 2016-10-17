package org.ftc7244.robotcontrol.sensors;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import org.ftc7244.robotcontrol.sensors.math.MatrixF4x4;
import org.ftc7244.robotcontrol.sensors.math.Quaternion;

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
     * The quaternion that stores the difference that is obtained by the gyroscope.
     * Basically it contains a rotational difference encoded into a quaternion.
     *
     * To obtain the absolute orientation one must add this into an initial position by
     * multiplying it with another quaternion
     */
    private final Quaternion deltaQuaternion = new Quaternion();

    /**
     * The quaternion that holds the current rotation
     */
    protected final Quaternion currentOrientationQuaternion;

    /**
     * The matrix that holds the current rotation
     */
    protected final MatrixF4x4 currentOrientationRotationMatrix;

    /**
     * The time-stamp being used to record the time when the last gyroscope event occurred.
     */
    private long timestamp;

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
     * Temporary variable to save allocations.
     */
    private Quaternion correctedQuaternion = new Quaternion();

    /**
     * Sensor Manager used in the start
     */
    private SensorManager sensorManager;

    /**
     * Initialises a new GyroscopeProvider
     *
     */
    public GyroscopeProvider() {
        this.currentOrientationQuaternion = new Quaternion();
        this.currentOrientationRotationMatrix = new MatrixF4x4();
    }

    public void start(SensorManager manager, int samplingPeriod) {
        System.out.println(manager + ":" + samplingPeriod);
        this.sensorManager = manager;
        sensorManager.registerListener(this, manager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), samplingPeriod);
    }

    public void stop() {
        if (sensorManager == null) return;
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        // we received a sensor event. it is a good practice to check
        // that we received the proper event
        if (event.sensor.getType() != Sensor.TYPE_GYROSCOPE) {
            return;
        }

        // This timestamps delta rotation to be multiplied by the current rotation
        // after computing it from the gyro sample data.
        if (timestamp != 0) {
            final float dT = (event.timestamp - timestamp) * NS2S;
            // Axis of the rotation sample, not normalized yet.
            float axisX = event.values[0];
            float axisY = event.values[1];
            float axisZ = event.values[2];

            double gyroscopeRotationVelocity = Math.sqrt(axisX * axisX + axisY * axisY + axisZ * axisZ);
            // Calculate the angular speed of the sample

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
            deltaQuaternion.multiplyByQuat(currentOrientationQuaternion, currentOrientationQuaternion);

            correctedQuaternion.set(currentOrientationQuaternion);
            // We inverted w in the deltaQuaternion, because currentOrientationQuaternion required it.
            // Before converting it back to matrix representation, we need to revert this process
            correctedQuaternion.w(-correctedQuaternion.w());

            // Set the rotation matrix as well to have both representations
            SensorManager.getRotationMatrixFromVector(currentOrientationRotationMatrix.matrix,
                    correctedQuaternion.array());

            onUpdate();
        }
        timestamp = event.timestamp;
    }

    public abstract void onUpdate();

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //Do nothing
    }
}