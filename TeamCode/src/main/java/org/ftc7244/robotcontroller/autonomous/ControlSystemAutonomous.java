package org.ftc7244.robotcontroller.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.RobotLog;

import org.ftc7244.datalogger.Logger;
import org.ftc7244.robotcontroller.autonomous.drivers.GyroscopeDrive;
import org.ftc7244.robotcontroller.hardware.Westcoast;
import org.ftc7244.robotcontroller.sensor.gyroscope.GyroscopeProvider;
import org.ftc7244.robotcontroller.sensor.gyroscope.RevIMUGyroscopeProvider;
import org.ftc7244.robotcontroller.sensor.vuforia.ImageTransformProvider;

/**
 * Contains all the code for different drive types including ${@link GyroscopeDrive}.
 * Not much happens here beyond the essentials for each control method. It also
 * automatically handles wait for startImageReading since most of the setup is completed and only driving
 * instructions are needed.
 */
public abstract class ControlSystemAutonomous extends LinearOpMode {

    public final static long AUTONOMOUS_DURATION = 30 * 1000;

    protected final GyroscopeProvider gyroProvider;

    protected final GyroscopeDrive gyroscope;

    protected final ImageTransformProvider imageProvider;

    protected Westcoast robot;
    private long end;

    /**
     * Loads hardware, pid drives, and sensor providers
     */
    protected ControlSystemAutonomous() {
        robot = new Westcoast(this);

        gyroProvider = new RevIMUGyroscopeProvider();

        gyroscope = new GyroscopeDrive(robot, gyroProvider);

        imageProvider = new ImageTransformProvider();
    }

    @Override
    public void runOpMode() throws InterruptedException {
        Logger.init();

        robot.init();
        robot.initServos();
        robot.getDriveBackLeft().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.getDriveBackRight().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.getDriveFrontLeft().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.getDriveFrontRight().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Status.setAutonomous(this);
        //Initializes robot and debug features

        try {
            gyroProvider.start(hardwareMap);
            imageProvider.start(hardwareMap);
            while (!isStarted()) {
                if (gyroProvider.isCalibrated()) {
                    telemetry.addLine("Gyroscope calibrated!");
                    telemetry.update();
                } else  {
                    telemetry.addLine("No Connection");
                    telemetry.update();
                }
                idle();
            }

            gyroscope.resetOrientation();
            end = System.currentTimeMillis() + AUTONOMOUS_DURATION;
            //Calibrates and starts providers
            run();
        } catch (Throwable t) {
            RobotLog.e(t.getMessage());
            t.printStackTrace();
            //Logs unexpected errors to prevent app crashing
        } finally {
            gyroProvider.stop();
            imageProvider.stop();
            Status.setAutonomous(null);
            //Stops all providers regardless of error
        }
    }

    public long getAutonomousEnd() {
        return end;
    }

    /**
     * The custom autonomous procedure
     * @throws InterruptedException if code fails to terminate on stop requested
     */
    public abstract void run() throws InterruptedException;
}