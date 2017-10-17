package org.ftc7244.robotcontroller.programs.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.ftc7244.robotcontroller.autonomous.bases.VelocityVortexPIDAutonomous;
@Deprecated
@Disabled
public class CornerBlue extends VelocityVortexPIDAutonomous {

    @Override
    public void run() throws InterruptedException {
        sleep(5000);
        //Move off the wall and head in the direction of the line
        gyroscope.drive(.5, 5);
        gyroscope.rotate(45);
        //Drive to the middle of the field
        gyroscope.drive(.5, 8);
        //Orient for shooting balls
        gyroscope.rotate(100);
        //shoot two balls
        robot.shootLoop(2, 1500);
        sleep(5000);
        robot.shoot(0);
    }
}
