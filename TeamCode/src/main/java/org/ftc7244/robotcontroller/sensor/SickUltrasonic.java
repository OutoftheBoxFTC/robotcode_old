package org.ftc7244.robotcontroller.sensor;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

/**
 * Created by OOTB on 1/25/2017.
 */

public class SickUltrasonic implements UltrasonicSensor {

    private AnalogInput input;
    private Mode mode;

    public SickUltrasonic(AnalogInput input) {
        this(input, Mode.INCHES);
    }

    public SickUltrasonic(AnalogInput input, Mode mode) {
        this.input = input;
        this.mode = mode;
    }

    @Override
    public double getUltrasonicLevel() {
        return mode.multiplyer * input.getVoltage();
    }

    @Override
    public String status() {
        return mode.name() + "[" + (getUltrasonicLevel() == mode.cap ? "WAITING" : "DETECTING") + "]";
    }

    @Override
    public Manufacturer getManufacturer() {
        return Manufacturer.Other;
    }

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

    public Mode getMode() {
        return mode;
    }

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
