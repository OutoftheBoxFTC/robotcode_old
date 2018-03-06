package org.ftc7244.robotcontroller.programs.debug;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.ftc7244.robotcontroller.autonomous.ControlSystemAutonomous;

@Autonomous(name = "Drive Straight")
@Disabled
public class DriveStraight extends ControlSystemAutonomous {
    @Override
    public void run() throws InterruptedException {
        gyroscopePID.drive(1, 20);
    }
}
