package org.ftc7244.robotcontroller;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.ftc7244.robotcontroller.autonomous.PIDAutonomous;

/**
 * Created by OOTB on 11/11/2016.
 */

@Autonomous(name = "Corner Blue")
public class CornerBlue extends PIDAutonomous {

    @Override
    public void run() throws InterruptedException {
        //ENCODER BASE
        /*gyroscope.drive(.8, 12, 0);
        gyroscope.drive(1, 22, 22);
        gyroscope.rotate(.8, 55, Direction.LEFT);
        robot.shootLoop(3, 1000);
        gyroscope.rotate(.8, 60, Direction.RIGHT);
        robot.getIntake().setPower(-1);
        gyroscope.drive(1, 20, 20);*/

        gyroscope.drive(.5, 5);
        gyroscope.rotate(-45);
        gyroscope.drive(.5, 10);
        gyroscope.rotate(-92);
        sleep(1000);
        robot.shootLoop(2, 1500);
        gyroscope.rotate(90);
        gyroscope.drive(.5, 36);
    }
}
