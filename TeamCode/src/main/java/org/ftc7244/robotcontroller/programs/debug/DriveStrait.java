package org.ftc7244.robotcontroller.programs.debug;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.ftc7244.robotcontroller.autonomous.ControlSystemAutonomous;

/**
 * Created by ftc72 on 2/28/2018.
 */
@Autonomous
public class DriveStrait extends ControlSystemAutonomous {
    @Override
    public void run() throws InterruptedException {
        gyroscopePID.drive(1, 20);
    }
}
