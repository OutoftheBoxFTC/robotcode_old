package org.ftc7244.robotcontroller.sensor.accerometer;

import org.ftc7244.robotcontroller.sensor.SensorProvider;

/**
 * A simple provider that requests that the state of the accelerometer if it senses movement. It has
 * two different states including: moving and stopped.
 */
public abstract class AccelerometerProvider extends SensorProvider {

    /**
     * If the accelerometer senses any movement beyond its minimum sensitivity
     *
     * @return the rate of movement of the robot
     */
    public abstract Status getStatus();

    public enum Status {
        MOVING,
        STOPPED
    }
}
