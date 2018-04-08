package org.ftc7244.robotcontroller.programs.debug;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.ftc7244.robotcontroller.autonomous.ControlSystemAutonomous;

@Autonomous(name = "Turn 90°")
@Disabled
public class Turn90 extends ControlSystemAutonomous {
    @Override
    public void run(){
        gyroscopePID.rotate(90);
    }
}