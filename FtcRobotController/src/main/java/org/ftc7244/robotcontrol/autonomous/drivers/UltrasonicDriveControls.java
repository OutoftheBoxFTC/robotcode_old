package org.ftc7244.robotcontrol.autonomous.drivers;

import org.ftc7244.robotcontrol.autonomous.pid.SensitivityTerminator;

/**
 * Created by OOTB on 1/15/2017.
 */

public interface UltrasonicDriveControls {

    void parallelize() throws InterruptedException;

    void driveParallel(double power) throws InterruptedException;
}
