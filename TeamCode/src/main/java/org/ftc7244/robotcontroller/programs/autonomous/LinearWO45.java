package org.ftc7244.robotcontroller.programs.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.ftc7244.robotcontroller.autonomous.ControlSystemAutonomous;

/**
 * Created by ftc72 on 2/21/2018.
 */

@Autonomous(name = "Linear W/O 45")
public class LinearWO45 extends ControlSystemAutonomous {
    @Override
    public void run() throws InterruptedException {
        gyroscopeSP.rotate(45);
    }
}
