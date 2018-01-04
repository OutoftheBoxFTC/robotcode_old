package org.ftc7244.robotcontroller.programs.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.ftc7244.robotcontroller.autonomous.PIDAutonomous;

/**
 * Created by BeaverDuck on 1/3/18.
 */

@Autonomous(name = "Turn 90")
public class Turn90 extends PIDAutonomous{
    @Override
    public void run() throws InterruptedException {
        gyroscope.rotate(90);
    }
}
