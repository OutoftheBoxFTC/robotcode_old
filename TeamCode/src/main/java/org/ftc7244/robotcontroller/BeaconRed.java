package org.ftc7244.robotcontroller;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.ftc7244.robotcontroller.autonomous.BeaconAutonomous;

/**
 * Created by OOTB on 11/12/2016.
 */
//
@Autonomous(name = "Beacon Red")
@Deprecated
@Disabled
public class BeaconRed extends BeaconAutonomous {

    @Override
    public void run() throws InterruptedException {
        gyroscope.drive(-0.35, 16);
        sleep(1000);
        robot.shootLoop(2, 1500);
        gyroscope.drive(-0.35, 21.5);
        gyroscope.rotate(-43);
        gyroscope.drive(-0.35, 2);

        sleep(2000);
        if (isColor(Color.RED)) {
            pushBeacon();
        } else {
            gyroscope.drive(-0.35, 5);
            pushBeacon();
        }

        gyroscope.rotate(90);
        robot.getIntake().setPower(1);
        gyroscope.drive(.75, 40);
        sleep(2000);
        gyroscope.drive(.75, 10);
    }

}
