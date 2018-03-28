package org.ftc7244.robotcontroller.autonomous.terminators;

import org.ftc7244.robotcontroller.autonomous.controllers.DriveControl;

/**
 * Created by ftc72 on 3/26/2018.
 */

public class RangeTerminator extends Terminator {
    DriveControl context;
    double target, maximumError;
    public RangeTerminator(DriveControl context, double target, double maximumError){
        this.context = context;
        this.target = target;
        this.maximumError = maximumError;
    }
    @Override
    public boolean shouldTerminate() {
        return Math.abs(target - context.getReading()) < maximumError;
    }
}
