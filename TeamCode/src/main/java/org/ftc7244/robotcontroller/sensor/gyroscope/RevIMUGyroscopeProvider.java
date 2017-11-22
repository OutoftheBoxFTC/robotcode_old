package org.ftc7244.robotcontroller.sensor.gyroscope;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;
import org.ftc7244.robotcontroller.autonomous.Status;

import java.util.Locale;
import java.util.concurrent.Executors;

/**
 * Created by Eeshwar Laptop on 11/15/2017.
 */

public class    RevIMUGyroscopeProvider extends GyroscopeProvider implements Runnable {

    private BNO055IMU imu;
    private volatile boolean quit;

    @Override
    public void start(HardwareMap map) {
        imu = map.get(BNO055IMU.class, "imu");
        if(imu.isGyroCalibrated()) {
            BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
            parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
            parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
            parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
            parameters.loggingEnabled = true;
            parameters.loggingTag = "IMU";
            parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

            // Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
            // on a Core Device Interface Module, configured to be a sensor of type "AdaFruit IMU",
            // and named "imu".
            imu.initialize(parameters);
            quit = false;
        }
        Executors.newSingleThreadExecutor().submit(this);
    }

    @Override
    public void calibrate() {
        //ignore
    }

    @Override
    public boolean isCalibrated() {
        return imu.isGyroCalibrated();
    }

    @Override
    public void stop() {
        quit = true;
        imu.close();
    }

    @Override
    public void run() {
        while (!quit && !Status.isStopRequested()) {
            RobotLog.i("PID | UPDATING VALUES");
            Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            setX(angles.thirdAngle);
            setY(angles.secondAngle);
            setZ(angles.firstAngle);
        }
    }
}
