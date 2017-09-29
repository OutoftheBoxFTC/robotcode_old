package org.ftc7244.robotcontroller.programs.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

/**
 * Created by FTC 7244 on 4/10/2017.
 */
@Deprecated
@Autonomous(name = "Beacon Red [REVERSE] [CORNER]", group = "Red")
@Disabled
public class CornerReverseBeaconRed extends ReverseBeaconRed {

    @Override
    public void run() throws InterruptedException {
        super.run();
        gyroscope.rotate(-90);
        gyroscope.drive(-0.2, 30);
    }
}
