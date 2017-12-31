package org.ftc7244.robotcontroller.programs.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.ftc7244.robotcontroller.autonomous.PIDAutonomous;

/**
 * Created by BeaverDuck on 12/31/17.
 */
@Autonomous(name = "Test")
public class JoshGuy extends PIDAutonomous {
    @Override
    public void run() throws InterruptedException {
        robot.getJewelVertical().setPosition(.31);
        robot.getJewelHorizontal().setPosition(0.45);
        sleep(1000000000);
    }
}
