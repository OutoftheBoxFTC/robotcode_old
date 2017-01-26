package org.ftc7244.robotcontroller.autonomous.drivers;

import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.util.RobotLog;

import org.ftc7244.robotcontroller.Westcoast;
import org.ftc7244.robotcontroller.autonomous.EncoderAutonomous;
import org.ftc7244.robotcontroller.autonomous.pid.Handler;
import org.ftc7244.robotcontroller.autonomous.pid.PIDController;
import org.ftc7244.robotcontroller.autonomous.pid.PIDDriveControl;
import org.ftc7244.robotcontroller.autonomous.pid.SensitivityTerminator;
import org.ftc7244.robotcontroller.sensor.GyroscopeProvider;

/**
 * Created by OOTB on 1/15/2017.
 */

public class GyroscopeDrive extends PIDDriveControl implements GyroscopeDriveControls {

    private GyroscopeProvider provider;

    public GyroscopeDrive(Westcoast robot, GyroscopeProvider provider, boolean debug) {
        //        super(new PIDController(-0.02, , -3.25, 30, 15, .25), robot, debug);
        super(new PIDController(-0.02, -0.00003, -3.25, 30, 6, 0.8), robot, debug);
        this.provider = provider;
    }

    @Override
    public double getReading() {
        return this.provider.getZ();
    }

    public void drive(final double power, final double inches) throws InterruptedException {
        final double ticks = inches * EncoderAutonomous.COUNTS_PER_INCH;
        EncoderAutonomous.resetMotors(robot.getDriveLeft(), robot.getDriveRight());
        if (inches <= 0) RobotLog.ee("Error", "Invalid distances!");
        final int offset = getEncoderAverage();
        control(0, new Handler() {
            @Override
            public double offset() {
                return power;
            }

            @Override
            public boolean shouldTerminate() {
                return Math.abs(getEncoderAverage() - offset) >= ticks;
            }
        });
    }

    public void driveUntilLine(final double power, Sensor mode, double distance) throws InterruptedException {
        EncoderAutonomous.resetMotors(robot.getDriveLeft(), robot.getDriveRight());
        if (distance <= 0) RobotLog.ee("Error", "Invalid distances!");
        final double ticks = distance * EncoderAutonomous.COUNTS_PER_INCH;
        final LightSensor sensor = mode == Sensor.Trailing ? robot.getTrailingLight() : robot.getLeadingLight();
        final int encoderError = getEncoderAverage();
        sensor.enableLed(true);

        control(0, new Handler() {
            double offset = 0;

            @Override
            public double offset() {
                return power;
            }

            @Override
            public boolean shouldTerminate() {
                if (sensor.getLightDetected() > .3) offset = getEncoderAverage();
                if (offset != 0) sensor.enableLed(false);
                RobotLog.ii("Color", String.valueOf(sensor.getLightDetected()));
                return Math.abs(getEncoderAverage() - encoderError - offset) >= ticks && offset != 0;
            }
        });
    }

    public void rotate(final double degrees) throws InterruptedException {
        final double target = degrees + provider.getZ();
        control(target, new SensitivityTerminator(this, degrees, 2, 300, 2000));
        resetOrientation();
    }

    public void resetOrientation() throws InterruptedException {
        provider.setZToZero();
        while (Math.abs(Math.round(provider.getZ())) > 1) Thread.yield();
    }

    private int getEncoderAverage() {
        return (robot.getDriveLeft().getCurrentPosition() + robot.getDriveRight().getCurrentPosition()) / 2;
    }

    public static enum Sensor {
        Leading,
        Trailing
    }
}
