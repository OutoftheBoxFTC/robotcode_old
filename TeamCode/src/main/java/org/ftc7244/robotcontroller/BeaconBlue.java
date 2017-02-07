package org.ftc7244.robotcontroller;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.ftc7244.robotcontroller.autonomous.PIDAutonomous;
import org.ftc7244.robotcontroller.autonomous.drivers.GyroscopeDrive;

@Autonomous(name = "Beacon Blue", group = "Blue")
public class BeaconBlue extends PIDAutonomous {

    @Override
    public void run() throws InterruptedException {
        //drive off the wall so we do not hit it
        gyroscope.drive(.35, 3.5);
        //allow the robot to settle and then rotate a little more than parallel to the side vortex
        sleep(500);
        gyroscope.rotate(-54);
        //drive to the center of the field and shoot then got the wall
        gyroscope.drive(0.35, 28);
        robot.shootLoop(2, 500);
        gyroscope.drive(0.35, 21);

        //attempt to rotate using our code then use the ultrasonic sensors to re-parallelize
        sleep(500);
        gyroscope.rotate(54);
        sleep(100);
        ultrasonic.parallelize();
        sleep(300);
        gyroscope.resetOrientation();

        //drive until a line is seen and drive two more inches
        gyroscope.driveUntilLine(.2, GyroscopeDrive.Sensor.Leading, 2);
        sleep(500);
        if (robot.isColor(Color.BLUE)) {
            //if it is in front of blue immediately press the button
            robot.pushBeacon();
        } else {
            //if it is red drive forward then press the button
            gyroscope.drive(0.3, 2.5);
            robot.pushBeacon();
        }

        //repreat task above but with different offsets
        gyroscope.driveUntilLine(0.2, GyroscopeDrive.Sensor.Leading, .5);
        sleep(500);
        if (robot.isColor(Color.BLUE)) {
            robot.pushBeacon();
        } else {
            gyroscope.drive(0.3, 2.5);
            robot.pushBeacon();
        }

        //rotate to be parallel with the field divider
        gyroscope.rotate(-45);
        gyroscope.drive(-1, 40);
        //pause and push the robot onto the platform in case it was pushed off
        sleep(2000);
        gyroscope.drive(-.75, 10);
    }
}
