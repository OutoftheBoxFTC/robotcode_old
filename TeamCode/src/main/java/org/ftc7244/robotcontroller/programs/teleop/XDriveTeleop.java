package org.ftc7244.robotcontroller.programs.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.RobotLog;

import org.ftc7244.robotcontroller.Westcoast;
import org.ftc7244.robotcontroller.XDrive;
import org.ftc7244.robotcontroller.input.Button;
import org.ftc7244.robotcontroller.input.ButtonType;
import org.ftc7244.robotcontroller.input.PressButton;

/**
 * Created by Stargamer285 on 10/9/2017.
 */
@TeleOp(name="XDrive")
public class XDriveTeleop extends OpMode {
    private XDrive robot;
    @Override
    public void init(){
        robot = new XDrive(this);
        robot.init();
    }
    @Override
    public void loop(){
        robot.getDriveBottomLeft().setPower(gamepad1.right_stick_y + gamepad1.right_stick_x - gamepad1.left_stick_x);
        robot.getDriveBottomRight().setPower(gamepad1.right_stick_y - gamepad1.right_stick_x - gamepad1.left_stick_x);
        robot.getDriveTopLeft().setPower(gamepad1.right_stick_y - gamepad1.right_stick_x - gamepad1.left_stick_x);
        robot.getDriveTopRight().setPower(gamepad1.right_stick_y + gamepad1.right_stick_x - gamepad1.left_stick_x);
    }
}
