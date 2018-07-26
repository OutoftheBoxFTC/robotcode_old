package org.ftc7244.robotcontroller.programs.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.ftc7244.robotcontroller.hardware.Westcoast;
@Autonomous
public class test extends LinearOpMode {
    Westcoast robot;

    @Override
    public void runOpMode() throws InterruptedException {
        robot = new Westcoast(this);
        robot.init();
        robot.initServos();
        waitForStart();
        while (opModeIsActive()) {
            robot.drive(1, 1, 1000);
            robot.drive(-1, 1, 100);
            robot.drive(1, 1, 1000);
        }
    }
}
