package org.ftc7244.robotcontroller.programs.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.ftc7244.robotcontroller.autonomous.ControlSystemAutonomous;

/**
 * Created by ftc72 on 3/4/2018.
 */
@Autonomous(name = "Turn 45 Semi Proportional")
public class SP45 extends ControlSystemAutonomous {

    @Override
    public void run() throws InterruptedException {
        gyroscopeSP.rotate(45);
    }
}
