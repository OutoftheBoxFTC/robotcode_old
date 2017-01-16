package org.ftc7244.robotcontroller;

import org.ftc7244.robotcontroller.autonomous.EncoderAutonomous;

/**
 * Created by OOTB on 11/7/2016.
 */

//@Autonomous(name="Shoot Ball Blue")
@Deprecated
public class ShootBlueBall extends EncoderAutonomous {
    @Override
    public void run() throws InterruptedException {
        drive(0.8, 10, 0);
        drive(1.0, 28, 28);

        //Put a pause
        robot.shootLoop(3, 1500);

        drive(1.0, 8, 8);
        drive(0.8, 13, 0);
        rotate(1.0, 118, Direction.RIGHT);

        robot.getIntake().setPower(1);
        drive(0.8, 25, 25);
        sleep(5000);
        drive(1.0, 11, 11);
        robot.getIntake().setPower(0);
    }
}
