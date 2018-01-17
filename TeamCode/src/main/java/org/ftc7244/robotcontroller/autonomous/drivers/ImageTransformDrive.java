package org.ftc7244.robotcontroller.autonomous.drivers;

import org.ftc7244.robotcontroller.autonomous.controllers.PIDControllerBuilder;
import org.ftc7244.robotcontroller.autonomous.controllers.PIDDriveControl;
import org.ftc7244.robotcontroller.autonomous.terminators.ConditionalTerminator;
import org.ftc7244.robotcontroller.autonomous.terminators.SensitivityTerminator;
import org.ftc7244.robotcontroller.autonomous.terminators.TimerTerminator;
import org.ftc7244.robotcontroller.hardware.Hardware;
import org.ftc7244.robotcontroller.sensor.vuforia.ImageTransformProvider;

/**
 * Originally used to recenter robot after driving off balancing stone. Deprecated due to
 * unreliability
 *
 * Uses Vuforia relative image rotation as input for PID. This allows us to always align to the wall
 * no matter the rotational uncertainty of driving off the balancing stone
 */

@Deprecated
public class ImageTransformDrive extends PIDDriveControl {
    private ImageTransformProvider imageProvider;

    /**
     *
     * @param robot access to motors on robot
     * @param imageProvider supplier of Vuforia inputs
     */
    public ImageTransformDrive(Hardware robot, ImageTransformProvider imageProvider) {
        super(new PIDControllerBuilder().setProportional(0)
                .setIntegral(0.02)
                .setDerivative(0.00004)
                .setDelay(3.5)
                .setOutputRange(1)
                .createController(), robot);
        this.imageProvider = imageProvider;
    }

    /**
     * Uses vuforia as a frame of reference for our robot
     * @return Vufora image offset
     */
    @Override
    public double getReading() {
        return this.imageProvider.getImageRotation(ImageTransformProvider.RotationAxis.YAW);
    }

    /**
     * Aligns the robot to within 1 degree of parallel with the wall. It will also manually terminate
     * after 3 seconds
     * @throws InterruptedException if code fails to terminate on stop requested
     */
    public void allignToImage() throws InterruptedException {
        double offset = imageProvider.getImageRotation(ImageTransformProvider.RotationAxis.YAW),
                degrees = -offset;
        control(0, 0, new ConditionalTerminator(new SensitivityTerminator(this, degrees, 1, 300), new TimerTerminator(2000)));
    }
}