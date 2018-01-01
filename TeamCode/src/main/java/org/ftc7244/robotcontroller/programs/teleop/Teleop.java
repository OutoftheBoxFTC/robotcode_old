package org.ftc7244.robotcontroller.programs.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.ftc7244.robotcontroller.hardware.Westcoast;
import org.ftc7244.robotcontroller.input.Button;
import org.ftc7244.robotcontroller.input.ButtonType;

/**
 * Created by Eeshwar Laptop on 10/16/2017.
 */

@TeleOp(name = "Teleop")
public class Teleop extends LinearOpMode {
    private Westcoast robot;
    private Button leftTrigger1, dPadUp, dPadDown, rightTrigger, leftTrigger, leftBumper, aButton, bButton, panicButton, rightBumper;
    private static final double SLOW_DRIVE_COEFFICIENT = -0.5, LIFT_REST = 0.1, LIFT_RAISE = .8;

    /*
    Driver:
    Left Joystick: Drive Left
    Right Joystick: Drive Right
    Right Trigger: Slow Mode

    Operator:
    Dpad Up: Raise Intake
    Dpad Down: Lower Intake

    Right Trigger: Intake Bottom
    Left Trigger: Outtake Bottom
    Right Bumper: Intake Top
    Left Bumper: Outtake Top
    A: Intake Down
    B: Intake Up
    Y: Intake Open
     */

    @Override
    public void runOpMode() throws InterruptedException {
        robot = new Westcoast(this);
        panicButton = new Button(gamepad1, ButtonType.LEFT_BUMPER);
        leftTrigger1 = new Button(gamepad1, ButtonType.LEFT_TRIGGER);
        dPadUp = new Button(gamepad2, ButtonType.D_PAD_UP);
        dPadDown = new Button(gamepad2, ButtonType.D_PAD_DOWN);
        rightTrigger = new Button(gamepad2, ButtonType.RIGHT_TRIGGER);
        leftTrigger = new Button(gamepad2, ButtonType.LEFT_TRIGGER);
        leftBumper = new Button(gamepad2, ButtonType.LEFT_BUMPER);
        aButton = new Button(gamepad2, ButtonType.A);
        bButton = new Button(gamepad2, ButtonType.B);
        rightBumper = new Button(gamepad2, ButtonType.RIGHT_BUMPER);
        robot.init();
        waitForStart();
        robot.initServos();
        while (opModeIsActive()) {
            //Driver
            double coefficient = leftTrigger1.isPressed() ? SLOW_DRIVE_COEFFICIENT : -1;
            robot.drive(gamepad1.left_stick_y * coefficient, gamepad1.right_stick_y * coefficient);
            robot.getIntakeServo().setPosition(rightBumper.isPressed() ? 0.45 : 0.7);
            if (panicButton.isPressed())
                robot.getSpring().setPosition(0.6);
            //Operator
            if (rightTrigger.isPressed()) {
                robot.getIntakeBottom().setPower(-1);
                robot.getIntakeTop().setPower(-1);
            } else{
                robot.getIntakeBottom().setPower(leftTrigger.isPressed()?1:0);
                robot.getIntakeTop().setPower(leftBumper.isPressed()?1:0);
            }
            robot.driveIntakeVertical(aButton.isPressed()?.5:bButton.isPressed()?-.5:0);
            robot.getIntakeLift().setPower(dPadUp.isPressed()?LIFT_RAISE:dPadDown.isPressed()?-LIFT_RAISE:LIFT_REST);
        }
    }
}