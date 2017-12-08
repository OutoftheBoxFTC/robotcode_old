package org.ftc7244.robotcontroller.hardware;

import android.support.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.RobotLog;

import org.ftc7244.robotcontroller.autonomous.Status;

import java.util.Map;

/**
 * Created by FTC 7244 on 10/16/2017.
 */

public abstract class Hardware {
    protected OpMode opMode;
    protected double countsPerInch;

    public Hardware(OpMode opMode, double countsPerInch) {
        this.opMode = opMode;
        this.countsPerInch = countsPerInch;
    }

    /**
     * This is the codes own way of pausing. This has the the capability of stopping the wait if
     * stop is requested and passing up an exception if it fails as well
     *
     * @param ms the duration to sleep in milliseconds
     * @throws InterruptedException if the code fails to terminate before stop requested
     */
    public static void sleep(long ms) throws InterruptedException {
        long target = System.currentTimeMillis() + ms;
        while (target > System.currentTimeMillis() && !Status.isStopRequested()) Thread.sleep(1);
    }

    /**
     * Get the value associated with an id and instead of raising an error return null and log it
     *
     * @param map  the hardware map from the HardwareMap
     * @param name The ID in the hardware map
     * @param <T>  the type of hardware map
     * @return the hardware device associated with the name
     */
    protected <T extends HardwareDevice> T getOrNull(@NonNull HardwareMap.DeviceMapping<T> map, String name) {
        for (Map.Entry<String, T> item : map.entrySet()) {
            if (!item.getKey().equalsIgnoreCase(name)) {
                continue;
            }
            return item.getValue();
        }
        opMode.telemetry.addLine("ERROR: " + name + " not found!");
        RobotLog.e("ERROR: " + name + " not found!");
        return null;
    }

    /**
     * Waits for all the motors to have zero position and if it is not zero tell it to reset
     *
     * @param motors all the motors to reset
     */
    public static void resetMotors(@NonNull DcMotor... motors) {
        boolean notReset = true;
        while (notReset) {
            boolean allReset = true;
            for (DcMotor motor : motors) {
                if (motor.getCurrentPosition() == 0) {
                    continue;
                }
                allReset = false;
                motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            }
            notReset = !allReset;
        }
        for (DcMotor motor : motors) motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public abstract void init();

    public abstract void drive(double leftPower, double rightPower, long timeMillis) throws InterruptedException;
    public abstract void drive(double leftPower, double rightPower);
    public abstract void driveToInch(double power, double inches);

    public abstract void resetDriveMotors();

    public double getCountsPerInch() {
        return countsPerInch;
    }

    public abstract int getDriveEncoderAverage();

    public OpMode getOpMode() {
        return opMode;
    }

}
