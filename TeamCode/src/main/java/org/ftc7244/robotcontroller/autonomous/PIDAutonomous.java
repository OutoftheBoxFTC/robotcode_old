package org.ftc7244.robotcontroller.autonomous;

import android.hardware.SensorManager;

import com.qualcomm.robotcore.util.RobotLog;

import org.ftc7244.robotcontroller.Westcoast;
import org.ftc7244.robotcontroller.autonomous.pid.drivers.GyroscopeDrive;
import org.ftc7244.robotcontroller.autonomous.pid.drivers.UltrasonicDrive;
import org.ftc7244.robotcontroller.sensor.GyroscopeProvider;

import static android.content.Context.SENSOR_SERVICE;

/**
 * Created by OOTB on 10/16/2016.
 */
public abstract class PIDAutonomous extends CoreAutonomous {

    public static boolean DEBUG = true;

    protected final GyroscopeDrive gyroscope;
    protected final UltrasonicDrive ultrasonic;
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
}
