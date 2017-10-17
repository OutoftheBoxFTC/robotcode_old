package org.ftc7244.robotcontroller.autonomous.drivers;

import org.ftc7244.robotcontroller.hardware.Westcoast;
import org.ftc7244.robotcontroller.autonomous.controllers.PIDControllerBuilder;
import org.ftc7244.robotcontroller.autonomous.controllers.PIDDriveControl;
import org.ftc7244.robotcontroller.autonomous.terminators.ConditionalTerminator;
import org.ftc7244.robotcontroller.autonomous.terminators.SensitivityTerminator;
import org.ftc7244.robotcontroller.autonomous.terminators.TimerTerminator;
import org.ftc7244.robotcontroller.sensor.vuforia.ImageTransformProvider;

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

    public void allignToImage() throws InterruptedException {
        double offset = imageProvider.getImageRotation(ImageTransformProvider.Axis.Z),
        degrees = -offset;
        control(0, 0, new ConditionalTerminator(new SensitivityTerminator(this, degrees, 1, 300), new TimerTerminator(2000)));
    }

    public void drive(double inches, double power){

    }
}