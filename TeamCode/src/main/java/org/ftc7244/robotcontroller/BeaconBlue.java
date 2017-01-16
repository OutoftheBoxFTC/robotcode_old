package org.ftc7244.robotcontroller;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.ftc7244.robotcontroller.autonomous.BeaconAutonomous;

/**
 * Created by OOTB on 11/12/2016.
 */
//
@Autonomous(name="Beacon Blue")
public class BeaconBlue extends BeaconAutonomous {

    @Override
    public void run() throws InterruptedException {
        drive(0.35, 29);
        sleep(1000);
        robot.shootLoop(2,  1500);
        drive(0.35, 11);
        //hi
        rotate(39.5);
        drive(0.5, 5);

        sleep(500);
        if (isColor(Color.BLUE)){
            pushBeacon();
        } else {
            drive(0.3, 5);
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
