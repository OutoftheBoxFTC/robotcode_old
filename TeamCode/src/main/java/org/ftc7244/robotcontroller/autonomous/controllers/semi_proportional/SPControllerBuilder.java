package org.ftc7244.robotcontroller.autonomous.controllers.semi_proportional;

public class SPControllerBuilder {
    private double basePower = 0;
    private double proportionalRange = 0;
    private double minimumPower = 0;

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
     */
    public SPControllerBuilder setMinimumPower(double minimumPower) {
        this.minimumPower = minimumPower;
        return this;
    }

    public SemiProportionalController createController(){
        return new SemiProportionalController(basePower, proportionalRange, minimumPower);
    }
}
