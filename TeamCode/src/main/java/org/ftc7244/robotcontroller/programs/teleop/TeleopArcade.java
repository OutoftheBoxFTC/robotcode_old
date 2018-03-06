package org.ftc7244.robotcontroller.programs.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.ftc7244.robotcontroller.hardware.Westcoast;
import org.ftc7244.robotcontroller.input.Button;
import org.ftc7244.robotcontroller.input.ButtonType;
import org.ftc7244.robotcontroller.input.PressButton;

/**
 * Created by ftc7244 on 2/19/2018.
 */
@TeleOp
public class TeleopArcade extends LinearOpMode {
    private Westcoast robot;
    private Button driveLeftTrigger, dPadUp, dPadDown, rightTrigger, leftTrigger, leftBumper, bButton, yButton, driverLeftBumper, rightBumper;
    private PressButton aButton, driveRightTrigger;
    private static final double SLOW_DRIVE_COEFFICIENT = 0.6, LIFT_REST = 0.1, LIFT_RAISE = .8;
    private double leftdrive,rightdrive, leftforward, rightforward;

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
        waitForStart();
        robot.initServos();
        while (opModeIsActive()) {
            //Driver
            double coefficient = driveLeftTrigger.isPressed() ? SLOW_DRIVE_COEFFICIENT : 1;
            if(gamepad1.left_stick_y > 0.1 || gamepad1.left_stick_y < -0.1){
                leftforward = gamepad1.left_stick_y;
                rightforward = gamepad1.left_stick_y;
            }
            if(gamepad1.right_stick_x > 0.1 && leftforward != 0){
                leftdrive = leftforward + gamepad1.right_stick_x;
                rightforward = rightdrive - gamepad1.right_stick_x;
            }else if(gamepad1.right_stick_x < -0.1 && leftforward != 0){
                leftdrive = leftforward + gamepad1.right_stick_x;
                rightdrive = rightforward - gamepad1.right_stick_x;
            }else if(gamepad1.right_stick_x > 0.1 || gamepad1.right_stick_x < -0.1){
                leftdrive = gamepad1.right_stick_x;
                rightdrive = -gamepad1.right_stick_x;
            } else{
                leftdrive = leftforward;
                rightdrive = rightforward;
            }
            robot.drive(leftdrive * coefficient, rightdrive * coefficient);

            robot.getIntakeServo().setPosition(rightBumper.isPressed() ? 0.8: .2);
            if (driverLeftBumper.isPressed())
                robot.getSpring().setPosition(0.5);

            //Operator
            if (rightTrigger.isPressed()) {
                robot.getIntakeBottom().setPower(-1);
                robot.getIntakeTop().setPower(-1);
            } else{
                robot.getIntakeBottom().setPower(leftTrigger.isPressed()?1:0);
                robot.getIntakeTop().setPower(leftBumper.isPressed()?1:0);
            }

            robot.driveIntakeVertical(bButton.isPressed()?.5:yButton.isPressed()?-.5:0);
            robot.getIntakeLift().setPower(dPadUp.isPressed()?LIFT_RAISE:dPadDown.isPressed()?-LIFT_RAISE - LIFT_REST:LIFT_REST);
            if(robot.getRelicSpool().getCurrentPosition() < -1875)
                robot.getRelicSpool().setPower(gamepad2.left_stick_y < -0.1 ? 0 : gamepad2.left_stick_y);
            else if(robot.getRelicSpool().getCurrentPosition() > 50)
                robot.getRelicSpool().setPower(gamepad2.left_stick_y > 0.1 ? 0 : gamepad2.left_stick_y);
            else
                robot.getRelicSpool().setPower(gamepad2.left_stick_y);
            //closer it is to 1, the higher it goes
            robot.getRelicWrist().setPosition(gamepad2.right_stick_y<-0.1?0.6:gamepad2.right_stick_y>0.1?0.1:robot.getRelicWrist().getPosition());
            //The closer you get to 0, the tighter it gets
            robot.getRelicFinger().setPosition(aButton.isPressed()?0.375:0.7);
            if(gamepad2.left_stick_y > -0.1 && gamepad2.left_stick_y < 0.1){
                telemetry.addData("Spool", robot.getRelicSpool().getCurrentPosition());
                telemetry.update();
            }
        }
    }
}
