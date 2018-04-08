package org.ftc7244.robotcontroller.programs.debug;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.ftc7244.robotcontroller.autonomous.ControlSystemAutonomous;

@Autonomous(name = "Timed Turn")
@Disabled
public class TimedTurn extends ControlSystemAutonomous {
    @Override
    public void run(){
        robot.drive(-1, 1, 3000);
    }
}
