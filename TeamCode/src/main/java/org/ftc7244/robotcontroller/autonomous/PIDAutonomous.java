package org.ftc7244.robotcontroller.autonomous;

import android.hardware.SensorManager;

import com.qualcomm.robotcore.util.RobotLog;

import org.ftc7244.robotcontroller.Westcoast;
import org.ftc7244.robotcontroller.autonomous.drivers.GyroscopeDrive;
import org.ftc7244.robotcontroller.autonomous.drivers.GyroscopeDriveControls;
import org.ftc7244.robotcontroller.autonomous.drivers.UltrasonicDrive;
import org.ftc7244.robotcontroller.autonomous.drivers.UltrasonicDriveControls;
import org.ftc7244.robotcontroller.sensor.GyroscopeProvider;

import static android.content.Context.SENSOR_SERVICE;

/**
 * Created by OOTB on 10/16/2016.
 */
public abstract class PIDAutonomous extends CoreAutonomous implements UltrasonicDriveControls, GyroscopeDriveControls {

    public static boolean DEBUG = true;

    protected GyroscopeDrive gyroscope;
    protected UltrasonicDrive ultrasonic;
    protected Westcoast robot;
    private GyroscopeProvider provider;

    protected PIDAutonomous() {
        robot = new Westcoast(this);
        provider = new GyroscopeProvider();
        gyroscope = new GyroscopeDrive(robot, provider, DEBUG);
        ultrasonic = new UltrasonicDrive(robot, DEBUG);
    }

    @Override
    public void runOpMode() throws InterruptedException {
        robot.init();

        waitForStart();
        provider.start((SensorManager) hardwareMap.appContext.getSystemService(SENSOR_SERVICE), 1);
        sleep(1000);

        try {
            gyroscope.resetOrientation();
            super.runOpMode();
        } catch (Throwable t) {
            RobotLog.ee("Error", "Error");
            t.printStackTrace();
        } finally {
            provider.stop();
        }
    }

    public abstract void run() throws InterruptedException;

    @Override
    public void drive(double power, double inches) throws InterruptedException {
        gyroscope.drive(power, inches);
    }

    @Override
    public void rotate(double degrees) throws InterruptedException {
        gyroscope.rotate(degrees);
    }

    @Override
    public void driveUntilLine(double power, GyroscopeDrive.Sensor mode, double distance) throws InterruptedException {
        gyroscope.driveUntilLine(power, mode, distance);
    }

    @Override
    public void resetOrientation() throws InterruptedException {
        gyroscope.resetOrientation();
    }

    @Override
    public void parallelize() throws InterruptedException {
        ultrasonic.parallelize();
    }

    @Override
    public void driveParallel(double power) throws InterruptedException {
        ultrasonic.driveParallel(power);
    }
}
