package org.ftc7244.robotcontroller.programs.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.ftc7244.robotcontroller.autonomous.PIDAutonomous;

/**
 * Created by FTC 7244 on 10/29/2017.
 */
@Autonomous(name = "Test45")
public class TestAutonamous45degree extends PIDAutonomous {

    @Override
    public void run() throws InterruptedException {
        gyroscope.rotate(45);
    }
}
