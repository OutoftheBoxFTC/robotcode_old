package org.ftc7244.robotcontroller.programs.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by FTC 7244 on 4/3/2017.
 */
@Autonomous(name = "Beacon Blue [REVERSE] [BASE]", group = "Blue")
public class BaseReverseBeaconBlue extends ReverseBeaconBlue {

    @Override
    public void run() throws InterruptedException {
        super.run();
        gyroscope.rotate(-90);
        robot.getIntake().setPower(1);
        gyroscope.drive(0.5, 20);
        long sleep = getAutonomousEnd() - (System.currentTimeMillis() + 1250);
        if (sleep > 0) sleep(sleep);
        gyroscope.drive(0.5, 20);
    }
}
