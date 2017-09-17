package org.ftc7244.robotcontroller.programs.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.ftc7244.robotcontroller.autonomous.PIDAutonomous;

@Autonomous(name = "Corner Red [BASE]", group = "Red")
public class BaseCornerRed extends CornerRed {

    @Override
    public void run() throws InterruptedException {
        super.run();
        gyroscope.rotate(90);
        gyroscope.drive(-0.5, 28);
    }
}
