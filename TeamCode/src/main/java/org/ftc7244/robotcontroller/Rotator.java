package org.ftc7244.robotcontroller;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.ftc7244.robotcontroller.autonomous.PIDAutonomous;

/**
 * Created by OOTB on 1/19/2017.
 */
@Autonomous
public class Rotator extends PIDAutonomous {
    @Override
    public void run() throws InterruptedException {
        /*for (int i = 0; i < 4; i ++) {
            sleep(500);
            gyroscope.rotate(-45);
        }*/
        gyroscope.rotate(90);
    }
}
