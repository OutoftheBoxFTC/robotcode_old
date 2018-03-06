package org.ftc7244.robotcontroller.programs.debug;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.ftc7244.robotcontroller.autonomous.ControlSystemAutonomous;

/**
 * Created by ftc72 on 2/28/2018.
 */
@Autonomous(name = "Drive Straight")
@Disabled
public class DriveStraight extends ControlSystemAutonomous {
    @Override
    public void run() throws InterruptedException {
        gyroscopePID.drive(1, 20);
    }
}
