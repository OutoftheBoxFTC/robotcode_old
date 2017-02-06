package org.ftc7244.robotcontroller.autonomous;

import com.qualcomm.robotcore.util.RobotLog;

import org.ftc7244.robotcontroller.Westcoast;
import org.ftc7244.robotcontroller.autonomous.pid.drivers.GyroscopeDrive;
import org.ftc7244.robotcontroller.autonomous.pid.drivers.UltrasonicDrive;
import org.ftc7244.robotcontroller.sensor.PhoneGyroscopeProvider;

/**
 * Created by OOTB on 10/16/2016.
 */
public abstract class PIDAutonomous extends CoreAutonomous {

    public static boolean DEBUG = true;

    protected final GyroscopeDrive gyroscope;
    protected final UltrasonicDrive ultrasonic;
    protected Westcoast robot;

    private PhoneGyroscopeProvider provider;

    protected PIDAutonomous() {
        robot = new Westcoast(this);
        provider = new PhoneGyroscopeProvider();
        gyroscope = new GyroscopeDrive(robot, provider, DEBUG);
        ultrasonic = new UltrasonicDrive(robot, DEBUG);
    }

    @Override
    public void runOpMode() throws InterruptedException {
        robot.init();

        waitForStart();
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
