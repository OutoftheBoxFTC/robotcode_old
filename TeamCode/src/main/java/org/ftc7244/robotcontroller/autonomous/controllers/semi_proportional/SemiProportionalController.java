package org.ftc7244.robotcontroller.autonomous.controllers.semi_proportional;

import org.ftc7244.robotcontroller.autonomous.controllers.ControlSystem;

/**
 * A proportional-only controller which acts within a range from the target. Otherwise, it acts at a constant power.
 */
public class SemiProportionalController extends ControlSystem {
    /**
     * This is the constant power outside of the proportional range, and the kp for a normalized
     * proportional value within the range. Throughout the robot's traveling through the proportional
     * range, the power will slowly drop from 100% of the base power, to 0%, aka stopping once its reached
     * the target.
     */
    private double basePower;

    /**
     * The range in which the power of the robot is proportional to the error
     */
    private double proportionalRange;

    /**
     * This is the offset from the initial target position at which the robot will attempt to reach.
     * The error will be shifted towards 0 by a factor of this value in order to account for external
     * factors such as drift or update time.
     */
    private double stopOffset;




    public SemiProportionalController(double basePower, double proportionalRange, double stopOffset) {
        this.basePower = basePower;
        this.proportionalRange = proportionalRange;
        this.stopOffset = stopOffset;
    }

    @Override
    public void reset() {
        //Nothing unique to a given process is remembered between iterations of the loop except set point
    }

    @Override
    public double update(double measured) {
        //derive error for use in further calcuations
        double error = setPoint-measured;
        //Decreases the error by the stop offset
        error += (error>0?-1:1)*stopOffset;
        //Use base power if outside the proportional range
        if(Math.abs(error)>=proportionalRange)
            return basePower;
        //Normalizes error in terms of proportional range
        double normalized = error/proportionalRange;
        //Normalized value acts as a percent of the base power
        double power = normalized * basePower;
        return -power;
    }
}