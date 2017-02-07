package org.ftc7244.robotcontroller.autonomous;

import android.support.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.RobotLog;

import org.ftc7244.robotcontroller.Westcoast;
import org.ftc7244.robotcontroller.autonomous.drivers.GyroscopeDrive;
import org.ftc7244.robotcontroller.autonomous.drivers.UltrasonicDrive;
import org.ftc7244.robotcontroller.sensor.accerometer.AccelerometerProvider;
import org.ftc7244.robotcontroller.sensor.accerometer.PhoneAccelerometerProvider;
import org.ftc7244.robotcontroller.sensor.gyroscope.GyroscopeProvider;
import org.ftc7244.robotcontroller.sensor.gyroscope.PhoneGyroscopeProvider;

/**
 * Contains all the code for different drive types including ${@link GyroscopeDrive}
 * and ${@link UltrasonicDrive} it also connects to the ${@link Westcoast} class to handle robot
 * control. Not much happens here beyond the essentials for each control method. It also
 * automatically handles wait for start since most of the setup is completed and only driving
 * instructions are needed.
 */
public abstract class PIDAutonomous extends LinearOpMode {

    public static boolean DEBUG = true;

    @NonNull
    protected final GyroscopeDrive gyroscope;
    @NonNull
    protected final UltrasonicDrive ultrasonic;
    protected Westcoast robot;

    private GyroscopeProvider gyroProvider;
    private AccelerometerProvider accelProvider;

    /**
     * Set the classes up and allow for java
     */
    protected PIDAutonomous() {
        robot = new Westcoast(this);
        gyroProvider = new PhoneGyroscopeProvider();
        accelProvider = new PhoneAccelerometerProvider();
        gyroscope = new GyroscopeDrive(robot, gyroProvider, accelProvider, DEBUG);
        ultrasonic = new UltrasonicDrive(robot, DEBUG);
    }

    @Override
    public void runOpMode() throws InterruptedException {
        robot.init();
        Status.setAutonomous(this);
        waitForStart();

        gyroProvider.start(hardwareMap);
        accelProvider.start(hardwareMap);

        try {
            while (!gyroProvider.isCalibrated()) idle();
            gyroscope.resetOrientation();
            run();
        } catch (Throwable t) {
            RobotLog.e(t.getMessage());
        } finally {
            gyroProvider.stop();
            accelProvider.stop();
            Status.setAutonomous(null);
        }
    }

    public abstract void run() throws InterruptedException;
}
