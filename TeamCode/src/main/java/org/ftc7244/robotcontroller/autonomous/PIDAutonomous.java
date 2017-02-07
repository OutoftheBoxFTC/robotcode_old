package org.ftc7244.robotcontroller.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.RobotLog;

import org.ftc7244.robotcontroller.Westcoast;
import org.ftc7244.robotcontroller.autonomous.drivers.GyroscopeDrive;
import org.ftc7244.robotcontroller.autonomous.drivers.UltrasonicDrive;
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

    protected final GyroscopeDrive gyroscope;
    protected final UltrasonicDrive ultrasonic;
    protected Westcoast robot;

    private PhoneGyroscopeProvider provider;

    /**
     * Set the classes up and allow for java
     */
    protected PIDAutonomous() {
        robot = new Westcoast(this);
        provider = new PhoneGyroscopeProvider();
        gyroscope = new GyroscopeDrive(robot, provider, DEBUG);
        ultrasonic = new UltrasonicDrive(robot, DEBUG);
    }

    @Override
    public void runOpMode() throws InterruptedException {
        robot.init();
        Status.setAutonomous(this);

        waitForStart();

        try {
            while (!provider.isCalibrated()) idle();
            gyroscope.resetOrientation();
            run();
        } catch (Throwable t) {
            RobotLog.ee("Error", "Error");
            t.printStackTrace();
        } finally {
            provider.stop();
            Status.setAutonomous(null);
        }
    }

    public abstract void run() throws InterruptedException;
}
