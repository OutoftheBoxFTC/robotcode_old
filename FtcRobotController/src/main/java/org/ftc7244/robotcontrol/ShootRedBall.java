package org.ftc7244.robotcontrol;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.ftc7244.robotcontrol.core.EncoderBaseAutonomous;

/**
 * Created by OOTB on 10/31/2016.
 */

@Autonomous(name="Shoot Ball Red")
public class ShootRedBall extends EncoderBaseAutonomous {
    @Override
    public void run() throws InterruptedException {
        drive(0.4, -8.5, 0);
        drive(1.0, -20, -25);

        //Put a pause
        for (int i = 0; i < 3; i++) robot.shoot(1000);

        drive(1.0, -10, -10);
        drive(0.4, -13, 0);
        robot.getIntake().setPower(1);
        drive(1.0, 25, 25);
        sleep(5000);
        drive(1.0, 15, 15);
        robot.getIntake().setPower(0);
    }
}
