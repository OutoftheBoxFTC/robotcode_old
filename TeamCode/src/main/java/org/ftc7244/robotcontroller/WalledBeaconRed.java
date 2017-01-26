package org.ftc7244.robotcontroller;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.ftc7244.robotcontroller.autonomous.BeaconAutonomous;
import org.ftc7244.robotcontroller.autonomous.drivers.GyroscopeDrive;

/**
 * Created by OOTB on 11/12/2016.
 */
//
@Autonomous(name = "Walled Beacon Red ")
public class WalledBeaconRed extends BeaconAutonomous {

    @Override
    public void run() throws InterruptedException {
        drive(-0.35, 3);
        sleep(500);
        rotate(51);
        drive(-0.35, 14);
        robot.shootLoop(2, 500);
        drive(-0.35, 29);

        sleep(500);
        rotate(-44);
        driveUntilLine(-0.2, GyroscopeDrive.Sensor.Trailing, 0);

        sleep(500);
        if (isColor(Color.RED)) {
            pushBeacon();
        } else {
            drive(-0.3, 2.5);
            pushBeacon();
        }

        driveUntilLine(-0.2, GyroscopeDrive.Sensor.Trailing, 0);
        sleep(500);
        drive(.2, 2);
        if (isColor(Color.RED)) {
            pushBeacon();
        } else {
            drive(-0.3, 4);
            pushBeacon();
        }

        rotate(45);
        drive(.75, 45);
        sleep(2000);
        drive(.75, 10);
    }
}
