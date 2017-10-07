package org.ftc7244.robotcontroller.sensor.vuforia;

import android.support.annotation.Nullable;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.ftc7244.robotcontroller.sensor.DataFilter;
import org.ftc7244.robotcontroller.sensor.SensorProvider;

import java.util.Vector;

public class ImageTransformProvider extends SensorProvider implements Runnable {
    public static final String VUFORIA_LICENSE_KEY = "AQ7YHUT/////AAAAGVOxmiN4SkHwqyEIEpsDKxo9Gpbkev2MCSd8RFB1jHcnH21ZDRyGXPK9hwVuWRRN4eiOU42jJhNeOiOlyh7yAdqsjfotKCW71TMFv7OiZr7uw6kS049r5LuvfMrxc9DyfDVCRh8aViWYNSuJVAGk6nF8D9dC9i5hy1FQFCRN3wxdQ49o/YqMfLeQNMgQIW/K3fqLi8ez+Ek9cF0mH1SGqBcv6dJrRavFqV/twq9F9fK+yW1rwcAQGunLKu2g6p0r1YXeSQe0qiMkfwumVwb2Sq0ZmEKQjHV4hwm14opyvtbXZzJwHppKOmBC0XXpkCBs7xLcYgoGbEiiGwEQv+N1xVnRha3NZXCmHH44JweTvmbh";
    private boolean vuforiaInitialized, running, imageSeen;
    private static final double MM_TO_INCHES = 25.4;
    @Nullable
    private VuforiaLocalizer vuforia;
    @Nullable
    private VuforiaTrackables pictographs;
    @Nullable
    private VuforiaTrackable template;

    private DataFilter xTrans, yTrans, zTrans, xRot, yRot, zRot;

    public ImageTransformProvider(){
        vuforiaInitialized = false;
        xTrans = new DataFilter(10);
        yTrans = new DataFilter(10);
        zTrans = new DataFilter(10);
        xRot = new DataFilter(10);
        yRot = new DataFilter(10);
        zRot = new DataFilter(10);
    }
    @Override
    public void start(HardwareMap map) {
        if(!vuforiaInitialized)initializeVuforia(map);
        Thread thread = new Thread(this);
        thread.start();
    }

    private void initializeVuforia(HardwareMap  map){
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(map.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", map.appContext.getPackageName()));
        parameters.vuforiaLicenseKey = VUFORIA_LICENSE_KEY;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        vuforia = ClassFactory.createVuforiaLocalizer(parameters);
        pictographs = vuforia.loadTrackablesFromAsset("RelicVuMark");
        template = pictographs.get(0);
        pictographs.activate();
        vuforiaInitialized = true;
    }

    public RelicRecoveryVuMark getImageReading(){
        return RelicRecoveryVuMark.from(template);
    }

    public double getImageDistance(Axis axis){
        if(imageSeen){

        }
        return -1;
    }

    public double getImageRotation(Axis axis){
        if(imageSeen){

        }
        return -1;
    }

    @Override
    public void run() {
        VectorF translation = null;
        Orientation rotation = null;
        while (running){
            if(!RelicRecoveryVuMark.from(template).equals(RelicRecoveryVuMark.UNKNOWN)){
                if(imageSeen){

                }
                else {
                    OpenGLMatrix transform = ((VuforiaTrackableDefaultListener)template.getListener()).getPose();
                    translation = transform.getTranslation();
                    rotation = Orientation.getOrientation(transform, AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);
                    imageSeen = true;
                }
            }
            else {
                translation = null;
                rotation = null;
            }
        }
    }

    @Override
    public void stop() {

    }

    public enum Axis {
        X,Y,Z
    }
}
