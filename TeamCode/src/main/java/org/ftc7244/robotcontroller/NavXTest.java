package org.ftc7244.robotcontroller;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.RobotLog;

import org.ftc7244.robotcontroller.autonomous.PIDAutonomous;

/**
 * Created by FTC 7244 on 2/8/2017.
 */
@Autonomous
public class NavXTest extends PIDAutonomous {
    @Override
    public void run() throws InterruptedException {
        while (!isStopRequested()) RobotLog.ii("Gyro", gyroProvider.getX() + ":" + gyroProvider.getZ() + ":" + gyroProvider.getY());
    }
}
