package org.ftc7244.robotcontrol.autonomous.drivers;

import com.qualcomm.robotcore.util.RobotLog;

import org.ftc7244.robotcontrol.Westcoast;
import org.ftc7244.robotcontrol.autonomous.EncoderAutonomous;
import org.ftc7244.robotcontrol.autonomous.pid.PIDController;
import org.ftc7244.robotcontrol.autonomous.pid.PIDDriveControl;
import org.ftc7244.robotcontrol.autonomous.pid.SensitivityTerminator;
import org.ftc7244.robotcontrol.sensor.GyroscopeProvider;

/**
 * Created by OOTB on 1/15/2017.
 */

public class GyroscopeDrive extends PIDDriveControl implements GyroscopeDriveControls {

    private GyroscopeProvider provider;

    public GyroscopeDrive(Westcoast robot, GyroscopeProvider provider, boolean debug) {
        super(new PIDController(-0.02, -0.00003, -3.25, 30, 15, .25), robot, debug);
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
            protected double offset() {
                return power;
            }

            @Override
            public boolean shouldTerminate() {
                return Math.abs(getEncoderAverage() - offset) >= ticks;
            }
        });
    }

    public void rotate(final double degrees) throws InterruptedException {
        final double target = degrees + provider.getZ();
        control(target, new SensitivityTerminator(this, degrees, .5, 100));
        resetOrientation();
    }

    public void resetOrientation() throws InterruptedException {
        provider.setZToZero();
        while (Math.abs(Math.round(provider.getZ())) > 1) Thread.yield();
    }

    private int getEncoderAverage() {
        return (robot.getDriveLeft().getCurrentPosition() + robot.getDriveRight().getCurrentPosition()) / 2;
    }
}
