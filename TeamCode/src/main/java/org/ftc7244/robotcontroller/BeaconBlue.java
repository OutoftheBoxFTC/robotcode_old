package org.ftc7244.robotcontroller;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.ftc7244.robotcontroller.autonomous.BeaconAutonomous;
import org.ftc7244.robotcontroller.autonomous.pid.drivers.GyroscopeDrive;

/**
 * Created by OOTB on 11/12/2016.
 */
@Autonomous(name = "Beacon Blue", group = "Blue")
public class BeaconBlue extends BeaconAutonomous {

    @Override
    public void run() throws InterruptedException {
        gyroscope.drive(.35, 3.5);
        sleep(500);
        gyroscope.rotate(-54);
        gyroscope.drive(0.35, 28);
        robot.shootLoop(2, 500);
        gyroscope.drive(0.35, 21);

        sleep(500);
        gyroscope.rotate(54);

        sleep(100);
        ultrasonic.parallelize();
        sleep(300);
        gyroscope.resetOrientation();

        gyroscope.driveUntilLine(.2, GyroscopeDrive.Sensor.Leading, 2);
        sleep(500);
        if (isColor(Color.BLUE)) {
            pushBeacon();
        } else {
            gyroscope.drive(0.3, 2.5);
            pushBeacon();
        }

        gyroscope.driveUntilLine(0.2, GyroscopeDrive.Sensor.Leading, .5);
        sleep(500);
        if (isColor(Color.BLUE)) {
            pushBeacon();
        } else {
            gyroscope.drive(0.3, 2.5);
            pushBeacon();
        }

        gyroscope.rotate(-45);
        gyroscope.drive(-1, 40);
        sleep(2000);
        gyroscope.drive(-.75, 10);
    }
}
