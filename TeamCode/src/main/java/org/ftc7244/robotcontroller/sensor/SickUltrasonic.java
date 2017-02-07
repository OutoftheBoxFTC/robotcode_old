package org.ftc7244.robotcontroller.sensor;

import android.support.annotation.NonNull;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

/**
 * Converts analog to an understandable output for the SICK Sensor specified. It also acts as a good
 * way of providing debug and general information.
 */
public class SickUltrasonic implements UltrasonicSensor {

    private AnalogInput input;
    private Mode mode;

    /**
     * Automatically use ${@link Mode#INCHES} since this is AMERICA!
     *
     * @param input which sensor to use
     */
    public SickUltrasonic(AnalogInput input) {
        this(input, Mode.INCHES);
    }

    /**
     * Setup the sick ultrasonic using an AnalogInput and handle its input as distance.
     *
     * @param input which sensor to use
     * @param mode the distance units in ${@link Mode#INCHES} or ${@link Mode#CENTIMETERS}
     */
    public SickUltrasonic(AnalogInput input, Mode mode) {
        this.input = input;
        this.mode = mode;
    }

    /**
     * Based off the {@link Mode} the distance will be returned from the sensor in the units specified.
     * However, if the sensor is out of its range: 9.84252 inches or 25 centimeters the code will return
     * the maximum value possible.
     *
     * @return distance in units of {@link Mode}
     */
    @Override
    public double getUltrasonicLevel() {
        return mode.multiplyer * input.getVoltage();
    }

    @NonNull
    @Override
    public String status() {
        return mode.name() + "[" + (getUltrasonicLevel() == mode.cap ? "WAITING" : "DETECTING") + "]";
    }
    @NonNull
    @Override
    public Manufacturer getManufacturer() {
        return Manufacturer.Other;
    }

    @NonNull
    @Override
    public String getDeviceName() {
        return "SENSICK UM18-2 Pro";
    }

    @Override
    public String getConnectionInfo() {
        return input.getConnectionInfo();
    }

    @Override
    public int getVersion() {
        return 217;
    }

    @Override
    public void resetDeviceConfigurationForOpMode() {
        input.resetDeviceConfigurationForOpMode();
    }

    @Override
    public void close() {
        input.close();
    }

    /**
     * The units to be used in the robot
     *
     * @return the mode either in ${@link Mode#INCHES} or ${@link Mode#CENTIMETERS}
     */
    public Mode getMode() {
        return mode;
    }

    /**
     * Change the mode of the robot
     *
     * @param mode the new mode either in ${@link Mode#INCHES} or ${@link Mode#CENTIMETERS}
     */
    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public enum Mode {
        INCHES(9.84252),
        CENTIMETERS(25);

        private final double multiplyer, cap;

        Mode(double distance) {
            this.cap = distance;
            this.multiplyer = 2 * (distance / 10);
        }
    }
}
