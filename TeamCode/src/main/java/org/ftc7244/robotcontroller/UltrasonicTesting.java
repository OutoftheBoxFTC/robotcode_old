package org.ftc7244.robotcontroller;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.RobotLog;

/**
 * Created by OOTB on 1/15/2017.
 */
@Autonomous
public class UltrasonicTesting extends LinearOpMode {

    Westcoast westcoast;

    @Override
    public void runOpMode() throws InterruptedException {
        westcoast = new Westcoast(this);
        waitForStart();

        while (!isStopRequested()) {
            RobotLog.ii("HELP",
                    "Trailing: " + westcoast.getTrailingUltrasonic().getUltrasonicLevel() +
                    "Leading: " + westcoast.getLeadingUltrasonic().getUltrasonicLevel());
        }
    }
}
