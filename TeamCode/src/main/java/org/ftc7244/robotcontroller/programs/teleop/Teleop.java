package org.ftc7244.robotcontroller.programs.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.ftc7244.robotcontroller.hardware.Westcoast;
import org.ftc7244.robotcontroller.input.Button;
import org.ftc7244.robotcontroller.input.ButtonType;
import org.ftc7244.robotcontroller.input.PressButton;

/**
 * Created on 10/16/2017.
 */

@TeleOp(name = "Teleop")
public class Teleop extends LinearOpMode {
    private Westcoast robot;
    private Button driveLeftTrigger, dPadUp, dPadDown, rightTrigger, leftTrigger, leftBumper, bButton, yButton, driverLeftBumper, rightBumper;
    private PressButton aButton, driveRightTrigger;
    private static final double SLOW_DRIVE_COEFFICIENT = -0.5, ACTION_BUFFER = 200;

    /**
    Driver:
        Left Joystick: Drive Left
        Right Joystick: Drive Right
        Right Trigger: Slow Mode
        Left Bumper: Release Intake Spring

    Operator:
        Dpad Up: Raise Intake
        Dpad Down: Lower Intake

        Right Trigger: Intake Both
        Right Bumper: Intake Top
        Left Bumper: Outtake Top
        A: Intake Down
        B: Intake Up
        Y: Intake Open
     */

    @Override
    public void runOpMode() throws InterruptedException {
        robot = new Westcoast(this);
        driverLeftBumper = new Button(gamepad1, ButtonType.LEFT_BUMPER);
        driveLeftTrigger = new Button(gamepad1, ButtonType.LEFT_TRIGGER);
        dPadUp = new Button(gamepad2, ButtonType.D_PAD_UP);
        dPadDown = new Button(gamepad2, ButtonType.D_PAD_DOWN);
        rightTrigger = new Button(gamepad2, ButtonType.RIGHT_TRIGGER);
        leftTrigger = new Button(gamepad2, ButtonType.LEFT_TRIGGER);
        leftBumper = new Button(gamepad2, ButtonType.LEFT_BUMPER);
        bButton = new Button(gamepad2, ButtonType.B);
        yButton = new Button(gamepad2, ButtonType.Y);
        rightBumper = new Button(gamepad2, ButtonType.RIGHT_BUMPER);
        aButton = new PressButton(gamepad2, ButtonType.A);
        driveRightTrigger = new PressButton(gamepad1, ButtonType.RIGHT_TRIGGER);
        robot.init();
        robot.getIntakeLift().setPower(0.1);
        waitForStart();
        robot.initServos();
        while (opModeIsActive()) {
            //Driver
            double coefficient = driveLeftTrigger.isPressed() ? SLOW_DRIVE_COEFFICIENT : -1;
            if (driveRightTrigger.isPressed())
                robot.drive(gamepad1.right_stick_x * coefficient, gamepad1.left_stick_x * coefficient);
            else
                robot.drive(gamepad1.left_stick_y * coefficient, gamepad1.right_stick_y * coefficient);

            if (driverLeftBumper.isPressed())
                robot.getSpring().setPosition(0.5);

            //Operator
            /**Relic Arm Control*/
            if (robot.getRelicSpool().getCurrentPosition() < Westcoast.RELIC_SPOOL_MIN)
                robot.getRelicSpool().setPower(gamepad2.left_stick_y < -0.1 ? 0 : gamepad2.left_stick_y);
            else if (robot.getRelicSpool().getCurrentPosition() > Westcoast.RELIC_SPOOL_MAX)
                robot.getRelicSpool().setPower(gamepad2.left_stick_y > 0.1 ? 0 : gamepad2.left_stick_y);
            else
                robot.getRelicSpool().setPower(gamepad2.left_stick_y);
            robot.getRelicWrist().setPosition(gamepad2.right_stick_y < -0.1 ? 0.6 : gamepad2.right_stick_y > 0.1 ? 0.1 : robot.getRelicWrist().getPosition());
            robot.getRelicFinger().setPosition(aButton.isPressed() ? 0.375 : 0.7);

            /**Glyph Control*/
            robot.getIntakeServo().setPosition(rightBumper.isPressed() ? 0.8 : 0.2);
            robot.driveIntakeVertical(bButton.isPressed() ? .5 : yButton.isPressed() ? -.5 : 0);
            if(dPadUp.isPressed() || dPadDown.isPressed()){
                robot.getIntakeLift().setPower(dPadDown.isPressed() ? -1 : 1);
            }
            if (rightTrigger.isPressed()) {
                robot.getIntakeBottom().setPower(-1);
                robot.getIntakeTop().setPower(-1);
            } else if (leftTrigger.isPressed()) {
                robot.getIntakeTop().setPower(1);
                robot.getIntakeBottom().setPower(1);
                robot.getIntakeTopLeft().setPower(-0.5);
                robot.getIntakeTopRight().setPower(-0.5);
            } else {
                robot.getIntakeTop().setPower(0);
                robot.getIntakeBottom().setPower(0);
                robot.getIntakeTopLeft().setPower(0);
                robot.getIntakeTopRight().setPower(0);
            }
            if (leftBumper.isPressed()) {
                robot.getIntakeTop().setPower(1);
            } else if (leftBumper.isPressed() && !rightTrigger.isPressed()) {
                robot.getIntakeTop().setPower(0);
            }
            if(!leftBumper.isPressed() && !leftTrigger.isPressed() && !rightTrigger.isPressed()){
                robot.getIntakeTop().setPower(0);
            }
            /**Intake lift Control*/
            if (!dPadDown.isPressed() && !dPadUp.isPressed()) {//If neither of the buttons are pressed...
                if (robot.getBottomIntakeSwitch().getVoltage() > 0.5) { //And a block is in the intake...
                    if (robot.getIntakeLift().getCurrentPosition() > 100 && robot.getIntakeLift().getCurrentPosition() < 700) { //And the intake is in the home range...
                        robot.getIntakeLift().setPower(-0.8);//run the intake down
                    } else { //And it is not in the home range...
                        robot.getIntakeLift().setPower(0.2); //hold the intake at it's position
                    }
                } else { //And a block is NOT in the intake...
                    if (robot.getIntakeLift().getCurrentPosition() < 500 && !leftTrigger.isPressed()) {//And the intake lift is under it's holding range...
                        robot.getIntakeLift().setPower((500 - robot.getIntakeLift().getCurrentPosition()) / 500.0); //Run the lift using a proportional equation
                    } else { //And the intake lift is over it's holding range...
                        robot.getIntakeLift().setPower(0.2); //Hold the intake at it's position

                    }
                }
            }
            robot.getJewelHorizontal().setPosition(robot.getBottomIntakeSwitch().getVoltage() > 0.5 ? 0.45 : 0.75);
            telemetry.addData("Lift", robot.getIntakeLift().getCurrentPosition());
            telemetry.update();
        }
    }
}