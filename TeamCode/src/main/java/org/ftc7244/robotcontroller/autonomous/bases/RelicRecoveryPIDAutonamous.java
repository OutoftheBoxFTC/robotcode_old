package org.ftc7244.robotcontroller.autonomous.bases;

import org.ftc7244.robotcontroller.autonomous.drivers.GyroscopeDrive;
import org.ftc7244.robotcontroller.autonomous.drivers.ImageTransformDrive;
import org.ftc7244.robotcontroller.hardware.Westcoast;
import org.ftc7244.robotcontroller.sensor.gyroscope.GyroscopeProvider;
import org.ftc7244.robotcontroller.sensor.gyroscope.NavXGyroscopeProvider;
import org.ftc7244.robotcontroller.sensor.gyroscope.RevIMUGyroscopeProvider;
import org.ftc7244.robotcontroller.sensor.vuforia.ImageTransformProvider;

/**
 * Created by FTC 7244 on 10/22/2017.
 */

public abstract class RelicRecoveryPIDAutonamous extends PIDAutonomous {

    //protected final ImageTransformProvider imageProvider;

    //protected final ImageTransformDrive imageDrive;

    protected final GyroscopeProvider gyroProvider;

    protected final GyroscopeDrive gyroscope;

    protected Westcoast robot;

    public RelicRecoveryPIDAutonamous(){
        robot = new Westcoast(this);
        //imageProvider = new ImageTransformProvider();
        //imageDrive = new ImageTransformDrive(robot, imageProvider);
        gyroProvider = new NavXGyroscopeProvider(robot);
        gyroscope = new GyroscopeDrive(robot, gyroProvider);
        calibratedMsg = false;
    }

    private boolean calibratedMsg;

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
        //imageProvider.start(hardwareMap);
        gyroProvider.start(hardwareMap);
    }

    @Override
    protected void onEnd(boolean err) {
        //imageProvider.stop();
        gyroProvider.stop();
    }

    @Override
    protected void beforeStart() throws InterruptedException {
        gyroProvider.calibrate();
    }
}
