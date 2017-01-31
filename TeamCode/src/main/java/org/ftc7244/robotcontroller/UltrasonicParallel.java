package org.ftc7244.robotcontroller;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.RobotLog;

import org.ftc7244.robotcontroller.autonomous.PIDAutonomous;

/**
 * Created by FTC 7244 on 1/30/2017.
 */
@Autonomous
public class UltrasonicParallel extends PIDAutonomous {
    @Override
    public void run() throws InterruptedException {
        ultrasonic.parallelize();
    }
}
