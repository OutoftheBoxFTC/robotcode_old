package org.ftc7244.robotcontroller;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.ftc7244.robotcontroller.autonomous.PIDAutonomous;
import org.ftc7244.robotcontroller.autonomous.drivers.GyroscopeDrive;

@Autonomous(name = "Beacon Red", group = "Red")
public class BeaconRed extends PIDAutonomous {

    @Override
    public void run() throws InterruptedException {
        //drive off the wall so the robot does not hit it
        gyroscope.drive(-0.35, 3);
        sleep(500);
        //rotate over the amount of being parallel with the side goals and then goto the middle and shoot
        gyroscope.rotate(-51);
        gyroscope.drive(-0.35, 14);
        robot.shootLoop(2, 500);
        gyroscope.drive(-0.35, 31);

        //rotate and get parallel with wall
        sleep(500);
        gyroscope.rotate(44);
        sleep(100);
        ultrasonic.parallelize();
        sleep(300);
        gyroscope.resetOrientation();

        //drive until a line is seen
        gyroscope.drive(.2, 2);
        gyroscope.driveUntilLine(-0.2, GyroscopeDrive.Sensor.Trailing, 2);
        sleep(500);
        //the robot has passed the beacon but drive backwards to compensate
        if (robot.isColor(Color.RED)) {
            robot.pushBeacon();
        } else {
            //drive forward and press beacon
            gyroscope.drive(0.3, 4);
            robot.pushBeacon();
        }

        //repeat above
        gyroscope.driveUntilLine(-0.2, GyroscopeDrive.Sensor.Trailing, 2, 30, 60);
        sleep(500);
        if (robot.isColor(Color.RED)) {
            robot.pushBeacon();
        } else {
            gyroscope.drive(0.3, 4);
            robot.pushBeacon();
        }

        //get parallel with field divider
        gyroscope.rotate(-45);
        gyroscope.drive(1, 45);
        //wait and then drive more forward to ensure its on the stand
        long sleep = getAutonomousEnd() - (System.currentTimeMillis() + 500);
        if (sleep > 0) sleep(sleep);
        gyroscope.drive(.75, 10);
    }
}
