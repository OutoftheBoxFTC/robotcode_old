package org.ftc7244.robotcontroller.programs.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

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
    private Button dPadUp, dPadDown, dPadLeft, dPadRight, left_trigger, right_trigger, spring_button, intake_right, intake_left, a_button, b_button, slow_button;
    private static final double SLOW_DRIVE_COEFFICIENT = 0.5;
    private boolean vertLimit = true;
    public void init(){
        robot = new RelicRecoveryWestcoast(this);
        robot.init();
        slow_button = new Button(gamepad1, ButtonType.LEFT_TRIGGER);
        a_button = new Button(gamepad2, ButtonType.A);
        b_button = new Button(gamepad2, ButtonType.B);
        dPadUp = new Button(gamepad2, ButtonType.D_PAD_UP);
        dPadDown = new Button(gamepad2, ButtonType.D_PAD_DOWN);
        dPadLeft = new Button(gamepad2, ButtonType.D_PAD_LEFT);
        dPadRight = new Button(gamepad2, ButtonType.D_PAD_RIGHT);
        left_trigger = new Button(gamepad2, ButtonType.LEFT_BUMPER);
        right_trigger = new Button(gamepad2, ButtonType.RIGHT_BUMPER);
        spring_button = new Button(gamepad1, ButtonType.LEFT_BUMPER);
        intake_left = new Button(gamepad2, ButtonType.LEFT_TRIGGER);
        intake_right = new Button(gamepad2, ButtonType.RIGHT_TRIGGER);
    }
    @Override
    public void loop(){
        if(slow_button.isPressed()){
            robot.drive(-gamepad1.left_stick_y, -gamepad1.right_stick_y);
        }
        else {
            robot.drive(-gamepad1.left_stick_y* SLOW_DRIVE_COEFFICIENT,
                    -gamepad1.right_stick_y* SLOW_DRIVE_COEFFICIENT);
        }
        if(intake_left.isPressed()) {
            robot.getIntake().setPower(1);
        }else if (intake_right.isPressed()){
            robot.getIntake().setPower(-1);
        }else{
            robot.getIntake().setPower(0);
        }
        if(b_button.isPressed()) {
            robot.getIntakeBtmLf().setPower(0.5);
            robot.getIntakeBtmRt().setPower(-0.5);
        }else if(a_button.isPressed()){
            robot.getIntakeBtmLf().setPower(-0.5);
            robot.getIntakeBtmRt().setPower(0.5);
        }else{
            robot.getIntakeBtmLf().setPower(0);
            robot.getIntakeBtmRt().setPower(0);
        }
        if(left_trigger.isPressed()){
            robot.getIntakeVerticle().setPower(0.5);
        }else if(right_trigger.isPressed()){
            robot.getIntakeVerticle().setPower(-0.5);
        }else{
            robot.getIntakeVerticle().setPower(-0.1);
        }
        if(spring_button.isPressed()){
            robot.getSpring().setDirection(DcMotorSimple.Direction.REVERSE);
            robot.getSpring().setPower(1);
        }else{
            robot.getSpring().setPower(0);
        }
    }
}