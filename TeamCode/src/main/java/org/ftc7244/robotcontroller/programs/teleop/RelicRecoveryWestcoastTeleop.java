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
    private Button dPadUp, dPadDown, dPadLeft, dPadRight;
    private PressButton a_button;
    private static final double SLOW_DRIVE_COEFFICIENT = 0.5;
    public void init(){
        robot = new RelicRecoveryWestcoast(this);
        robot.init();

        a_button = new PressButton(gamepad1, ButtonType.A);
        dPadUp = new Button(gamepad2, ButtonType.D_PAD_UP);
        dPadDown = new Button(gamepad2, ButtonType.D_PAD_DOWN);
        dPadLeft = new Button(gamepad2, ButtonType.D_PAD_LEFT);
        dPadRight = new Button(gamepad2, ButtonType.D_PAD_RIGHT);
    }
    @Override
    public void loop(){
        if(a_button.isPressed()){
            robot.drive(-gamepad1.left_stick_y, -gamepad1.right_stick_y);
        }
        else {
            robot.drive(-gamepad1.left_stick_y* SLOW_DRIVE_COEFFICIENT,
                    -gamepad1.right_stick_y* SLOW_DRIVE_COEFFICIENT);
        }

        if(dPadLeft.isPressed()){
            robot.getIntake().setPower(1);
        }
        else if(dPadRight.isPressed()){
            robot.getIntake().setPower(-1);
        }
        else {
            robot.getIntake().setPower(0);
        }

        if(dPadUp.isPressed()){
            robot.getIntakeBtmLf().setPower(1);
            robot.getIntakeBtmRt().setPower(1);
        }
        else if(dPadDown.isPressed()){
            robot.getIntakeBtmLf().setPower(-1);
            robot.getIntakeBtmRt().setPower(-1);
        }
        else {
            robot.getIntakeBtmLf().setPower(0);
            robot.getIntakeBtmRt().setPower(0);
        }
    }
}