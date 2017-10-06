package org.ftc7244.robotcontroller.autonomous.drivers;

import org.ftc7244.robotcontroller.Westcoast;
import org.ftc7244.robotcontroller.autonomous.controllers.PIDControllerBuilder;
import org.ftc7244.robotcontroller.autonomous.controllers.PIDDriveControl;
import org.ftc7244.robotcontroller.sensor.vuforia.ImageTransformProvider;

/**
 * Created by BeaverDuck on 10/4/17.
 */

public class ImageTranformDrive extends PIDDriveControl{
    private ImageTransformProvider imageProvider;
    public ImageTranformDrive(Westcoast robot, ImageTransformProvider imageProvider) {
        super(new PIDControllerBuilder().setProportional(0)
                .setIntegral(0)
                .setDerivative(0)
                .setDelay(0)
                .setOutputRange(1)
                .createController(), robot);
        this.imageProvider = imageProvider;
    }

    @Override
    public double getReading() {
        return this.imageProvider.getImageRotation(ImageTransformProvider.Axis.Z);
    }

    public void allignToImage(){
        double offset = imageProvider.getImageRotation(ImageTransformProvider.Axis.Z),
        target = -offset;
        control(target, 0, );

    }
}