package org.ftc7244.robotcontroller.programs.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.ftc7244.robotcontroller.autonomous.PIDAutonomous;
@Deprecated
@Autonomous(name = "Corner Red [BASE]", group = "Red")
@Disabled
public class BaseCornerRed extends CornerRed {

    @Override
    public void run() throws InterruptedException {
        super.run();
        gyroscope.rotate(90);
        gyroscope.drive(-0.5, 28);
    }
}
