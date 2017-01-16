package org.ftc7244.robotcontroller;

import org.ftc7244.robotcontroller.autonomous.EncoderAutonomous;

/**
 * Created by OOTB on 10/31/2016.
 */

//@Autonomous(name="Shoot Ball Red")
@Deprecated
public class ShootRedBall extends EncoderAutonomous {
    @Override
    public void run() throws InterruptedException {
        drive(0.8, -7.5, 0);
        drive(1.0, -16, -16);

        //Put a pause
        robot.shootLoop(3, 1500);

        drive(1.0, -16, -16);
        drive(0.8, -13, 0);

        robot.getIntake().setPower(1);
        drive(0.8, 26, 26);
        sleep(5000);
        drive(1.0, 11, 11);
        robot.getIntake().setPower(0);
    }
}
