package org.ftc7244.robotcontroller;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.ftc7244.robotcontroller.autonomous.PIDAutonomous;

/**
 * Created by OOTB on 11/11/2016.
 */
@Autonomous(name = "Corner Blue", group = "Blue")
public class CornerBlue extends PIDAutonomous {

    @Override
    public void run() throws InterruptedException {
        sleep(8000);
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
