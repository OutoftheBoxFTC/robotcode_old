package org.ftc7244.robotcontroller.sensor.gyroscope;

import android.support.annotation.Nullable;

import com.qualcomm.hardware.kauailabs.NavxMicroNavigationSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

/**
 * Utilizes the NavX-Micro to get an orientatio and prevents code from executing until it
 * has been fully calibrated.
 */
public class NavXGyroscopeProvider extends GyroscopeProvider implements Runnable {

    private volatile boolean requestQuit, quit;
    @Nullable
    private NavxMicroNavigationSensor navxDevice;

    @Override
    public void start(HardwareMap map) {
        navxDevice = map.get(NavxMicroNavigationSensor.class, "navx");
        requestQuit = false;
        quit = false;
        new Thread(this).run();
    }

    @Override
    public void calibrate() {
        //ignore
    }

    @Override
    public boolean isCalibrated() {
        return navxDevice.isCalibrating();
    }

    @Override
    public void stop() {
        requestQuit = true;
        try {
            while (!quit) Thread.sleep(5);
        } catch (Exception e) {
            RobotLog.w("Unable to quit");
        }
        navxDevice.close();
        navxDevice = null;
    }

    @Override
    public void run() {
        while (!requestQuit) {
            setTimestamp(System.currentTimeMillis());
            Orientation angles = navxDevice.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);
            setX(getX() + angles.firstAngle);
            setY(getZ() + angles.secondAngle);
            setZ(getZ() + angles.thirdAngle);
        }
        quit = true;
    }
}
