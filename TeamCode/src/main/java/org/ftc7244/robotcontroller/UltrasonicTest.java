package org.ftc7244.robotcontroller;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.RobotLog;


@Autonomous(name = "Ultrasonic Test")
public class UltrasonicTest extends LinearOpMode {
    Westcoast robot = new Westcoast(this);
    private static final double distanceConversion = .984 * 2;

    @Override
    public void runOpMode() throws InterruptedException {
        robot.init();
        waitForStart();
        while(!isStopRequested()) {
            double leadingDistance = distanceConversion * robot.getLeadingUltrasonic().getVoltage(),
                    trailingDistance = distanceConversion * robot.getTrailingUltrasonic().getVoltage();
            RobotLog.ii("Voltage", "Leading: " + robot.getLeadingUltrasonic().getVoltage() + ", Trailing: " + robot.getLeadingUltrasonic().getVoltage());
            //RobotLog.ii("Distance", "Leading: " + leadingDistance + ", Trailing: " + trailingDistance);
        }
    }
}
