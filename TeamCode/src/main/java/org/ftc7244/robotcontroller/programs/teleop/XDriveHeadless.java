package org.ftc7244.robotcontroller.programs.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.ftc7244.robotcontroller.hardware.XDrive;
import org.ftc7244.robotcontroller.sensor.gyroscope.PhoneGyroscopeProvider;

/**
 * Created by Eeshwar Laptop on 10/27/2017.
 */
@TeleOp(name="XDrive Teleop Headless")
public class XDriveHeadless extends OpMode {
    double x;
    double y;
    double rot;
    double direction;
    double newx;
    double newy;
    XDrive robot = new XDrive(this);
    PhoneGyroscopeProvider gyro = new PhoneGyroscopeProvider();
    public void init(){
        gyro.start(hardwareMap);
        gyro.calibrate();
        robot.init();
    }
    public void loop(){
        x = gamepad1.right_stick_x;
        y = gamepad1.right_stick_y;
        rot = gamepad1.left_stick_x;
        gyro.calibrate();
        direction = Math.atan2(x, y) + gyro.getZ();
        newx = Math.cos(direction);
        newy = Math.sin(direction);
        newx = Math.round(newx);
        newx = Math.round(newx);
        robot.getDriveBottomLeft().setPower(-newx - rot);
        robot.getDriveBottomRight().setPower(newy - rot);
        robot.getDriveTopLeft().setPower(-newy - rot);
        robot.getDriveTopRight().setPower(newx - rot);
    }
}
