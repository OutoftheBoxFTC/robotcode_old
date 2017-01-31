package org.ftc7244.robotcontroller.autonomous.pid.terminators;

import com.qualcomm.robotcore.util.RobotLog;

import org.ftc7244.robotcontroller.autonomous.pid.PIDDriveControl;

/**
 * Created by OOTB on 1/15/2017.
 */

public class SensitivityTerminator implements Terminator {

    private long timestamp, successDuration;
    private double maximumError, target;
    private PIDDriveControl context;

    public SensitivityTerminator(PIDDriveControl context, double target, double maximumError, long successDuration) {
        this.target = target;
        this.maximumError = maximumError;
        this.successDuration = successDuration;
        this.context = context;

        this.timestamp = -1;
    }

    public boolean shouldTerminate() {
        if (timestamp == -1 && Math.abs(context.getReading() - target) < maximumError)
            timestamp = System.currentTimeMillis();
        else if (Math.abs(context.getReading() - target) > maximumError) timestamp = -1;
        RobotLog.ii("STOP", context.getReading() + ":" + target);

        return Math.abs(System.currentTimeMillis() - timestamp) > successDuration && timestamp != -1;
    }
}
