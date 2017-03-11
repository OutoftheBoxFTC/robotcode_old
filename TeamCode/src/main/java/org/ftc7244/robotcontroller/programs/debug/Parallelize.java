package org.ftc7244.robotcontroller.programs.debug;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.ftc7244.robotcontroller.autonomous.PIDAutonomous;


@Autonomous
public class Parallelize extends PIDAutonomous {
    @Override
    public void run() throws InterruptedException {
        ultrasonic.parallelize();
    }
}
