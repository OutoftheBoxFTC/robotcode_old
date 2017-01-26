package org.ftc7244.robotcontroller.autonomous.pid;

import com.qualcomm.robotcore.util.RobotLog;

/**
 * Created by OOTB on 1/15/2017.
 */

public class SensitivityTerminator extends Handler {

    private long timestamp, successDuration, timeout, start;
    private double maximumError, target;
    private PIDDriveControl context;

    public SensitivityTerminator(PIDDriveControl context, double target, double maximumError, long successDuration) {
        this(context, target, maximumError, successDuration, -1);
    }

    public SensitivityTerminator(PIDDriveControl context, double target, double maximumError, long successDuration, long timeout) {
        this.target = target;
        this.maximumError = maximumError;
        this.successDuration = successDuration;
        this.context = context;
        this.timeout = timeout;

        this.timestamp = -1;
        this.start = -1;
    }

    public boolean shouldTerminate() {
        if (timestamp == -1 && Math.abs(context.getReading() - target) < maximumError)
            timestamp = System.currentTimeMillis();
        else if (Math.abs(context.getReading() - target) > maximumError) timestamp = -1;
        if (start == -1 && timeout > 0) start = System.currentTimeMillis();
        RobotLog.ii("STOP", context.getReading() + ":" + target);

        boolean result = Math.abs(System.currentTimeMillis() - timestamp) > successDuration && timestamp != -1;
        if (timeout > 0 && System.currentTimeMillis() > start + timeout) result = true;
        if (result) start = -1;
        return result;
    }
}
