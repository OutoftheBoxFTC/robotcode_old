package org.ftc7244.robotcontroller.autonomous.terminators;

import com.qualcomm.robotcore.util.RobotLog;

import org.ftc7244.robotcontroller.Debug;
import org.ftc7244.robotcontroller.autonomous.controllers.DriveControl;

/**
 * Once the control loop has reached a certain level of accuracy the sensitivity terminator will trigger
 * after a time period is passed to prevent early termination of the object passes the target value
 * and over compensated
 */
public class SensitivityTerminator extends Terminator {

    private long timestamp, successDuration;
    private double maximumError, target;
    private DriveControl context;

    /**
     * Requires the control system context to know if the values are in the target value and if they are within
     * a maximum amount of error kill the control loop.
     *
     * @param context         the Control system being used in the ${@link DriveControl}
     * @param target          the target value of the rotation
     * @param maximumError    the absolute value of error the control system can have
     * @param successDuration how long after the target value must the target value retain before terminating
     */
    public SensitivityTerminator(DriveControl context, double target, double maximumError, long successDuration) {
        this.target = target;
        this.maximumError = maximumError;
        this.successDuration = successDuration;
        this.context = context;

        this.timestamp = -1;
    }

    @Override
    public boolean shouldTerminate() {
        if (timestamp == -1 && Math.abs(context.getReading() - target) < maximumError)
            timestamp = System.currentTimeMillis();
        else if (Math.abs(context.getReading() - target) > maximumError) timestamp = -1;
        if (Debug.STATUS) RobotLog.ii("STOP", context.getReading() + ":" + target);

        return Math.abs(System.currentTimeMillis() - timestamp) > successDuration && timestamp != -1;
    }

    @Override
    public void terminated(boolean status) {
        if (status) timestamp = -1;
    }
}