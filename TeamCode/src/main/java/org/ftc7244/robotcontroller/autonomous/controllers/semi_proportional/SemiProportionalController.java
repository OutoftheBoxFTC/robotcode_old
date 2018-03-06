package org.ftc7244.robotcontroller.autonomous.controllers.semi_proportional;

import org.ftc7244.datalogger.Logger;
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
     *
     */
    private double minimumPower;




    public SemiProportionalController(double basePower, double proportionalRange, double minimumPower) {
        this.basePower = basePower;
        this.proportionalRange = proportionalRange;
        this.minimumPower = minimumPower;
    }

    @Override
    public void reset() {

    }

    @Override
    public double update(double measured) {
        //derive error for use in further calculations
        double error = setPoint-measured;
        //Use base power if outside the proportional range
        if(Math.abs(error)>=proportionalRange)
            return basePower;
        //Normalizes error in terms of proportional range
        double normalized = error/proportionalRange;
        //Takes measure of the direction the robot wants to turn
        double polarity = normalized<0?-1:1;
        //Calculate power based off proportional
        double power = normalized*basePower;

        if(Math.abs(power)<=minimumPower){
            //Calculate the minimum power in the correct direction
            power =  minimumPower*polarity;
        }
        Logger.getInstance().queueData("Error", error).
                             queueData("Power", power*180);
        return -power;
    }
}