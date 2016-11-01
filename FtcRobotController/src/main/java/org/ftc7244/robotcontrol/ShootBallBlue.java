package org.ftc7244.robotcontrol;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.ftc7244.robotcontrol.core.EncoderBaseAutonomous;

/**
 * Created by OOTB on 10/31/2016.
 */

@Autonomous(name="Shoot Ball Blue")
public class ShootBallBlue extends EncoderBaseAutonomous {
    @Override
    public void run() throws InterruptedException {
        encoderDrive(.4, -16, 0, 100);
        encoderDrive(.4, -37, -37, 100);
    }
}
