package org.ftc7244.robotcontroller.autonomous.bases;

import org.ftc7244.robotcontroller.autonomous.drivers.ImageTransformDrive;
import org.ftc7244.robotcontroller.autonomous.drivers.RelicRecoveryGyroscope;
import org.ftc7244.robotcontroller.hardware.RelicRecoveryWestcoast;
import org.ftc7244.robotcontroller.sensor.gyroscope.GyroscopeProvider;
import org.ftc7244.robotcontroller.sensor.gyroscope.NavXGyroscopeProvider;
import org.ftc7244.robotcontroller.sensor.gyroscope.PhoneGyroscopeProvider;
import org.ftc7244.robotcontroller.sensor.gyroscope.RevIMUGyroscopeProvider;
import org.ftc7244.robotcontroller.sensor.vuforia.ImageTransformProvider;

/**
 * Created by FTC 7244 on 10/22/2017.
 */

public abstract class RelicRecoveryPIDAutonamous extends PIDAutonamous {

    protected final ImageTransformProvider imageProvider;

    protected final ImageTransformDrive imageDrive;

    protected final GyroscopeProvider gyroProvider;

    protected final RelicRecoveryGyroscope gyroscope;

    protected RelicRecoveryWestcoast robot;

    public RelicRecoveryPIDAutonamous(){
        robot = new RelicRecoveryWestcoast(this);
        imageProvider = new ImageTransformProvider();
        imageDrive = new ImageTransformDrive(robot, imageProvider);
        gyroProvider = new RevIMUGyroscopeProvider();
        gyroscope = new RelicRecoveryGyroscope(robot, gyroProvider);
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
        imageProvider.start(hardwareMap);
        gyroProvider.start(hardwareMap);
    }

    @Override
    protected void onEnd(boolean err) {
        imageProvider.stop();
    }

    @Override
    protected void beforeStart() throws InterruptedException {
        gyroProvider.calibrate();
    }
}
