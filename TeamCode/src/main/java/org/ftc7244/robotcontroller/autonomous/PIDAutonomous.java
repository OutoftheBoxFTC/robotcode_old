package org.ftc7244.robotcontroller.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.RobotLog;

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
public abstract class PIDAutonomous extends LinearOpMode {

    private final static long AUTONOMOUS_DURATION = 30 * 1000;

    protected final GyroscopeProvider gyroProvider;

    protected final GyroscopeDrive gyroscope;

    protected final ImageTransformProvider imageTransformProvider;

    protected Westcoast robot;
    private long end;

    /**
     * Set the classes up and allow for java
     */
    protected PIDAutonomous() {
        robot = new Westcoast(this);

        gyroProvider = new RevIMUGyroscopeProvider();
        imageTransformProvider = new ImageTransformProvider();

        gyroscope = new GyroscopeDrive(robot, gyroProvider);
    }

    @Override
    public void runOpMode() throws InterruptedException {
        robot.init();
        Status.setAutonomous(this);

        try {
            gyroProvider.start(hardwareMap);
            imageTransformProvider.start(hardwareMap);
            while (!isStarted()) {
                if (gyroProvider.isCalibrated()) {
                    telemetry.addLine("Gyroscope calibrated!");
                    telemetry.update();
                } else if (!gyroProvider.isCalibrated()) {
                    telemetry.addLine("No Connection");
                    telemetry.update();
                }
                idle();
            }

            gyroscope.resetOrientation();
            end = System.currentTimeMillis() + AUTONOMOUS_DURATION;
            run();
        } catch (Throwable t) {
            RobotLog.e(t.getMessage());
            t.printStackTrace();
        } finally {
            gyroProvider.stop();
            Status.setAutonomous(null);
        }
    }

    public long getAutonomousEnd() {
        return end;
    }

    public abstract void run() throws InterruptedException;
}