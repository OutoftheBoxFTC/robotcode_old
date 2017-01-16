package org.ftc7244.robotcontrol.autonomous.drivers;

/**
 * Created by OOTB on 1/15/2017.
 */

public interface GyroscopeDriveControls {
     void drive(final double power, final double inches) throws InterruptedException;

    void rotate(final double degrees) throws InterruptedException;

    void resetOrientation() throws InterruptedException;
}
