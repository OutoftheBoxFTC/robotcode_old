package org.ftc7244.robotcontroller;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.ftc7244.robotcontroller.autonomous.BeaconAutonomous;
import org.ftc7244.robotcontroller.autonomous.drivers.GyroscopeDrive;

/**
 * Created by OOTB on 11/12/2016.
 */
//
@Autonomous(name = "Walled Beacon Blue")
public class WalledBeaconBlue extends BeaconAutonomous {

    @Override
    public void run() throws InterruptedException {
        drive(.35, 3.5);
        sleep(500);
        rotate(-52);
        drive(0.35, 28);
        robot.shootLoop(2, 500);
        drive(0.35, 21);

        sleep(500);
        rotate(50);
        driveUntilLine(.2, GyroscopeDrive.Sensor.Leading, 2);

        sleep(500);
        if (isColor(Color.BLUE)) {
            pushBeacon();
        } else {
            drive(0.3, 2.5);
            pushBeacon();
        }

        driveUntilLine(0.2, GyroscopeDrive.Sensor.Leading, .5);
        sleep(500);
        if (isColor(Color.BLUE)) {
            pushBeacon();
        } else {
            drive(0.3, 2.5);
            pushBeacon();
        }

        rotate(-45);
        drive(-.75, 40);
        sleep(2000);
        drive(-.75, 10);
    }
}
