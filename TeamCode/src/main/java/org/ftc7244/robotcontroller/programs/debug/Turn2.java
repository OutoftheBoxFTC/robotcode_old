package org.ftc7244.robotcontroller.programs.debug;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.ftc7244.robotcontroller.autonomous.ControlSystemAutonomous;

/**
 * Created by BeaverDuck on 2/9/18.
 */
@Autonomous(name = "Turn 6°")
public class Turn2 extends ControlSystemAutonomous {
    @Override
    public void run() throws InterruptedException {
        gyroscope.rotate(6);
    }
}
