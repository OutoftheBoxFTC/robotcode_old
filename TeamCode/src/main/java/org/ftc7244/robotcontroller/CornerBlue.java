package org.ftc7244.robotcontroller;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.ftc7244.robotcontroller.autonomous.PIDAutonomous;

/**
 * Created by OOTB on 11/11/2016.
 */

@Autonomous(name="Corner Blue")
public class CornerBlue extends PIDAutonomous {

    @Override
    public void run() throws InterruptedException {
        //ENCODER BASE
        /*drive(.8, 12, 0);
        drive(1, 22, 22);
        rotate(.8, 55, Direction.LEFT);
        robot.shootLoop(3, 1000);
        rotate(.8, 60, Direction.RIGHT);
        robot.getIntake().setPower(-1);
        drive(1, 20, 20);*/

        drive(.5, 5);
        rotate(-45);
        drive(.5, 10);
        rotate(-92);
        sleep(1000);
        robot.shootLoop(2, 1500);
        rotate(90);
        drive(.5, 36);
    }
}
