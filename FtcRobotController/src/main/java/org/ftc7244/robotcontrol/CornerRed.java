package org.ftc7244.robotcontrol;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.ftc7244.robotcontrol.autonomous.EncoderBaseAutonomous;

/**
 * Created by OOTB on 11/11/2016.
 */

@Autonomous(name="Corner Red")
public class CornerRed extends EncoderBaseAutonomous {

    @Override
    public void run() throws InterruptedException {
        sleep(5000);
        drive(.8, -10, 0);
        drive(1, -24, -24);
        rotate(1, 35, Direction.RIGHT);
        robot.shootLoop(3, 1000);
        rotate(.8, 35, Direction.LEFT);
        drive(1, -15, -15);
        rotate(.8, 40, Direction.LEFT);
        drive(1, -10, -10);
    }
}
