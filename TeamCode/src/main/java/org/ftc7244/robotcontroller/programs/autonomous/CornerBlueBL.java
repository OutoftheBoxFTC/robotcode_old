package org.ftc7244.robotcontroller.programs.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.ftc7244.robotcontroller.autonomous.PIDAutonomous;


/**
 * Created by Eeshwar Laptop on 10/29/2017.
 */
@Autonomous(name = "CornerBlueBL")
public class CornerBlueBL extends PIDAutonomous {
    private String image;

    public void run() {
        double countsPerInch = 134.4 / (4 * Math.PI);
        robot.init();
        //image = imageProvider.getImageReading().name();
        waitForStart();
        sleep(1500);
        robot.drive(0.3, 0.3);
        sleep(850);
        robot.drive(0, 0);
        sleep(1500);
        robot.drive(-0.3, 0.3);
        switch (image) {
            case "R":
                sleep(800);
                break;
            case "C":
                sleep(900);
                break;
            case "L":
                sleep(1000);
                break;
            default:
                sleep(800);
                break;
        }
        robot.drive(0.3, 0.3);
        sleep(1300);
        robot.drive(0, 0);
        sleep(1500);
        robot.getIntakeTopLeft().setPower(1);
        sleep(2000);
        robot.getIntakeTopLeft().setPower(0);
        sleep(1500);
        robot.getSpring().setDirection(DcMotorSimple.Direction.REVERSE);
        robot.getSpring().setPower(1);
        sleep(500);
        robot.getSpring().setPower(0);
        sleep(1000);
        robot.drive(-0.3, -0.3);
        sleep(200);
        robot.drive(0, 0);
        sleep(500);
        while (opModeIsActive()) {
            //Loop to prevent crash
        }
    }
}
