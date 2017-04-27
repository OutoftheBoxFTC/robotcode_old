package org.ftc7244.robotcontroller.programs.autonomous;

import android.graphics.Color;

import org.ftc7244.robotcontroller.autonomous.PIDAutonomous;
import org.ftc7244.robotcontroller.autonomous.drivers.GyroscopeDrive;

/**
 * Created by FTC 7244 on 3/31/2017.
 */
public class ReverseBeaconBlue extends PIDAutonomous {

    @Override
    public void run() throws InterruptedException {
        gyroscope.drive(0.75, 85, 36);
        sleep(500);
        gyroscope.rotate(-38);
        ultrasonic.parallelize();
        sleep(300);
        gyroscope.resetOrientation();
        gyroscope.driveUntilLine(0.2, GyroscopeDrive.Sensor.Leading, 5);
        sleep(150);
        if (robot.isColor(Color.BLUE)) {
            //if it is in front of blue immediately press the button
            robot.pushBeacon();
        } else {
            //if it is red drive forward then press the button
            gyroscope.drive(-0.2, 2.5);
            robot.pushBeacon();
        }

        gyroscope.driveUntilLine(-0.25, GyroscopeDrive.Sensor.Trailing, 0.5, 35, 55);
        sleep(500);
        if (robot.isColor(Color.BLUE)) {
            //if it is in front of blue immediately press the button
            robot.pushBeacon();
        } else {
            //if it is red drive forward then press the button
            gyroscope.drive(0.2, 2.5);
            robot.pushBeacon();
        }

        gyroscope.rotate(30);
        gyroscope.drive(-0.5, 15);
        robot.shootLoop(2, 500);
    }
}
