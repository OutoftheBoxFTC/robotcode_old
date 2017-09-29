package org.ftc7244.robotcontroller.programs.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@Deprecated
@Autonomous(name = "Corner Blue [CORNER]", group = "Blue")
@Disabled
public class CornerCornerBlue extends CornerBlue {

    @Override
    public void run() throws InterruptedException {
        super.run();
        //drive onto the platform
        gyroscope.rotate(-45);
        gyroscope.drive(1, 50);
        gyroscope.drive(.3, 30);
    }
}
