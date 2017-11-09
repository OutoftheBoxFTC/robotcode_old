package org.ftc7244.robotcontroller.programs.autonomous.RelicRecoveryAutonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.ftc7244.robotcontroller.autonomous.bases.RelicRecoveryPIDAutonamous;

/**
 * Created by Eeshwar Laptop on 11/8/2017.
 */
@Autonomous(name="GyroDrive")
public class gyroDrive extends RelicRecoveryPIDAutonamous {
    public void run() throws InterruptedException {
        waitForStart();
        gyroscope.drive(0.25, 36);
    }
}

