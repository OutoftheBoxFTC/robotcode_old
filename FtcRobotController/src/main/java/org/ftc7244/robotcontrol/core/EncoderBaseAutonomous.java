package org.ftc7244.robotcontrol.core;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;

import org.ftc7244.robotcontrol.WestcoastHardware;

/**
 * This code failed because the robot frame is too skewed to handle the encoders
 * Thus there was not much of an effect for using encoders.
 */
public abstract class EncoderBaseAutonomous extends LinearOpMode {

    private final static double COUNTS_PER_INCH = 560 / (Math.PI * 3);

    protected WestcoastHardware robot = new WestcoastHardware(this);

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
    public void encoderDrive(double speed, double leftInches, double rightInches, double timeoutS) throws InterruptedException {
        // Ensure that the opmode is still active
        if (!opModeIsActive()) {
            telemetry.addLine("Tried to use encoder while stationary");
            telemetry.update();
            return;
        }

        // Determine new target position, and pass to motor controller
        int targetLeft = (int) (robot.getDriveLeft().getCurrentPosition() + Math.round(leftInches * COUNTS_PER_INCH));
        int targetRight = (int) (robot.getDriveRight().getCurrentPosition() + Math.round(rightInches * COUNTS_PER_INCH));
        robot.getDriveLeft().setTargetPosition(targetLeft);
        robot.getDriveRight().setTargetPosition(targetRight);

        // Turn On RUN_TO_POSITION
        robot.getDriveLeft().setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.getDriveRight().setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // reset the timeout time and start motion.
        robot.getDriveLeft().setPower(Math.abs(speed));
        robot.getDriveRight().setPower(Math.abs(speed));

        ElapsedTime runtime = new ElapsedTime();
        // keep looping while we are still active, and there is time left, and both motors are running.
        while (Math.abs(robot.getDriveLeft().getCurrentPosition()) < Math.abs(targetLeft) || Math.abs(robot.getDriveRight().getCurrentPosition()) < Math.abs(targetRight)) {
            telemetry.addData("Path1",  "Running to %7d :%7d", targetLeft,  targetRight);
            telemetry.addData("Path2",  "Running at %7d :%7d",
                    robot.getDriveLeft().getCurrentPosition(),
                    robot.getDriveRight().getCurrentPosition());
            telemetry.update();

            idle();
        }

        // Stop all motion;
        robot.getDriveLeft().setPower(0);
        robot.getDriveRight().setPower(0);

        // Turn off RUN_TO_POSITION

        robot.getDriveLeft().setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.getDriveRight().setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        sleep(250);
    }

    /*public void encoderDrive(double speed,
                             double leftInches, double rightInches,
                             double timeoutS) throws InterruptedException {
        
        int newLeftTarget;
        int newRightTarget;

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newLeftTarget = robot.getDriveLeft().getCurrentPosition() + (int)(leftInches * COUNTS_PER_INCH);
            newRightTarget = robot.getDriveRight().getCurrentPosition() + (int)(rightInches * COUNTS_PER_INCH);
            robot.getDriveLeft().setTargetPosition(newLeftTarget);
            robot.getDriveRight().setTargetPosition(newRightTarget);

            // Turn On RUN_TO_POSITION
            robot.getDriveLeft().setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.getDriveRight().setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            ElapsedTime runtime = new ElapsedTime();
            robot.getDriveLeft().setPower(Math.abs(speed));
            robot.getDriveRight().setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (robot.getDriveLeft().isBusy() && robot.getDriveRight().isBusy())) {

                // Display it for the driver.
                telemetry.addData("Path1",  "Running to %7d :%7d", newLeftTarget,  newRightTarget);
                telemetry.addData("Path2",  "Running at %7d :%7d",
                        robot.getDriveLeft().getCurrentPosition(),
                        robot.getDriveRight().getCurrentPosition());
                telemetry.update();

                // Allow time for other processes to run.
                idle();
            }

            // Stop all motion;
            robot.getDriveLeft().setPower(0);
            robot.getDriveRight().setPower(0);

            // Turn off RUN_TO_POSITION
            robot.getDriveLeft().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.getDriveRight().setMode(DcMotor.RunMode.RUN_USING_ENCODER);

              sleep(250);   // optional pause after each move
        }
    }*/
}
