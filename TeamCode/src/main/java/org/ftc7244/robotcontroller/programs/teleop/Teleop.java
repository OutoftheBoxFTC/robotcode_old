package org.ftc7244.robotcontroller.programs.teleop;

import android.media.MediaPlayer;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.ftc7244.robotcontroller.hardware.Westcoast;
import org.ftc7244.robotcontroller.input.Button;
import org.ftc7244.robotcontroller.input.ButtonType;

import java.util.ArrayList;

/**
 * Created by Eeshwar Laptop on 10/16/2017.
 */

@TeleOp(name = "Teleop")
public class Teleop extends LinearOpMode {
    private Westcoast robot;
    private Button leftTrigger1, dPadUp, dPadDown, rightTrigger, leftTrigger, rightBumper, leftBumper, aButton, bButton, yButton, panicButton, driverAButton, driverBButton, driverXButton;
    private static final double SLOW_DRIVE_COEFFICIENT = 0.5, LIFT_REST = 0.1, LIFT_RAISE = .8;
    private double VERTICAL_INTAKE_MULTIPLIER = 1, VERTICAL_INTAKE_STOP = 1;
    private static final long JIGGLE_INTERVAL_MS = 250, JIGGLE_INTERVAL_REST = 1000;
    ElapsedTime elapsedTime = new ElapsedTime();
    private ArrayList<MediaPlayer> sounds;

    /*
    Driver:
    Left Joystick: Drive Left
    Right Joystick: Drive Right
    Right Trigger: Slow Mode
    A: NFL song
    B: Sad song
    X: Clear song

    Operator:
    Dpad Up: Raise Intake
    Dpad Down: Lower Intake

    Right Trigger: Intake Bottom (Jiggle)
    Left Trigger: Outtake Bottom
    Right Bumper: Intake Top (Jiggle)
    Left Bumper: Outtake Top
    A: Intake Up
    B: Intake Down
    Y: Intake Open
     */

    @Override
    public void runOpMode() throws InterruptedException {
        //sounds = new ArrayList<>(Arrays.asList(MediaPlayer.create(hardwareMap.appContext, R.raw.nfltheme), MediaPlayer.create(hardwareMap.appContext, R.raw.sadviolin)));
        robot = new Westcoast(this);
        driverAButton = new Button(gamepad1, ButtonType.A);
        driverBButton = new Button(gamepad1, ButtonType.B);
        driverXButton = new Button(gamepad1, ButtonType.X);
        panicButton = new Button(gamepad1, ButtonType.LEFT_BUMPER);
        leftTrigger1 = new Button(gamepad1, ButtonType.LEFT_TRIGGER);
        dPadUp = new Button(gamepad2, ButtonType.D_PAD_UP);
        dPadDown = new Button(gamepad2, ButtonType.D_PAD_DOWN);
        rightTrigger = new Button(gamepad2, ButtonType.RIGHT_TRIGGER);
        leftTrigger = new Button(gamepad2, ButtonType.LEFT_TRIGGER);
        rightBumper = new Button(gamepad2, ButtonType.RIGHT_BUMPER);
        leftBumper = new Button(gamepad2, ButtonType.LEFT_BUMPER);
        aButton = new Button(gamepad2, ButtonType.A);
        bButton = new Button(gamepad2, ButtonType.B);
        yButton = new Button(gamepad2, ButtonType.Y);
        robot.init();
        waitForStart();
        robot.initServos();

        while (opModeIsActive()) {
            double coefficient = leftTrigger1.isPressed() ? SLOW_DRIVE_COEFFICIENT : 1;
            robot.drive(-gamepad1.left_stick_y * coefficient, -gamepad1.right_stick_y * coefficient);
            robot.getIntakeServo().setPosition(yButton.isPressed() ? 1 : 0.05);
            if (panicButton.isPressed()) {
                robot.getSpring().setPosition(0.6);
            }
            //Drive Code
            boolean horizontalRunning = false;
            if (rightTrigger.isPressed()) {
                horizontalRunning = true;
                robot.getIntakeTopLeft().setPower(0.5 * VERTICAL_INTAKE_MULTIPLIER * VERTICAL_INTAKE_STOP);
                robot.getIntakeTopRight().setPower(0.5 * VERTICAL_INTAKE_MULTIPLIER * VERTICAL_INTAKE_STOP);
                robot.getIntakeBottomLeft().setPower(0.25 * VERTICAL_INTAKE_MULTIPLIER * VERTICAL_INTAKE_STOP);
                robot.getIntakeBottomRight().setPower(0.25 * VERTICAL_INTAKE_MULTIPLIER * VERTICAL_INTAKE_STOP);
                if (elapsedTime.milliseconds() >= JIGGLE_INTERVAL_MS) {
                    VERTICAL_INTAKE_STOP = 0;
                }
                if (elapsedTime.milliseconds() >= JIGGLE_INTERVAL_REST) {
                    VERTICAL_INTAKE_STOP = 1;
                    elapsedTime.reset();
                }
                robot.getIntakeTopLeft().setPower(0);
                robot.getIntakeTopRight().setPower(0);
                robot.getIntakeBottom().setPower(-1);
            } else if (leftTrigger.isPressed()) {
                horizontalRunning = true;
                robot.getIntakeBottom().setPower(1);
            } else {
                robot.getIntakeBottom().setPower(0);
            }
            if (leftBumper.isPressed()) {
                horizontalRunning = true;
                robot.getIntakeBottomLeft().setPower(0);
                robot.getIntakeBottomRight().setPower(0);
                robot.getIntakeTop().setPower(1);
            } else if (rightBumper.isPressed()) {
                horizontalRunning = true;
                robot.getIntakeTop().setPower(-1);
            } else {
                robot.getIntakeTop().setPower(0);
            }
            if (!horizontalRunning) {
                if (aButton.isPressed()) {
                    robot.getIntakeBottomRight().setPower(.5);
                    robot.getIntakeTopRight().setPower(.5);
                    robot.getIntakeBottomLeft().setPower(.5);
                    robot.getIntakeTopLeft().setPower(.5);
                } else if (bButton.isPressed()) {
                    robot.getIntakeBottomRight().setPower(-.5);
                    robot.getIntakeTopRight().setPower(-.5);
                    robot.getIntakeBottomLeft().setPower(-.5);
                    robot.getIntakeTopLeft().setPower(-.5);
                } else {
                    robot.getIntakeBottomRight().setPower(0);
                    robot.getIntakeTopRight().setPower(0);
                    robot.getIntakeBottomLeft().setPower(0);
                    robot.getIntakeTopLeft().setPower(0);
                }
            }

            if (dPadUp.isPressed()) {
                robot.getIntakeVertical().setPower(LIFT_RAISE);
            } else if (dPadDown.isPressed()) {
                robot.getIntakeVertical().setPower(-LIFT_RAISE);
            } else {
                robot.getIntakeVertical().setPower(LIFT_REST);
            }

            /*if (driverAButton.isPressed() && driverAButton.isUpdated()) {
                sounds.get(0).start();
                sounds.get(1).stop();
            } else if (driverBButton.isPressed() && driverBButton.isUpdated()) {
                sounds.get(0).stop();
                sounds.get(1).start();
            } else if (driverXButton.isPressed() && driverXButton.isUpdated()) {
                sounds.get(0).stop();
                sounds.get(1).stop();
            }*/
        }
        for(MediaPlayer sound : sounds){
            sound.stop();
        }
    }
}