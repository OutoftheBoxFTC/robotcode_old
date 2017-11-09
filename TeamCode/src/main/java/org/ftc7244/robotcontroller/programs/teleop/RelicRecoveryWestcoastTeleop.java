package org.ftc7244.robotcontroller.programs.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.ftc7244.robotcontroller.hardware.RelicRecoveryWestcoast;
import org.ftc7244.robotcontroller.input.Button;
import org.ftc7244.robotcontroller.input.ButtonType;
import org.ftc7244.robotcontroller.input.PressButton;

/**
 * Created by Eeshwar Laptop on 10/16/2017.
 */


@TeleOp(name = "Relic Recovery Westcoast")
public class RelicRecoveryWestcoastTeleop extends OpMode {
    RelicRecoveryWestcoast robot;
    private Button dPadUp, dPadDown, left_trigger, right_trigger;
    private PressButton right_trigger_slow;
    private static final double SLOW_DRIVE_COEFFICIENT = 0.5;
    public void init(){
        robot = new RelicRecoveryWestcoast(this);
        robot.init();

        right_trigger_slow = new PressButton(gamepad1, ButtonType.A);
        left_trigger = new Button(gamepad2, ButtonType.D_PAD_LEFT);
        right_trigger = new Button(gamepad2, ButtonType.D_PAD_RIGHT);
    }
    @Override
    public void loop(){
        if(right_trigger_slow.isPressed()){
            robot.drive(-gamepad1.left_stick_y, -gamepad1.right_stick_y);
        }
        else {
            robot.drive(-gamepad1.left_stick_y* SLOW_DRIVE_COEFFICIENT, -gamepad1.right_stick_y* SLOW_DRIVE_COEFFICIENT);
        }
        if(left_trigger.isPressed()){
            robot.getIntake().setPower(1);
        }
        else if(right_trigger.isPressed()){
            robot.getIntake().setPower(-1);
        }
        else {
            robot.getIntake().setPower(0);
        }
    }
}