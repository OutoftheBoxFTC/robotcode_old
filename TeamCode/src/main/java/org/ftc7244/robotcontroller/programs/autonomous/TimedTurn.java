package org.ftc7244.robotcontroller.programs.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.ftc7244.robotcontroller.autonomous.ControlSystemAutonomous;

/**
 * Created by ftc72 on 3/1/2018.
 */
@Autonomous(name = "Timed Turn")
public class TimedTurn extends ControlSystemAutonomous {
    @Override
    public void run() throws InterruptedException {
        robot.drive(-1, 1, 3000);
    }
}
