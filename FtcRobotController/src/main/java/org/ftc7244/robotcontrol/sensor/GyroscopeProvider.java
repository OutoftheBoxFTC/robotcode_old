package org.ftc7244.robotcontrol.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * The orientation provider that delivers the relative orientation from the {@link Sensor#TYPE_GYROSCOPE
 * Gyroscope}. This sensor does not deliver an absolute orientation (with respect to magnetic north and gravity) but
 * only a relative measurement starting from the point where it started.
 *
 * @author Alexander Pacha
 */
public abstract class GyroscopeProvider implements SensorEventListener {

    private static final double EPSILON = 0.1f;

    private static final float NS2S = 1.0f / 1000000000.0f;
    private final float[] deltaQuaternion;
    private float[] currentQuaternion;
    private float timestamp;

    private SensorManager sensorManager;

    /**
     * Initialises a new GyroscopeProvider
     */
    public GyroscopeProvider() {
        //Initialize the sensor readings
        this.deltaQuaternion = new float[4];
        this.currentQuaternion = new float[4];
    }

    public void start(SensorManager sensorManager, int samplingPeriod) {
        this.sensorManager = sensorManager;
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), samplingPeriod);
    }

    public void stop() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // This timestep's delta rotation to be multiplied by the current rotation
        // after computing it from the gyro sample data.
        if (timestamp != 0) {
            final float dT = (event.timestamp - timestamp) * NS2S;
            // Axis of the rotation sample, not normalized yet.
            float axisX = event.values[0];
            float axisY = event.values[1];
            float axisZ = event.values[2];

            // Calculate the angular speed of the sample
            float omega = (float) Math.sqrt(axisX * axisX + axisY * axisY + axisZ * axisZ);
            // Normalize the rotation vector if it's big enough to get the axis
            if (omega > EPSILON) {
                axisX /= omega;
                axisY /= omega;
                axisZ /= omega;
            }

            // Integrate around this axis with the angular speed by the timestep
            // in order to get a delta rotation from this sample over the timestep
            // We will convert this axis-angle representation of the delta rotation
            // into a quaternion before turning it into the rotation matrix.
            float thetaOverTwo = omega * dT / 2.0f;
            float sinThetaOverTwo = (float) Math.sin(thetaOverTwo);
            float cosThetaOverTwo = (float) Math.cos(thetaOverTwo);
            deltaQuaternion[0] = sinThetaOverTwo * axisX;
            deltaQuaternion[1] = sinThetaOverTwo * axisY;
            deltaQuaternion[2] = sinThetaOverTwo * axisZ;
            deltaQuaternion[3] = -cosThetaOverTwo;
        }
        timestamp = event.timestamp;
        float[] deltaRotationMatrix = new float[9];
        currentQuaternion = multiplyQuaternion(deltaQuaternion, currentQuaternion);
        SensorManager.getRotationMatrixFromVector(deltaRotationMatrix, deltaQuaternion);

        //https://bitbucket.org/apacha/sensor-fusion-demo/src/468b322635d087ab2ba6865a9c4dfb775b0e69ff/app/src/main/java/org/hitlabnz/sensor_fusion_demo/orientationProvider/CalibratedGyroscopeProvider.java?at=master&fileviewer=file-view-default
        onUpdate();
    }

    public abstract void onUpdate();

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //Do nothing
    }

    private float[] multiplyQuaternion(float[] a, float[] b) {
        float[] results = new float[3];

        results[3] = (a[3] * b[3] - a[0] * b[0] - a[1] * b[1] - a[2] * b[2]); //w = w1w2 - x1x2 - y1y2 - z1z2
        results[0] = (a[3] * b[0] + a[0] * b[3] + a[1] * b[2] - a[2] * b[1]); //x = w1x2 + x1w2 + y1z2 - z1y2
        results[1] = (a[3] * b[1] + a[1] * b[3] + a[2] * b[0] - a[0] * b[2]); //y = w1y2 + y1w2 + z1x2 - x1z2
        results[2] = (a[3] * b[2] + a[2] * b[3] + a[0] * b[1] - a[1] * b[0]); //z = w1z2 + z1w2 + x1y2 - y1x2

        return results;
    }
}