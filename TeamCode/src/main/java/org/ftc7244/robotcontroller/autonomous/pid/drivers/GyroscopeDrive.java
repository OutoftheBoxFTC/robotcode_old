package org.ftc7244.robotcontroller.autonomous.pid.drivers;

import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.util.RobotLog;

import org.ftc7244.robotcontroller.Westcoast;
import org.ftc7244.robotcontroller.autonomous.EncoderAutonomous;
import org.ftc7244.robotcontroller.autonomous.pid.PIDController;
import org.ftc7244.robotcontroller.autonomous.pid.PIDDriveControl;
import org.ftc7244.robotcontroller.autonomous.pid.terminators.ConditionalTerminator;
import org.ftc7244.robotcontroller.autonomous.pid.terminators.SensitivityTerminator;
import org.ftc7244.robotcontroller.autonomous.pid.terminators.TerminationMode;
import org.ftc7244.robotcontroller.autonomous.pid.terminators.Terminator;
import org.ftc7244.robotcontroller.autonomous.pid.terminators.TimerTerminator;
import org.ftc7244.robotcontroller.sensor.GyroscopeProvider;

/**
 * Created by OOTB on 1/15/2017.
 */

public class GyroscopeDrive extends PIDDriveControl {

    private static final double LIGHT_TUNING = 0.3;

    private GyroscopeProvider provider;

    public GyroscopeDrive(Westcoast robot, GyroscopeProvider provider, boolean debug) {
        super(new PIDController(-0.02, -0.00003, -3.25, 30, 6, 0.8), robot, debug);
        this.provider = provider;
    }

    @Override
    public double getReading() {
        return this.provider.getZ();
    }

    public void drive(double power, final double inches) throws InterruptedException {
        final double ticks = inches * EncoderAutonomous.COUNTS_PER_INCH;
        EncoderAutonomous.resetMotors(robot.getDriveLeft(), robot.getDriveRight());
        if (inches <= 0) RobotLog.ee("Error", "Invalid distances!");
        final int offset = getEncoderAverage();
        control(0, power, new Terminator() {
            @Override
            public boolean shouldTerminate() {
                return Math.abs(getEncoderAverage() - offset) >= ticks;
            }
        });
    }

    public void driveUntilLine(double power, Sensor mode) throws InterruptedException {
        driveUntilLine(power, mode, 0);
    }

    public void driveUntilLine(double power, Sensor mode, double offsetDistance) throws InterruptedException {
        driveUntilLine(power, mode, offsetDistance, 0, 0);
    }

    public void driveUntilLine(double power, Sensor mode, double offsetDistance, final double minDistance, final double maxDistance) throws InterruptedException {
        EncoderAutonomous.resetMotors(robot.getDriveLeft(), robot.getDriveRight());
        if (offsetDistance <= 0) RobotLog.ee("Error", "Invalid distances!");
        final double ticks = offsetDistance * EncoderAutonomous.COUNTS_PER_INCH,
                maxTicks = maxDistance * EncoderAutonomous.COUNTS_PER_INCH,
                minTicks = minDistance * EncoderAutonomous.COUNTS_PER_INCH;
        final int encoderError = getEncoderAverage();

        control(0, power, new ConditionalTerminator(
                        new Terminator() {
                            @Override
                            public boolean shouldTerminate() {
                                return Math.abs(getEncoderAverage() - encoderError) >= maxTicks && maxTicks > 0;
                            }
                        },
                        new ConditionalTerminator(TerminationMode.AND,
                                new LineTerminator(mode == Sensor.Trailing ? robot.getTrailingLight() : robot.getLeadingLight(), encoderError, ticks),
                                new Terminator() {
                                    @Override
                                    public boolean shouldTerminate() {
                                        return Math.abs(getEncoderAverage() - encoderError) > minTicks || minTicks <= 0;
                                    }
                                }
                        )
                )
        );
    }

    public void rotate(final double degrees) throws InterruptedException {
        final double target = degrees + provider.getZ();
        control(target, 0, new ConditionalTerminator(new SensitivityTerminator(this, degrees, 2, 300), new TimerTerminator(2000)));
        resetOrientation();
    }

    public void resetOrientation() throws InterruptedException {
        provider.setZToZero();
        while (Math.abs(Math.round(provider.getZ())) > 1) Thread.yield();
    }

    private int getEncoderAverage() {
        return (robot.getDriveLeft().getCurrentPosition() + robot.getDriveRight().getCurrentPosition()) / 2;
    }

    public enum Sensor {
        Leading,
        Trailing
    }

    public class LineTerminator extends Terminator {

        private double driveAfterDistance, offset, encoderError;
        private LightSensor sensor;

        public LineTerminator(LightSensor sensor, double encoderError, double driveAfterDistance) {
            this.sensor = sensor;
            this.driveAfterDistance = driveAfterDistance;
            this.offset = 0;
            this.encoderError = encoderError;
        }

        @Override
        public boolean shouldTerminate() {
            if (sensor.getLightDetected() > 0.3) {
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
    }
}
