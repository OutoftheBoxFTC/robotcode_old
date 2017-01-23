package org.ftc7244.robotcontroller;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.ftc7244.robotcontroller.autonomous.BeaconAutonomous;
import org.ftc7244.robotcontroller.autonomous.drivers.GyroscopeDrive;
import org.ftc7244.robotcontroller.autonomous.pid.Handler;

/**
 * Created by OOTB on 11/12/2016.
 */
//
@Autonomous(name="Walled Beacon Blue")
public class WalledBeaconBlue extends BeaconAutonomous {

    @Override
    public void run() throws InterruptedException {
        drive(.5, 3);
        rotate(-54);
        drive(0.35, 27);
        sleep(1000);
        robot.shootLoop(2,  1500);
        drive(0.35, 15);

        rotate(54);
        driveUntilLine(.2, GyroscopeDrive.Sensor.Leading, 2);

        sleep(500);
        if (isColor(Color.BLUE)){
            pushBeacon();
        } else {
            drive(0.3, 3.5);
            sleep(1);
            pushBeacon();
        }
        rotate(80);
        robot.getIntake().setPower(1);
        drive(.75, 40);
        sleep(2000);
        drive(.75, 10);
    }
}
