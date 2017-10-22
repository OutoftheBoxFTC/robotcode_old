package org.ftc7244.robotcontroller.programs.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.robot.Robot;

import org.ftc7244.robotcontroller.Westcoast;
import org.ftc7244.robotcontroller.hardware.XDrive;

/**
 * Created by Eeshwar Laptop on 10/18/2017.
 */
@TeleOp(name="NewWestcoast")
public class NewWestcoastTeleop extends OpMode {
    Westcoast robot;
    public void init(){
        robot = new Westcoast(this);
        robot.init();
        telemetry.addLine("Westcoast started");
        telemetry.update();
    }
    @Override
    public void loop(){
        robot.getDriveBackLeft().setPower(gamepad1.left_stick_y);
        robot.getDriveFrontLeft().setPower(gamepad1.left_stick_y);
        robot.getDriveBackRight().setPower(-gamepad1.right_stick_y);
        robot.getDriveFrontRight().setPower(-gamepad1.right_stick_y);
    }
}