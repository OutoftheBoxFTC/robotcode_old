package org.ftc7244.robotcontroller.programs.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.ftc7244.robotcontroller.XDrive;

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
