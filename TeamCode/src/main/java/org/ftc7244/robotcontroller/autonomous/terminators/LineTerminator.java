package org.ftc7244.robotcontroller.autonomous.terminators;

import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.util.RobotLog;

import org.ftc7244.robotcontroller.Debug;
import org.ftc7244.robotcontroller.Westcoast;

/**
 * Controls how far after the robot should drive if it passes the line and when to trigger that.
 * It also respects if the request to terminate was ignored and continues to only drive until
 * a specific set of parameters were fulfilled;
 */
@Deprecated
public class LineTerminator extends Terminator {

    /**
     * Which light sensor to use based off the the user input. This only allows the code to be more
     * streamlined in choice of sensor
     */
    public enum Sensor {
        Leading(0.3),
        Trailing(0.3);

        protected double white;

        Sensor(double white) {
            this.white = white;
        }
    }

    private double driveAfterDistance, offset, encoderError;
    private LightSensor sensor;
    private double white;
    private Westcoast robot;

    public LineTerminator(Sensor mode, Westcoast robot, double encoderError, double driveAfterDistance) {
        this.sensor = mode == Sensor.Trailing ? robot.getTrailingLight() : robot.getLeadingLight();
        this.driveAfterDistance = driveAfterDistance;
        this.white = mode.white;
        this.offset = 0;
        this.encoderError = encoderError;
        this.robot = robot;
    }

    @Override
    public boolean shouldTerminate() {
        if (Debug.STATUS) RobotLog.ii("Light", sensor.getLightDetected() + "");
        if (sensor.getLightDetected() > white) {
            offset = getEncoderAverage();
        } else sensor.enableLed(true);

        return status();
    }

    private boolean status() {
        return Math.abs(getEncoderAverage() - encoderError - offset) >= driveAfterDistance && offset != 0;
    }

    @Override
    public void terminated(boolean status) {
        if (!status && status()) offset = 0;
        if (status) sensor.enableLed(false);

    }
    private int getEncoderAverage() {
        return (robot.getDriveLeft().getCurrentPosition() + robot.getDriveRight().getCurrentPosition()) / 2;
    }

}