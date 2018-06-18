package org.ftc7244.robotcontroller.programs.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.ftc7244.robotcontroller.hardware.Hardware;
import org.ftc7244.robotcontroller.hardware.Westcoast;
import org.ftc7244.robotcontroller.input.Button;
import org.ftc7244.robotcontroller.input.ButtonType;
import org.ftc7244.robotcontroller.input.PressButton;

/**
 * Created on 10/16/2017.
 */

@TeleOp(name = "Teleop Lander")
public class TeleopLander extends LinearOpMode {
    private Westcoast robot;
    private Button driverRightBumper, dPadUp, dPadDown, rightTrigger, leftTrigger, leftBumper, bButton, yButton, driverLeftBumper, rightBumper, driveYButton, driveXButton;
    private PressButton aButton, driveRightTrigger;
    private static final double SLOW_DRIVE_COEFFICIENT = -0.5, ACTION_BUFFER = 200, INTAKE_PUSHER_OUT = 0.69;
    private boolean raised;
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
        driverRightBumper = new PressButton(gamepad1, ButtonType.RIGHT_BUMPER);
        dPadUp = new Button(gamepad2, ButtonType.D_PAD_UP);
        dPadDown = new Button(gamepad2, ButtonType.D_PAD_DOWN);
        rightTrigger = new Button(gamepad2, ButtonType.RIGHT_TRIGGER);
        leftTrigger = new Button(gamepad2, ButtonType.LEFT_TRIGGER);
        leftBumper = new Button(gamepad2, ButtonType.LEFT_BUMPER);
        bButton = new Button(gamepad2, ButtonType.B);
        yButton = new Button(gamepad2, ButtonType.Y);
        rightBumper = new Button(gamepad2, ButtonType.RIGHT_BUMPER);
        aButton = new PressButton(gamepad2, ButtonType.A, true);
        driveXButton = new PressButton(gamepad1, ButtonType.X);
        driveRightTrigger = new PressButton(gamepad1, ButtonType.RIGHT_TRIGGER);
        driveYButton = new PressButton(gamepad1, ButtonType.Y);
        raised = false;
        robot.init();
        robot.getIntakeLift().setPower(0.1);
        waitForStart();
        Hardware.resetMotors(robot.getIntakeLift());
        robot.initServos();
        robot.getSpring().setPosition(0.5);
        while (opModeIsActive()) {
            //Driver
            double coefficient = driverRightBumper.isPressed()?SLOW_DRIVE_COEFFICIENT:driverLeftBumper.isPressed() ? SLOW_DRIVE_COEFFICIENT : -1;
            robot.drive(gamepad1.left_stick_y*coefficient, gamepad1.right_stick_y*coefficient);


            //Operator
            /**Relic Arm Control*/
            robot.getRelicSpool().setPower(gamepad1.left_trigger-gamepad1.right_trigger);
            robot.getRelicWrist().setPosition(gamepad2.left_stick_y < -0.1 ? 0.1 : gamepad2.left_stick_y > 0.1 ? 0.6 : robot.getRelicWrist().getPosition());
            robot.getRelicFinger().setPosition(aButton.isPressed() ? 0.375 : 0.7);

            /**Glyph Control*/
            robot.getIntakeServo().setPosition(rightBumper.isPressed() ? 0.75 : 0.2);
            robot.driveIntakeVertical(bButton.isPressed() ? .5 : yButton.isPressed() ? -.5 : 0);
            if(dPadUp.isPressed() || dPadDown.isPressed()){
                robot.getIntakeLift().setPower(dPadDown.isPressed() ? -1 : 1);
            }

            if (rightTrigger.isPressed()) {
                robot.getIntakeBottom().setPower(-1);
                robot.getIntakeTop().setPower(-1);
                robot.getIntakePusher().setPosition(0.5);
                robot.getIntakeBottomRight().setPower(0);
                robot.getIntakeBottomLeft().setPower(0);
            }
            else {
                if (leftTrigger.isPressed()) {
                    robot.getIntakeBottom().setPower(1);
                    robot.getIntakePusher().setPosition(INTAKE_PUSHER_OUT);
                } else {
                    robot.getIntakeBottom().setPower(0);
                }
                if(leftBumper.isPressed()) {
                    robot.getIntakeBottomRight().setPower(0.5);
                    robot.getIntakeBottomLeft().setPower(0.5);
                    robot.getIntakeTop().setPower(1);
                }
                else{
                    robot.getIntakeTop().setPower(0);
                    robot.getIntakeBottomRight().setPower(0);
                    robot.getIntakeBottomLeft().setPower(0);
                }
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
                        if(!bButton.isPressed() && !raised){
                            robot.getIntakeTopRight().setPower(0.5);
                            robot.getIntakeTopLeft().setPower(0.5);
                            sleep(250);
                            robot.getIntakeTopRight().setPower(0);
                            robot.getIntakeTopLeft().setPower(0);
                            raised = true;
                        }
                    }
                } else { //And a block is NOT in the intake...
                    raised = false;
                    if (robot.getIntakeLift().getCurrentPosition() < 455 && !leftTrigger.isPressed()) {//And the intake lift is under it's holding range...
                        robot.getIntakeLift().setPower((455 - robot.getIntakeLift().getCurrentPosition()) / 455.0); //Run the lift using a proportional equation
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