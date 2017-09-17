package org.ftc7244.robotcontroller.programs.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.ftc7244.robotcontroller.autonomous.PIDAutonomous;

@Autonomous(name = "Corner Blue [BASE]", group = "Blue")
public class BaseCornerBlue extends CornerBlue {

    @Override
    public void run() throws InterruptedException {
        super.run();
        //rotate backwards
        gyroscope.rotate(-95);
        //drive onto the platform
        gyroscope.drive(.5, 38);
    }
}
