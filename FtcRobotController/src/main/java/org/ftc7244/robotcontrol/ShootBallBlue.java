package org.ftc7244.robotcontrol;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.ftc7244.robotcontrol.core.EncoderBaseAutonomous;

/**
 * Created by OOTB on 10/31/2016.
 */

@Autonomous(name="Shoot Ball Red")
public class ShootBallBlue extends EncoderBaseAutonomous {
    @Override
    public void run() throws InterruptedException {
        moveDistance(.4, 13, 0, 100);
        moveDistance(1, 20, 25, 100);

        //Put a pause
        for (int i = 0; i < 3; i++) robot.shoot(1000);

        moveDistance(1, 8, 8, 100);
        moveDistance(.4, 13, 0, 100);
        robot.getIntake().setPower(1);
        moveDistance(-1, 25, 25, 100);
        sleep(5000);
        moveDistance(-1, 0, 8, 100);
        moveDistance(-1, 15, 15, 100);
        robot.getIntake().setPower(0);
    }
}
