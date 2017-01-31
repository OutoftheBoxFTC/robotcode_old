package org.ftc7244.robotcontroller;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.ftc7244.robotcontroller.autonomous.BeaconAutonomous;
import org.ftc7244.robotcontroller.autonomous.pid.drivers.GyroscopeDrive;

/**
 * Created by OOTB on 11/12/2016.
 */
//
@Autonomous(name = "Walled Beacon Red ")
public class WalledBeaconRed extends BeaconAutonomous {

    @Override
    public void run() throws InterruptedException {
        gyroscope.drive(-0.35, 3);
        sleep(500);
        gyroscope.rotate(51);
        gyroscope.drive(-0.35, 14);
        robot.shootLoop(2, 500);
        gyroscope.drive(-0.35, 29);

        sleep(500);
        gyroscope.rotate(-44);

        sleep(100);
        ultrasonic.parallelize();
        gyroscope.resetOrientation();

        gyroscope.driveUntilLine(-0.2, GyroscopeDrive.Sensor.Trailing);

        sleep(500);
        if (isColor(Color.RED)) {
            pushBeacon();
        } else {
            gyroscope.drive(-0.3, 2.5);
            pushBeacon();
        }

        gyroscope.driveUntilLine(-0.2, GyroscopeDrive.Sensor.Trailing, 0, 12, 60);
        sleep(500);
        gyroscope.drive(.2, 2);
        if (isColor(Color.RED)) {
            pushBeacon();
        } else {
            gyroscope.drive(-0.3, 4);
            pushBeacon();
        }

        gyroscope.rotate(45);
        gyroscope.drive(.75, 45);
        sleep(2000);
        gyroscope.drive(.75, 10);
    }
}
