package org.ftc7244.robotcontroller.programs.debug;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.ftc7244.robotcontroller.autonomous.ControlSystemAutonomous;

/**
 * Created by BeaverDuck on 2/9/18.
 */
@Autonomous(name = "Turn 6°")
@Disabled
public class Turn6 extends ControlSystemAutonomous {
    @Override
    public void run() throws InterruptedException {
        gyroscopePID.rotate(6);
    }
}
