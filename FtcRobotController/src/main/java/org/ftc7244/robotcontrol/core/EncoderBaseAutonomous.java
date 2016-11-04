package org.ftc7244.robotcontrol.core;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.RobotLog;

import org.ftc7244.robotcontrol.Westcoast;

/**
 * This code failed because the robot frame is too skewed to handle the encoders
 * Thus there was not much of an effect for using encoders.
 */
public abstract class EncoderBaseAutonomous extends LinearOpMode {

    private final static double COUNTS_PER_INCH = 1120 / (Math.PI * 3);
    private final static int ENCODER_THRESHOLD = 100;

    protected Westcoast robot = new Westcoast(this);

    @Override
    public void runOpMode() throws InterruptedException {
        robot.init();

        robot.getDriveLeft().setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.getDriveRight().setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        initialize();

        idle();
        waitForStart();

        run();
    }

    public void initialize() {}

    public abstract void run() throws InterruptedException;

    /**
     * Move the robot a certain distance and once it reaches a distance or reaches a timeout then stop the robot
     * @param speed the power of the robot from 0 to 1
     * @param leftInches how many inches the encoder moves in inches
     * @param rightInches how many inches the encoder moves in inches
     * @param timeoutS in seconds how long till a fail-safe is ran
     * @throws InterruptedException
     */
    public void moveDistance(double speed, double leftInches, double rightInches, double timeoutS) throws InterruptedException {
        // Ensure that the opmode is still active
        if (!opModeIsActive()) {
            telemetry.addLine("Tried to use encoder while stationary");
            telemetry.update();
            return;
        }

        //Initialize values
        DcMotor left = robot.getDriveLeft(), right = robot.getDriveRight();
        int directionFactor = (int) (speed / Math.abs(speed)),
            targetLeft = (int) Math.round(leftInches * COUNTS_PER_INCH) * directionFactor,
            targetRight = -1 *(int) Math.round(rightInches * COUNTS_PER_INCH) * directionFactor;

        resetMotors(right, left);

        //Setup the encoders and get them to move
        left.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        right.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        left.setTargetPosition(targetLeft);
        right.setTargetPosition(targetRight);
        right.setPower(speed);
        left.setPower(speed);

        // keep looping while we are still active, and there is time left, and both motors are running.
        while (left.getPower() != 0 || right.getPower() != 0) {
            stopWhenComplete(left, targetLeft, ENCODER_THRESHOLD);
            stopWhenComplete(right, targetRight, ENCODER_THRESHOLD);

            idle();
        }

        // Stop all motion
        left.setPower(0);
        right.setPower(0);

        // Turn off RUN_TO_POSITION
        resetMotors(left, right);
        sleep(500);
    }

    private void resetMotors(DcMotor... motors) {
        boolean notReset = true;
        while (notReset) {
            boolean allReset = true;
            for (DcMotor motor : motors) {
                if (motor.getCurrentPosition() == 0) {
                    continue;
                }
                allReset = false;
                motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            }
            notReset = !allReset;
        }
    }

    private boolean stopWhenComplete(DcMotor motor, int target, int threshold) {
        if (Math.abs(target - motor.getCurrentPosition()) >= threshold) {
            return false;
        }

        motor.setPower(0);
        return true;
    }

}
