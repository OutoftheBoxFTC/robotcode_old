package org.ftc7244.robotcontrol;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.ftc7244.robotcontrol.autonomous.BaseAutonomous;
import org.ftc7244.robotcontrol.autonomous.BeaconBaseAutonomous;

/**
 * Created by OOTB on 11/12/2016.
 */
//
@Autonomous(name="Beacon Blue")
public class BeaconBlue extends BaseAutonomous {
    @Override
    public void run() throws InterruptedException {
        drive(.5, 2);
        rotate(-45);
        drive(.5, 25);
        sleep(1000);
        robot.shootLoop(2, 1500);
        drive(.5, 25);
        rotate(45);
    }
}
