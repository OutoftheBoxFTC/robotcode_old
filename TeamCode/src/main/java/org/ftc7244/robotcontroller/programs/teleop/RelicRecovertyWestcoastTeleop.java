package org.ftc7244.robotcontroller.programs.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.ftc7244.robotcontroller.hardware.RelicRecoveryWestcoast;

/**
 * Created by FTC 7244 on 10/16/2017.
 */

@TeleOp(name = "Relic Recovery Westcoast")
public class RelicRecovertyWestcoastTeleop extends OpMode {
    RelicRecoveryWestcoast robot;
    public void init(){
        robot = new RelicRecoveryWestcoast(this);
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
