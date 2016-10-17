package org.ftc7244.robotcontrol;

import android.hardware.Sensor;
import android.hardware.SensorManager;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@Deprecated
/**
 * This code failed because the robot frame is too sqewed to handle the encoders
 * Thus there was not much of an effect for using encoders.
 */
public class EncoderBaseAutonomous extends LinearOpMode {

    private final static int COUNTS_PER_INCH = 89;

    protected WestcoastHardware robot = new WestcoastHardware();

    @Override
    public void runOpMode() throws InterruptedException {
        // Grab the sensor manager by reading the services of the hardware map
        //mSensorManager = (SensorManager) hardwareMap.appContext.getSystemService(Context.SENSOR_SERVICE);
        //Get the gyroscopic sensor
        //mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        //register the listener
        //mSensorManager.registerListener(new GyroscopeReader(), mSensor, 0);

        robot.init(hardwareMap);

        robot.getDriveLeft().setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.getDriveRight().setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        idle();

        //Wait for the autonomous gamemode to start
        waitForStart();
        telemetry.addLine("TEST");
        telemetry.update();

        encoderDrive(75, 12, 12, 100);
    }

    public void encoderDrive(double speed, double leftInches, double rightInches, double timeoutS) throws InterruptedException {
        // Ensure that the opmode is still active
        if (!opModeIsActive()) {
            telemetry.addLine("Tried to use encoder while stationary");
            telemetry.update();
            return;
        }
        ElapsedTime runtime = new ElapsedTime();

        // Determine new target position, and pass to motor controller
        robot.getDriveLeft().setTargetPosition(robot.getDriveLeft().getCurrentPosition() + (int) (leftInches * COUNTS_PER_INCH));
        robot.getDriveRight().setTargetPosition(robot.getDriveRight().getCurrentPosition() + (int) (rightInches * COUNTS_PER_INCH));

        // Turn On RUN_TO_POSITION
        robot.getDriveLeft().setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.getDriveRight().setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // reset the timeout time and start motion.
        runtime.reset();
        robot.getDriveLeft().setPower(Math.abs(speed));
        robot.getDriveRight().setPower(Math.abs(speed));

        telemetry.addLine("Problems??");
        telemetry.update();

        // keep looping while we are still active, and there is time left, and both motors are running.
        while (opModeIsActive() && (runtime.seconds() < timeoutS) && (robot.getDriveLeft().isBusy() && robot.getDriveRight().isBusy())) {
            telemetry.update();

            if (opModeIsActive())
                telemetry.addLine("Opmode is active");
            if (runtime.seconds() < timeoutS)
                telemetry.addLine("runtime less than timeout s");
            if (robot.getDriveLeft().isBusy())
                telemetry.addLine("Left drive is busy");
            if (robot.getDriveRight().isBusy())
                telemetry.addLine("Right drive is busy as a busy bee");

            // Allow time for other processes to run.
            idle();
        }

        // Stop all motion;
        robot.getDriveLeft().setPower(0);
        robot.getDriveRight().setPower(0);

        // Turn off RUN_TO_POSITION
        robot.getDriveLeft().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.getDriveRight().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
}
