package org.ftc7244.robotcontroller.programs.autonomous.VelocityVortexAutonamous;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.ftc7244.robotcontroller.autonomous.bases.VelocityVortexPIDAutonomous;
@Deprecated
@Disabled
public class CornerRed extends VelocityVortexPIDAutonomous {

    @Override
    public void run() throws InterruptedException {
        sleep(5000);
        //Move off the wall and head in the direction of the line
        gyroscope.drive(-0.5, 5);
        gyroscope.rotate(-45);
        //Drive to the middle of the field
        gyroscope.drive(-0.5, 8);
        //rotate so that the shooter is facing the vortex
        gyroscope.rotate(-90);
        //shoot two balls
        sleep(1000);
        robot.shootLoop(2, 1500);
        sleep(5000);
        robot.shoot(0);
    }
}
