package org.ftc7244.robotcontroller.programs.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by FTC 7244 on 4/3/2017.
 */
@Autonomous(name = "Beacon Blue [REVERSE] [CORNER]", group = "Blue")
public class CornerReverseBeaconBlue extends ReverseBeaconBlue {

    @Override
    public void run() throws InterruptedException {
        super.run();
        gyroscope.rotate(-75);
        gyroscope.drive(-0.2, 20);
    }
}
