package org.ftc7244.robotcontroller.programs.autonomous;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.Servo;

import org.ftc7244.robotcontroller.autonomous.PIDAutonomous;

/**
 * Created by BeaverDuck on 12/1/17.
 */
@Autonomous(name = "Red Left")
public class RedLeft extends PIDAutonomous {
    @Override
    public void run() throws InterruptedException {
        robot.knockOverJewel(Color.BLUE);
        sleep(1500);
        robot.drive(0.3, -0.3);
        sleep(500);
        robot.drive(0.3, 0.3);
        sleep(1000);
        robot.drive(0, 0);
        sleep(1000);
        robot.drive(0.3, -0.3);
        sleep(700);
        robot.drive(0, 0);
        sleep(1000);
        robot.drive(0.3, 0.3);
        sleep(200);
        robot.drive(0, 0);
        sleep(1000);
        robot.getIntakeBottomRight().setPower(1);
        robot.getIntakeBottomLeft().setPower(1);
        sleep(5000);
        robot.getIntakeBottomRight().setPower(0);
        robot.getIntakeBottomLeft().setPower(0);
        sleep(500);
//        robot.getSpring().setDirection(Servo.Direction.REVERSE);
//        robot.getIntakeBottomRight().setPower(1);
//        robot.getIntakeBottomLeft().setPower(1);
        robot.getSpring().setPosition(0.4);
        sleep(500);
//        robot.getIntakeBottomRight().setPower(0);
//        robot.getIntakeBottomLeft().setPower(0);
        sleep(250);
        robot.drive(-0.3, -0.3);
        sleep(150);
        robot.drive(0, 0);
        sleep(500);
        robot.drive(-0.3, 0.3);
        sleep(300);
        robot.drive(0, 0);
    }
}
