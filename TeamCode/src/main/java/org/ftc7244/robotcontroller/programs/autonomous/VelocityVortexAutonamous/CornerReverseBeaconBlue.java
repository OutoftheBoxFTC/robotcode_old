package org.ftc7244.robotcontroller.programs.autonomous.VelocityVortexAutonamous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

/**
 * Created by FTC 7244 on 4/3/2017.
 */
@Deprecated
@Autonomous(name = "Beacon Blue [REVERSE] [CORNER]", group = "Blue")
@Disabled
public class CornerReverseBeaconBlue extends ReverseBeaconBlue {

    @Override
    public void run() throws InterruptedException {
        super.run();
        gyroscope.rotate(-75);
        gyroscope.drive(-0.2, 20);
    }
}
