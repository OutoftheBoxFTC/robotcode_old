package org.ftc7244.robotcontroller.programs.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.ftc7244.robotcontroller.hardware.Westcoast;
import org.ftc7244.robotcontroller.input.Button;
import org.ftc7244.robotcontroller.input.ButtonType;
import org.ftc7244.robotcontroller.input.PressButton;

/**
 * Created by Eeshwar Laptop on 10/16/2017.
 */

@TeleOp(name = "Teleop")
public class Teleop extends LinearOpMode {
    private Westcoast robot;
    private Button leftTrigger1, dPadUp, dPadDown, rightTrigger, leftTrigger, leftBumper, bButton, yButton, driverLeftBumper, rightBumper, dPadRight;
    private PressButton aButton;
    private static final double SLOW_DRIVE_COEFFICIENT = -0.5, LIFT_REST = 0.1, LIFT_RAISE = .8;

    /**
    Driver:
        Left Joystick Y: Drive Left
        Right Joystick Y: Drive Right
        Right Trigger: Slow Mode
        Left Bumper: Release Intake Spring

    Operator:
        Dpad Up: Raise Intake
        Dpad Down: Lower Intake

        Right Trigger: Intake Both
        Left Trigger: Outtake Bottom
        Left Bumper: Outtake Top

        B: Intake Up
        Y: Intake Down
        Right Bumper: Intake Open

        Left Joystick Y: Extend/Retract Relic Arm
        Right Joystick Y: Raise/Lower Relic Claw
        A: Open Relic Arm
     */

    @Override
    public void runOpMode() throws InterruptedException {
        robot = new Westcoast(this);

        driverLeftBumper = new Button(gamepad1, ButtonType.LEFT_BUMPER);
        leftTrigger1 = new Button(gamepad1, ButtonType.LEFT_TRIGGER);
        dPadUp = new Button(gamepad2, ButtonType.D_PAD_UP);
        dPadDown = new Button(gamepad2, ButtonType.D_PAD_DOWN);
        rightTrigger = new Button(gamepad2, ButtonType.RIGHT_TRIGGER);
        leftTrigger = new Button(gamepad2, ButtonType.LEFT_TRIGGER);
        leftBumper = new Button(gamepad2, ButtonType.LEFT_BUMPER);
        bButton = new Button(gamepad2, ButtonType.B);
        yButton = new Button(gamepad2, ButtonType.Y);
        rightBumper = new Button(gamepad2, ButtonType.RIGHT_BUMPER);
        aButton = new PressButton(gamepad2, ButtonType.A);
        dPadRight = new Button(gamepad2, ButtonType.D_PAD_RIGHT);
        robot.init();
        waitForStart();
        robot.initServos();
        while (opModeIsActive()) {
            //Driver
            double coefficient = leftTrigger1.isPressed() ? SLOW_DRIVE_COEFFICIENT : -1;
            robot.drive(gamepad1.left_stick_y * coefficient, gamepad1.right_stick_y * coefficient);
            if (driverLeftBumper.isPressed())
                robot.getSpring().setPosition(0.6);
            //Operator
            robot.getIntakeServo().setPosition(rightBumper.isPressed() ? 0.5 : 0.9);
            if (rightTrigger.isPressed()) {
                robot.getIntakeBottom().setPower(-1);
                robot.getIntakeTop().setPower(-1);
            } else{
                robot.getIntakeBottom().setPower(leftTrigger.isPressed()?1:0);
                robot.getIntakeTop().setPower(leftBumper.isPressed()?1:0);
            }
            robot.driveIntakeVertical(bButton.isPressed()?.5:yButton.isPressed()?-.5:0);
            robot.getIntakeLift().setPower(dPadUp.isPressed()?LIFT_RAISE:dPadDown.isPressed()?-LIFT_RAISE - LIFT_REST:LIFT_REST);

            robot.getRelicSpool().setPower(gamepad2.left_stick_y);
            robot.getRelicArm().setPosition(gamepad2.right_stick_y<-0.1?0:gamepad2.right_stick_y>0.1?1:robot.getRelicArm().getPosition());
            robot.getRelicClaw().setPosition(aButton.isPressed()?0:1);
        }
    }
}