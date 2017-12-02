package org.ftc7244.robotcontroller.programs.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.ftc7244.robotcontroller.autonomous.PIDAutonomous;

/**
 * Created by BeaverDuck on 12/1/17.
 */
@Autonomous(name = "Red Left")
public class RedLeft extends PIDAutonomous {
    @Override
    public void run() throws InterruptedException {
        robot.knockOverJewel(false);
    }
}
