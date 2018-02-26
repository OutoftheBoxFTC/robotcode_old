package org.ftc7244.robotcontroller.autonomous.controllers.semi_proportional;

public class SPControllerBuilder {
    private double basePower = 0;
    private double proportionalRange = 0;
    private double stopOffset = 0;

    /**
     * The base power serves 2 purposes. The first is to act as a constant power level outside of the
     * proportional range. Within it, it then acts as a weight for the proportional.
     * @param basePower The power constant and proportional factor
     * @return a copy of the builder
     */
    public SPControllerBuilder setBasePower(double basePower) {
        this.basePower = basePower;
        return this;
    }

    /**
     * The proportional range is the range in which the robot will act proportional to the offset from
     * its target. Outside the range, the robot will always act with a factor of 1 in terms of the
     * base power. Within the range. It will act with a factor proportional to the offset from the target
     * normalized in terms of the proportional range, put in terms of the base power.
     * @param proportionalRange The range in which the robot acts proportional to its error
     * @return A copy of the builder
     */
    public SPControllerBuilder setProportionalRange(double proportionalRange) {
        this.proportionalRange = proportionalRange;
        return this;
    }

    /**
     * The stop offset is the offset from the given target at which the robot will be told to stop. This
     * way, the robot will still have time to decelerate to a stop, rather than continue turning past the
     * target after the process has been stopped.
     * @param stopOffset The offset from the target at which the robot will stop
     * @return a copy of the controller
     */
    public SPControllerBuilder setStopOffset(double stopOffset) {
        this.stopOffset = stopOffset;
        return this;
    }

    public SemiProportionalController createController(){
        return new SemiProportionalController(basePower, proportionalRange, stopOffset);
    }
}
