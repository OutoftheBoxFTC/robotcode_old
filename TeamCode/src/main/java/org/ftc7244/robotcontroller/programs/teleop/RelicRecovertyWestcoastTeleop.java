package org.ftc7244.robotcontroller.programs.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.ftc7244.robotcontroller.hardware.RelicRecoveryWestcoast;

/**
 * Created by FTC 7244 on 10/16/2017.
 */

@TeleOp(name = "Relic Recovery Westcoast")
public class RelicRecovertyWestcoastTeleop extends OpMode {
    private RelicRecoveryWestcoast robot;
    @Override
    public void init() {
        robot = new RelicRecoveryWestcoast(this);
        robot.init();
    }
    @Override
    public void loop() {
        robot.getLeft1().setPower(-gamepad1.left_stick_y);
        robot.getLeft2().setPower(-gamepad1.left_stick_y);
        robot.getRight1().setPower(-gamepad1.right_stick_y);
        robot.getRight2().setPower(-gamepad1.right_stick_y);
    }
}
