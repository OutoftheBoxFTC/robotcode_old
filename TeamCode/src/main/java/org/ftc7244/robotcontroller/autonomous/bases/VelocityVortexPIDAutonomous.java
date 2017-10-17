package org.ftc7244.robotcontroller.autonomous.bases;

import android.support.annotation.NonNull;

import org.ftc7244.robotcontroller.autonomous.drivers.EncoderDrive;
import org.ftc7244.robotcontroller.autonomous.drivers.GyroscopeDrive;
import org.ftc7244.robotcontroller.autonomous.drivers.UltrasonicDrive;
import org.ftc7244.robotcontroller.hardware.Westcoast;
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
public abstract class VelocityVortexPIDAutonomous extends PIDAutonamous {

    @NonNull
    protected final GyroscopeDrive gyroscope;
    @NonNull
    protected final UltrasonicDrive ultrasonic;

    protected final EncoderDrive encoder;

    protected final GyroscopeProvider gyroProvider;
    protected final ImageTransformProvider imageProvider;

    protected Westcoast robot;

    private boolean calibratedMsg;

    /**
     * Set the classes up and allow for java
     */
    protected VelocityVortexPIDAutonomous() {
        robot = new Westcoast(this);
        gyroProvider = new NavXGyroscopeProvider(robot);
        gyroscope = new GyroscopeDrive(robot, gyroProvider);
        ultrasonic = new UltrasonicDrive(robot);
        encoder = new EncoderDrive(robot);
        imageProvider = new ImageTransformProvider();
        calibratedMsg = false;
    }

    @Override
    protected void onEnd(boolean err) {
        gyroProvider.stop();
    }

    @Override
    protected void whileNotStarted() {
        if (!calibratedMsg && gyroProvider.isCalibrated()) {
            telemetry.addLine("Gyroscope calibrated!");
            telemetry.update();
            calibratedMsg = true;
        } else if (calibratedMsg && !gyroProvider.isCalibrated()) {
            telemetry.addLine("LOST CONNECTION");
            telemetry.update();
            calibratedMsg = false;
        }
    }

    @Override
    protected void startProviders() {
        robot.init();
        gyroProvider.start(hardwareMap);
        imageProvider.start(hardwareMap);
    }

    @Override
    protected void beforeStart() throws InterruptedException{
        gyroscope.resetOrientation();
    }

    public abstract void run() throws InterruptedException;
}