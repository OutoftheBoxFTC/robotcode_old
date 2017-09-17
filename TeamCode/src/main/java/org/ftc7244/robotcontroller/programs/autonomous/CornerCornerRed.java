package org.ftc7244.robotcontroller.programs.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Corner Red [CORNER]", group = "Red")
public class CornerCornerRed extends CornerRed {

    @Override
    public void run() throws InterruptedException {
        super.run();
        //drive onto the platform
        gyroscope.rotate(35);
        gyroscope.drive(-1, 50);
        gyroscope.drive(-0.3, 30);
    }
}
