package org.ftc7244.robotcontroller.sensor.gyroscope;

import android.opengl.GLES30;
import android.provider.Settings;
import android.support.annotation.Nullable;

import com.kauailabs.navx.ftc.AHRS;
import com.kauailabs.navx.ftc.IDataArrivalSubscriber;
import com.qualcomm.hardware.kauailabs.NavxMicroNavigationSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.ftc7244.robotcontroller.hardware.Westcoast;

/**
 * Utilizes the NavX-Micro to get an orientatio and prevents code from executing until it
 * has been fully calibrated.
 */
public class NavXGyroscopeProvider extends GyroscopeProvider implements Runnable{
    @Nullable
    private NavxMicroNavigationSensor navxDevice;
    private Westcoast robot;
    private Thread thread;
    private static final long WAIT_INTERVAL_MS = 30;
    private boolean running;
    public NavXGyroscopeProvider(Westcoast robot){
        this.robot = robot;
    }

    @Override
    public void start(HardwareMap map) {
        navxDevice = robot.getNavX();
        thread = new Thread(this);
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
        navxDevice.close();
        navxDevice = null;
        thread.stop();
    }

    @Override
    public void run() {
        while (running) {
            if (System.currentTimeMillis() - getTimestamp() >= WAIT_INTERVAL_MS) {
                super.setTimestamp(System.currentTimeMillis());
                Orientation angles = navxDevice.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);
                setX(getX() + angles.firstAngle);
                setY(getZ() + angles.secondAngle);
                setZ(getZ() + angles.thirdAngle);
            }
        }
    }
}
