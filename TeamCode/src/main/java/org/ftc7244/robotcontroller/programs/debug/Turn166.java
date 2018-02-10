package org.ftc7244.robotcontroller.programs.debug;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.ftc7244.robotcontroller.autonomous.PIDAutonomous;

/**
 * Created by BeaverDuck on 2/9/18.
 */
@Autonomous(name = "Turn 166°")
public class Turn166 extends PIDAutonomous {
    @Override
    public void run() throws InterruptedException {
        gyroscope.rotate(166);
    }
}
