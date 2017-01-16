package org.ftc7244.robotcontroller;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.ftc7244.robotcontroller.autonomous.PIDAutonomous;

/**
 * Created by OOTB on 11/11/2016.
 */

@Autonomous(name="Corner Red")
public class CornerRed extends PIDAutonomous {

    @Override
    public void run() throws InterruptedException {
        /*sleep(5000);
        drive(.8, -10, 0);
        drive(1, -24, -24);
        rotate(1, 35, Direction.RIGHT);
        robot.shootLoop(3, 1000);
        rotate(.8, 35, Direction.LEFT);
        drive(1, -15, -15);
        rotate(.8, 40, Direction.LEFT);
        drive(1, -10, -10);*/

        drive(-0.5, 5);
        rotate(45);
        drive(-0.5, 5);
        rotate(77);
        sleep(1000);
        robot.shootLoop(2, 1500);
        rotate(-77);
        drive(-0.5, 36);
    }
}
