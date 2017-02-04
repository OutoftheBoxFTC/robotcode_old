package org.ftc7244.robotcontroller;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.ftc7244.robotcontroller.autonomous.PIDAutonomous;

/**
 * Created by FTC 7244 on 2/4/2017.
 */
@Autonomous

public class Parallelize extends PIDAutonomous {
    @Override
    public void run() throws InterruptedException {
        ultrasonic.parallelize();
    }
}
