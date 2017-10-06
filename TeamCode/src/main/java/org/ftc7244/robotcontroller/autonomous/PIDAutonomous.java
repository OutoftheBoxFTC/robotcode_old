package org.ftc7244.robotcontroller.autonomous;

import android.support.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.RobotLog;

import org.ftc7244.robotcontroller.Westcoast;
import org.ftc7244.robotcontroller.autonomous.drivers.EncoderDrive;
import org.ftc7244.robotcontroller.autonomous.drivers.GyroscopeDrive;
import org.ftc7244.robotcontroller.autonomous.drivers.UltrasonicDrive;
import org.ftc7244.robotcontroller.sensor.gyroscope.GyroscopeProvider;
import org.ftc7244.robotcontroller.sensor.gyroscope.NavXGyroscopeProvider;
import org.ftc7244.robotcontroller.sensor.vuforia.ImageTransformProvider;

/**
 * Contains all the code for different drive types including ${@link GyroscopeDrive}
 * and ${@link UltrasonicDrive} it also connects to the ${@link Westcoast} class to handle robot
 * control. Not much happens here beyond the essentials for each control method. It also
 * automatically handles wait for startImageReading since most of the setup is completed and only driving
 * instructions are needed.
 */
public abstract class PIDAutonomous extends LinearOpMode {

    @NonNull
    protected final GyroscopeDrive gyroscope;
    @NonNull
    protected final UltrasonicDrive ultrasonic;

    protected final EncoderDrive encoder;

    protected final GyroscopeProvider gyroProvider;
    protected final ImageTransformProvider imageProvider;

    protected Westcoast robot;
    private long end;

    /**
     * Set the classes up and allow for java
     */
    protected PIDAutonomous() {
        robot = new Westcoast(this);
        gyroProvider = new NavXGyroscopeProvider(robot);
        gyroscope = new GyroscopeDrive(robot, gyroProvider);
        ultrasonic = new UltrasonicDrive(robot);
        encoder = new EncoderDrive(robot);
        imageProvider = new ImageTransformProvider();
    }

    @Override
    public void runOpMode() throws InterruptedException {
        robot.init();
        Status.setAutonomous(this);
        gyroProvider.start(hardwareMap);
        imageProvider.start(hardwareMap);

        boolean calibratedMsg = false;
        while (!isStarted()) {
            if (!calibratedMsg && gyroProvider.isCalibrated()) {
                telemetry.addLine("Gyroscope calibrated!");
                telemetry.update();
                calibratedMsg = true;
            } else if (calibratedMsg && !gyroProvider.isCalibrated()) {
                telemetry.addLine("LOST CONNECTION");
                telemetry.update();
                calibratedMsg = false;
            }
            idle();
        }

        try {
            gyroscope.resetOrientation();
            end = System.currentTimeMillis() + 30000;
            run();
        } catch (Throwable t) {
            RobotLog.e(t.getMessage());
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
