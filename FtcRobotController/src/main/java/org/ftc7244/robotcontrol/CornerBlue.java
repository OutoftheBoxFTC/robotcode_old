package org.ftc7244.robotcontrol;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.ftc7244.robotcontrol.autonomous.EncoderBaseAutonomous;

/**
 * Created by OOTB on 11/11/2016.
 */

@Autonomous(name="Corner Blue")
public class CornerBlue extends EncoderBaseAutonomous {

    @Override
    public void run() throws InterruptedException {
        drive(.8, 12, 0);
        drive(1, 22, 22);
        rotate(.8, 55, Direction.LEFT);
        robot.shootLoop(3, 1000);
        rotate(.8, 60, Direction.RIGHT);
        robot.getIntake().setPower(-1);
        drive(1, 20, 20);

        //Made drive 45 instead of 90
        //Moved from 14 to 12 inches in turn
        //turned 55 instead of 45
        //changed distance from 24 to 22
        // decreased rotation by 50
        //changed forward from 15 to 18
        //rotate 50 to 52
        //rotate from 52 to 55
    }
}
