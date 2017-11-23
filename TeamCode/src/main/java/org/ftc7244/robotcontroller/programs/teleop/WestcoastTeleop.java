package org.ftc7244.robotcontroller.programs.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManager;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.ftc7244.robotcontroller.hardware.Westcoast;
import org.ftc7244.robotcontroller.input.Button;
import org.ftc7244.robotcontroller.input.ButtonType;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Eeshwar Laptop on 10/16/2017.
 */

@TeleOp(name = "Relic Recovery Westcoast: Jiggle")
public class WestcoastTeleop extends OpMode {
    Westcoast robot;
    private Button leftTrigger1, dPadUp, dPadDown, rightTrigger, leftTrigger, rightBumper, leftBumper, aButton, bButton;
    private static final double SLOW_DRIVE_COEFFICIENT = 0.5, LIFT_VERTICAL_REST = 0.1, LIFT_RAISE = .8, HORIZONTAL_INTAKE_POWER = 1;
    private double VERTICLE_INTAKE_MULTIPLIER = 1;
    private static final long JIGGLE_INTERVAL_MS = 250;
    ElapsedTime elapsedTime = new ElapsedTime();


    public void init(){
        robot = new Westcoast(this);
        leftTrigger1 = new Button(gamepad1, ButtonType.LEFT_TRIGGER);
        dPadUp = new Button(gamepad2, ButtonType.D_PAD_UP);
        dPadDown = new Button(gamepad2, ButtonType.D_PAD_DOWN);
        rightTrigger = new Button(gamepad2, ButtonType.RIGHT_TRIGGER);
        leftTrigger = new Button(gamepad2, ButtonType.LEFT_TRIGGER);
        rightBumper = new Button(gamepad2, ButtonType.RIGHT_BUMPER);
        leftBumper = new Button(gamepad2, ButtonType.LEFT_BUMPER);
        aButton = new Button(gamepad2, ButtonType.A);
        bButton = new Button(gamepad2, ButtonType.B);
        robot.init();
    }
    /*
    Driver:
    Left Joystick: Drive Left
    Right Joystick: Drive Right
    Right Trigger: Slow Mode

    Operator:
    Dpad Up: Raise Intake
    Dpad Down: Lower Intake

    Right Trigger: Intake Bottom (Jiggle)
    Left Trigger: Outtake Bottom
    Right Bumper: Intake Top (Jiggle)
    Left Bumper: Outtake Top
    A: Intake Up
    B: Intake Down
     */
    @Override
    public void loop(){
        double coefficient = leftTrigger1.isPressed()?SLOW_DRIVE_COEFFICIENT:1;
        robot.drive(-gamepad1.left_stick_y*coefficient, -gamepad1.right_stick_y*coefficient);
        //Drive Code
        boolean horizontalRunning = false;
        if(rightTrigger.isPressed()){
            horizontalRunning = true;
            robot.getIntakeTopLeft().setPower(0.5 * VERTICLE_INTAKE_MULTIPLIER);
            robot.getIntakeTopRight().setPower(0.5 * VERTICLE_INTAKE_MULTIPLIER);
            if(elapsedTime.milliseconds() >= JIGGLE_INTERVAL_MS){
                VERTICLE_INTAKE_MULTIPLIER = VERTICLE_INTAKE_MULTIPLIER * -1;
                elapsedTime.reset();
            }
            robot.getIntakeTopLeft().setPower(0);
            robot.getIntakeTopRight().setPower(0);
            robot.getIntakeBottom().setPower(-1);
            telemetry.addData("ElapsedTime", elapsedTime.milliseconds());
            telemetry.update();
        }
        else if(leftTrigger.isPressed()){
            horizontalRunning = true;
            robot.getIntakeBottom().setPower(1);
        }
        else {
            robot.getIntakeBottom().setPower(0);
        }
        if(leftBumper.isPressed()){
            horizontalRunning = true;
            robot.getIntakeBottomLeft().setPower(0);
            robot.getIntakeBottomRight().setPower(0);
            robot.getIntakeTop().setPower(1);
        }
        else if(rightBumper.isPressed()){
            horizontalRunning = true;
            robot.getIntakeTop().setPower(-1);
        }
        else {
            robot.getIntakeTop().setPower(0);
        }
        if(!horizontalRunning){
            if(aButton.isPressed()){
                robot.getIntakeBottomRight().setPower(.5);
                robot.getIntakeTopRight().setPower(.5);
                robot.getIntakeBottomLeft().setPower(.5);
                robot.getIntakeTopLeft().setPower(.5);
            }
            else if(bButton.isPressed()){
                robot.getIntakeBottomRight().setPower(-.5);
                robot.getIntakeTopRight().setPower(-.5);
                robot.getIntakeBottomLeft().setPower(-.5);
                robot.getIntakeTopLeft().setPower(-.5);
            }
            else {
                robot.getIntakeBottomRight().setPower(0);
                robot.getIntakeTopRight().setPower(0);
                robot.getIntakeBottomLeft().setPower(0);
                robot.getIntakeTopLeft().setPower(0);
            }
        }

        if(dPadUp.isPressed()){
            robot.getIntakeVertical().setPower(LIFT_RAISE);
        }
        else if(dPadDown.isPressed()){
            robot.getIntakeVertical().setPower(-LIFT_RAISE);
        }
        else {
            robot.getIntakeVertical().setPower(LIFT_VERTICAL_REST);
        }
    }

    @Override
    public void stop() {
        //done
    }
}