package org.ftc7244.robotcontroller.sensor.gyroscope;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Eeshwar Laptop on 11/15/2017.
 */

public class RevIMUGyroscopeProvider extends GyroscopeProvider {

    private BNO055IMU imu;

    @Override
    public void start(HardwareMap map) {
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json";

        imu = map.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);
        imu.startAccelerationIntegration(new Position(), new Velocity(), 1000);
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
        //ignore
    }

    @Override
    public double getX() {
        update();
        return super.getX();
    }

    @Override
    public double getY() {
        update();
        return super.getY();
    }

    @Override
    public double getZ() {
        update();
        return super.getZ();
    }

    public void update() {
        Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        setX(angles.thirdAngle);
        setY(angles.secondAngle);
        setZ(angles.firstAngle);
        setTimestamp(angles.acquisitionTime);
    }
}
