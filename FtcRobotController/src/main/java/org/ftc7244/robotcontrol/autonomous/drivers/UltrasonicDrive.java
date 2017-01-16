package org.ftc7244.robotcontrol.autonomous.drivers;

import org.ftc7244.robotcontrol.Westcoast;
import org.ftc7244.robotcontrol.autonomous.pid.PIDController;
import org.ftc7244.robotcontrol.autonomous.pid.PIDDriveControl;
import org.ftc7244.robotcontrol.autonomous.pid.SensitivityTerminator;

/**
 * Created by OOTB on 1/15/2017.
 */

public class UltrasonicDrive extends PIDDriveControl implements UltrasonicDriveControls {
    public UltrasonicDrive(Westcoast robot, boolean debug) {
        super(new PIDController(1, 0, 0, 30), robot, debug);
    }

    @Override
    public double getReading() {
        return robot.getLeadingUltrasonic().getUltrasonicLevel() - robot.getTrailingUltrasonic().getUltrasonicLevel();
    }

    public void parallelize() throws InterruptedException {
        control(0, new SensitivityTerminator(this, 0, 1, 500));
    }

    public void driveParallel(double power) throws InterruptedException {
       // FIXME: 1/15/2017
        control(0, new Handler() {
            long end = System.currentTimeMillis() + 3000;

            @Override
            protected boolean shouldTerminate() {
                return System.currentTimeMillis() > end;
            }
        });
    }
}
