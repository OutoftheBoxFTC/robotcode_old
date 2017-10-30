package org.ftc7244.robotcontroller.autonomous.bases;

import org.ftc7244.robotcontroller.autonomous.drivers.ImageTransformDrive;
import org.ftc7244.robotcontroller.hardware.RelicRecoveryWestcoast;
import org.ftc7244.robotcontroller.sensor.vuforia.ImageTransformProvider;

/**
 * Created by FTC 7244 on 10/22/2017.
 */

public abstract class RelicRecoveryPIDAutonamous extends PIDAutonamous {

    protected final ImageTransformProvider imageProvider;

    protected final ImageTransformDrive imageDrive;

    protected RelicRecoveryWestcoast robot;

    public RelicRecoveryPIDAutonamous(){
        robot = new RelicRecoveryWestcoast(this);
        imageProvider = new ImageTransformProvider();
        imageDrive = new ImageTransformDrive(robot, imageProvider);
        calibratedMsg = false;
    }

    private boolean calibratedMsg;

    @Override
    protected void whileNotStarted() {

    }

    @Override
    protected void startProviders() {
        robot.init();
        imageProvider.start(hardwareMap);
    }

    @Override
    protected void onEnd(boolean err) {
        imageProvider.stop();
    }

    @Override
    protected void beforeStart() throws InterruptedException {

    }
}
