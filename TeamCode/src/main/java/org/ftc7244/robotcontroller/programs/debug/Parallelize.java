package org.ftc7244.robotcontroller.programs.debug;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.ftc7244.robotcontroller.autonomous.bases.VelocityVortexPIDAutonomous;


@Autonomous
public class Parallelize extends VelocityVortexPIDAutonomous {
    @Override
    public void run() throws InterruptedException {
        ultrasonic.parallelize();
    }
}
