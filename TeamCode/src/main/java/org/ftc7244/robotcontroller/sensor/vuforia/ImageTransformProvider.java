package org.ftc7244.robotcontroller.sensor.vuforia;

import android.support.annotation.Nullable;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.ftc7244.robotcontroller.sensor.SensorProvider;

public class ImageTransformProvider extends SensorProvider {
    public static final String VUFORIA_LICENSE_KEY = "AQ7YHUT/////AAAAGVOxmiN4SkHwqyEIEpsDKxo9Gpbkev2MCSd8RFB1jHcnH21ZDRyGXPK9hwVuWRRN4eiOU42jJhNeOiOlyh7yAdqsjfotKCW71TMFv7OiZr7uw6kS049r5LuvfMrxc9DyfDVCRh8aViWYNSuJVAGk6nF8D9dC9i5hy1FQFCRN3wxdQ49o/YqMfLeQNMgQIW/K3fqLi8ez+Ek9cF0mH1SGqBcv6dJrRavFqV/twq9F9fK+yW1rwcAQGunLKu2g6p0r1YXeSQe0qiMkfwumVwb2Sq0ZmEKQjHV4hwm14opyvtbXZzJwHppKOmBC0XXpkCBs7xLcYgoGbEiiGwEQv+N1xVnRha3NZXCmHH44JweTvmbh";
    private boolean vuforiaInitialized;
    private static final double MM_TO_INCHES = 25.4;
    @Nullable
    private VuforiaLocalizer vuforia;
    @Nullable
    private VuforiaTrackables pictographs;
    @Nullable
    private VuforiaTrackable template;
    public ImageTransformProvider(){
        vuforiaInitialized = false;
    }
    @Override
    public void start(HardwareMap map) {
        if(!vuforiaInitialized) {
            VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(map.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", map.appContext.getPackageName()));
            parameters.vuforiaLicenseKey = VUFORIA_LICENSE_KEY;
            parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
            vuforia = ClassFactory.createVuforiaLocalizer(parameters);
            pictographs = vuforia.loadTrackablesFromAsset("RelicVuMark");
            template = pictographs.get(0);
            pictographs.activate();
            vuforiaInitialized = true;
        }
    }

    public RelicRecoveryVuMark getImageReading(){
        return RelicRecoveryVuMark.from(template);
    }

    public double getImageDistance(Axis axis){
        if(RelicRecoveryVuMark.from(template).equals(RelicRecoveryVuMark.UNKNOWN)) return -1;
        OpenGLMatrix pose = ((VuforiaTrackableDefaultListener)template.getListener()).getPose();
        if(pose==null)return -1;

        switch (axis){
            case X:
                return (pose.getTranslation().get(0)) / MM_TO_INCHES;
            case Y:
                return pose.getTranslation().get(1) / MM_TO_INCHES;
            case Z:
                return pose.getTranslation().get(2) / MM_TO_INCHES;
        }
        return -1;
    }

    public double getImageRotation(Axis axis){
        if(RelicRecoveryVuMark.from(template).equals(RelicRecoveryVuMark.UNKNOWN)) return -1;
        OpenGLMatrix pose = ((VuforiaTrackableDefaultListener)template.getListener()).getPose();
        Orientation rotation = Orientation.getOrientation(pose, AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);
        if(pose==null)return -1;
        switch (axis){
            case X:
                return rotation.firstAngle;
            case Y:
                return rotation.secondAngle;
            case Z:
                return rotation.thirdAngle;
        }
        return -1;
    }

    @Override
    public void stop() {

    }

    public enum Axis {
        X,Y,Z
    }
}
