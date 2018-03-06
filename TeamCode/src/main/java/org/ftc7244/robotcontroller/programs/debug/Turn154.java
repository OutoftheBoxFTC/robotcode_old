package org.ftc7244.robotcontroller.programs.debug;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.ftc7244.robotcontroller.autonomous.ControlSystemAutonomous;

/**
 * Created by ftc72 on 3/1/2018.
 */

@Autonomous(name = "Turn -154")
public class Turn154 extends ControlSystemAutonomous{

    @Override
    public void run() throws InterruptedException {
        robot.getIntakeLift().setPower(1);
        sleep(100);
        robot.getIntakeLift().setPower(0.1);
        gyroscopePID.rotate(-154);
    }
}
