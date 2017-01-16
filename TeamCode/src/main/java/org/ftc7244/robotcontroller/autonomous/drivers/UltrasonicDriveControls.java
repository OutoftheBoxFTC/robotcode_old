package org.ftc7244.robotcontroller.autonomous.drivers;

/**
 * Created by OOTB on 1/15/2017.
 */

public interface UltrasonicDriveControls {

    void parallelize() throws InterruptedException;

    void driveParallel(double power) throws InterruptedException;
}
