package org.ftc7244.robotcontroller.hardware;

import android.support.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.RobotLog;

import org.ftc7244.robotcontroller.autonomous.Status;

import java.util.Map;

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
     * @param ms the duration to sleepWhile in milliseconds
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

    /**
     * Loads all needed hardware into code
     */
    public abstract void init();

    /**
     * Drives robot at a given power for a given amount of milliseconds
     * @param leftPower Power to drive left side of robot
     * @param rightPower Power to drive right side of robot
     * @param timeMillis Time to drive robot for
     * @throws InterruptedException of code fails to terminate on stop requested
     */
    public abstract void drive(double leftPower, double rightPower, long timeMillis) throws InterruptedException;

    /**
     * Drives robot at a certain power for an indefinite amount of time
     * @param leftPower Power to drive left side of robot
     * @param rightPower Power to drive right side of robot
     */
    public abstract void drive(double leftPower, double rightPower);

    /**
     * Drives robot at a given power to a given distance using encoders
     * @param power Power to drive robot at
     * @param inches Inches to drive robot
     */
    public abstract void driveToInch(double power, double inches);

    /**
     * Resets the direction and encoder offsets of all the drive motors
     */
    public abstract void resetDriveMotors();

    public double getCountsPerInch() {
        return countsPerInch;
    }

    /**
     *
     * @return returns the average value of all drive motor encoders
     */
    public abstract int getDriveEncoderAverage();

    public OpMode getOpMode() {
        return opMode;
    }

    public abstract void resetDriveEncoders();
}