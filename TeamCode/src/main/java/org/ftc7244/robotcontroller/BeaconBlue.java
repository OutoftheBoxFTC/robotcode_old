package org.ftc7244.robotcontroller;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.ftc7244.robotcontroller.autonomous.BeaconAutonomous;

/**
 * Created by OOTB on 11/12/2016.
 */
//
@Autonomous(name = "Beacon Blue")
@Deprecated
@Disabled
public class BeaconBlue extends BeaconAutonomous {

    @Override
    public void run() throws InterruptedException {
        gyroscope.drive(0.35, 29);
        sleep(1000);
        robot.shootLoop(2, 1500);
        gyroscope.drive(0.35, 11);
        //hi
        gyroscope.rotate(39.5);
        gyroscope.drive(0.5, 5);

        sleep(500);
        if (isColor(Color.BLUE)) {
            pushBeacon();
        } else {
            gyroscope.drive(0.3, 5);
            sleep(1);
            pushBeacon();
        }
        gyroscope.rotate(80);
        robot.getIntake().setPower(1);
        gyroscope.drive(.75, 40);
        sleep(2000);
        gyroscope.drive(.75, 10);
    }
}
