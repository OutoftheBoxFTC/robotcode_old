package org.ftc7244.robotcontroller.programs.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.Gyroscope;

import org.ftc7244.robotcontroller.XDrive;
import org.ftc7244.robotcontroller.input.Button;
import org.ftc7244.robotcontroller.input.ButtonType;
import org.ftc7244.robotcontroller.sensor.gyroscope.PhoneGyroscopeProvider;

/**
 * Created by Stargamer285 on 10/9/2017.
 */
@TeleOp(name="XDrive")
public class XDriveTeleop extends OpMode {
    private XDrive robot;
//    private Button xButton;
//    private PhoneGyroscopeProvider gyro;
    @Override
    public void init(){
        robot = new XDrive(this);
        robot.init();
//        gyro.start(hardwareMap);
//        xButton = new Button(gamepad1, ButtonType.X);
    }

    @Override
    public void loop(){

        robot.getDriveBottomLeft().setPower(gamepad1.right_stick_y - gamepad1.left_stick_x);
        robot.getDriveBottomRight().setPower(gamepad1.right_stick_x - gamepad1.left_stick_x);
        robot.getDriveTopLeft().setPower(-gamepad1.right_stick_x - gamepad1.left_stick_x);
        robot.getDriveTopRight().setPower(-gamepad1.right_stick_y - gamepad1.left_stick_x);

/*        if(xButton.isPressed()){
            gyro.calibrate();
        }
        final double x = Math.pow(gamepad1.left_stick_x, 3.0);
        final double y = Math.pow(gamepad1.left_stick_y, 3.0);
        final double rotation = Math.pow(gamepad1.right_stick_x, 3.0);
        final double direction = Math.atan2(x, y) + gyro.getX();
        final double speed = Math.min(1.0, Math.sqrt(x * x + y * y));

        robot.getDriveBottomLeft().setPower(speed * Math.sin(direction + Math.PI / 4.0) + rotation);
        robot.getDriveBottomRight().setPower(speed * Math.cos(direction + Math.PI / 4.0) - rotation);
        robot.getDriveTopLeft().setPower(speed * Math.cos(direction + Math.PI / 4.0) + rotation);
        robot.getDriveTopRight().setPower(speed * Math.sin(direction + Math.PI / 4.0) - rotation);

*/    }

}
