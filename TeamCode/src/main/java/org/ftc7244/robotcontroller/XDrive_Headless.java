package org.ftc7244.robotcontroller;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.ftc7244.robotcontroller.hardware.XDrive;
import org.ftc7244.robotcontroller.sensor.gyroscope.PhoneGyroscopeProvider;

/**
 * Created by Eeshwar Laptop on 10/27/2017.
 */
@TeleOp(name="XDrive Teleop Headless")
public class XDrive_Headless extends OpMode {
    double x;
    double y;
    double rot;
    double direction;
    double dir;
    double newx;
    double newy;
    double dirtime;
    boolean wasRunning;
    double nanotosec = 1000000000;
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
        direction = Math.atan2(x, y) + direction;
        newx = Math.cos(direction);
        newy = Math.sin(direction);
        telemetry.addData("RotZ", direction);
        robot.getDriveBottomLeft().setPower(-newx + gamepad1.left_stick_x);
        robot.getDriveBottomRight().setPower(newy + gamepad1.left_stick_x);
        robot.getDriveTopLeft().setPower(-newy + gamepad1.left_stick_x);
        robot.getDriveTopRight().setPower(newx + gamepad1.left_stick_x);
        telemetry.addData("NewX", newx);
        telemetry.addData("NewY", newy);
        gyro.calibrate();
        telemetry.update();
        if(gamepad1.left_stick_x != 0 && !wasRunning){
            wasRunning = true;
            dirtime = System.nanoTime();
        }
        if(gamepad1.left_stick_x >= 0 && wasRunning){
            direction += (((System.nanoTime() - dirtime) / nanotosec) / 1.988) * 360;
        }
        if(gamepad1.left_stick_x <= 0 && wasRunning){
            direction += (((System.nanoTime() - dirtime) / nanotosec) / 1.988) * 360;
        }

    }
}
