package org.ftc7244.robotcontrol;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.ftc7244.robotcontrol.core.EncoderBaseAutonomous;

/**
 * Created by OOTB on 11/7/2016.
 */

@Autonomous(name="Shoot Ball Blue")
public class ShootBlueBall extends EncoderBaseAutonomous {
    @Override
    public void run() throws InterruptedException {
        drive(0.4, 8.5, 0);
        drive(1.0, 26, 21);

        //Put a pause
        robot.shootLoop(3, 1000);

        drive(1.0, 12, 12);
        drive(0.4, 13, 0);
        rotate(1.0, 130, Direction.RIGHT);

        robot.getIntake().setPower(1);
        drive(0.8, 25, 25);
        sleep(5000);
        drive(1.0, 15, 15);
        robot.getIntake().setPower(0);
    }
}
