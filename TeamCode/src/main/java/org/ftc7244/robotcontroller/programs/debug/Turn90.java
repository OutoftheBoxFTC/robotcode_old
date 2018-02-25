package org.ftc7244.robotcontroller.programs.debug;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.ftc7244.robotcontroller.autonomous.ControlSystemAutonomous;

/**
 * Created by BeaverDuck on 2/2/18.
 */
@Autonomous(name = "Turn 90°")
public class Turn90 extends ControlSystemAutonomous {
    @Override
    public void run() throws InterruptedException {
        gyroscope.rotate(90);
    }
}