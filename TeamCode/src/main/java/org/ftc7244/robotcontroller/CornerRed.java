package org.ftc7244.robotcontroller;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.ftc7244.robotcontroller.autonomous.PIDAutonomous;

/**
 * Created by OOTB on 11/11/2016.
 */

@Autonomous(name = "Corner Red")
public class CornerRed extends PIDAutonomous {

    @Override
    public void run() throws InterruptedException {
        /*sleep(5000);
        gyroscope.drive(.8, -10, 0);
        gyroscope.drive(1, -24, -24);
        gyroscope.rotate(1, 35, Direction.RIGHT);
        robot.shootLoop(3, 1000);
        gyroscope.rotate(.8, 35, Direction.LEFT);
        gyroscope.drive(1, -15, -15);
        gyroscope.rotate(.8, 40, Direction.LEFT);
        gyroscope.drive(1, -10, -10);*/

        gyroscope.drive(-0.5, 5);
        gyroscope.rotate(45);
        gyroscope.drive(-0.5, 5);
        gyroscope.rotate(77);
        sleep(1000);
        robot.shootLoop(2, 1500);
        gyroscope.rotate(-77);
        gyroscope.drive(-0.5, 36);
    }
}
