package org.ftc7244.robotcontroller.programs.debug;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.ftc7244.robotcontroller.autonomous.ControlSystemAutonomous;

/**
 * Created by ftc72 on 3/1/2018.
 */

@Autonomous(name = "Turn 45")
public class Turn45 extends ControlSystemAutonomous {
    @Override
    public void run() throws InterruptedException {
        gyroscopePID.rotate(45);
    }
}
