package org.ftc7244.robotcontroller.autonomous.controllers.semi_proportional;

/**
 * A proportional-only controller used to correct for error. Similar to PID, except it
 * only acts proportional to the normalized error within a certain range from the error, otherwise
 * it drives at a given constant rate.
 */
public class SemiProportionalController {
    /**
     * This acts as the kp in the proportional range, as well as the constant power outside of it
     */
    private double basePower;
    /**
     * The range in which the power will be proportional to the error from the target
     */
    private double proportionalRange;

    /**
     * Indicates how far from the actual target to set power to 0. This is tuned to account for drift
     */
    private double stopOffset;

}
