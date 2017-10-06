package org.ftc7244.robotcontroller.autonomous.controllers;

import com.qualcomm.robotcore.util.RobotLog;

import org.ftc7244.robotcontroller.autonomous.Status;

import static java.lang.Double.isInfinite;
import static java.lang.Double.isNaN;

/**
 * Simple PID controller used to respond to a input accordingly. Certain custom features like Integral
 * Range and Output Range were incorporated to support more intelligent drive support.
 */
public class PIDController {

    /**
     * This is the coefficient used to calculate the effect of the proportional of the robot.
     * <p>
     * Increase the P gain until the response to a disturbance is steady oscillation.
     */
    private double kP;

    /**
     * This is the coefficient used to calculate the effect of the integral of the robot.
     * <p>
     * Increase the I gain until it brings you to the setpoint with the number of oscillations
     * desired (normally zero but a quicker response can be had if you don't mind a couple
     * oscillations of overshoot)
     */
    private double kI;

    /**
     * This is the coefficient used to calculate the effect of the derivative of the robot.
     * <p>
     * Increase the D gain until the the oscillations go away (i.e. it's critically damped).
     */
    private double kD;

    private double proportional, integral, derivative;

    /**
     * Information required to calculate each PID Loop
     */
    private double previousError;


    private double setPoint, delay, integralRange, outputRange;

    /**
     * How often will the new PID value be calculated.
     */
    private double dt, cycleTime;

    private boolean integralReset;

    public PIDController(double kP, double kI, double kD, double delay, double integralRange, double outputRange, boolean integralReset) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
        this.delay = delay;
        this.integralRange = integralRange;
        this.outputRange = Math.abs(outputRange);
        this.integralReset = integralReset;

        reset();
    }

    /**
     * Update the target value of the PID loop so that the algorithm can respond in a new way
     * the new target value will be used in the queue.
     *
     * @param target the new value
     */
    public void setTarget(double target) {
        this.setPoint = target;
    }

    /**
     * Update PID loop based off previous results. This number will be stored in a queue. As well as
     * being returned to the user
     *
     * @param measured what is the measured value? This will give us info based off the target
     * @return the error correction value from the PID loop
     */
    public double update(double measured) {
        //grab the error for caching or use in other calculates
        double error = setPoint - measured;

        //if the PID has yet to execute more than once grab a timestamp to use in the future
        if (cycleTime == 0) {
            this.cycleTime = System.currentTimeMillis();
            previousError = error;
            return 0;
        }

        //calculate error and then find proprtional through adjusting
        proportional = kP * error;

        //check if integral is in range otherwise zero it out
        if ((integralRange == 0 || Math.abs(error) < integralRange)) integral += kI * error * dt;
        else integral = 0;

        double previousPosition = (previousError<0?-1:previousError>0?1:0) , currentPosition = (error<0?-1:error>0?1:0);
        if (previousPosition != currentPosition) {
            integral = 0;
            RobotLog.ii("RESET", "Reset Integral");
        }
        //calculate derivative and then increase it by its kD
        derivative = kD * (error - previousError) / dt;
        //sanity check to prevent errors in the derivative
        derivative = (isNaN(derivative) || isInfinite(derivative) ? 0 : derivative);

        //save previous error for next integral
        previousError = error;

        //ensure that the PID is only calculating at certain intervals otherwise halt till next time
        if (this.delay > 0)
            pause((long) (this.delay - (System.currentTimeMillis() - this.cycleTime)));
        dt = System.currentTimeMillis() - this.cycleTime;
        cycleTime = System.currentTimeMillis();

        //calculate the PID result
        double result = proportional + integral + derivative;
        //limit the PID result if range is present
        if (outputRange != 0) result = Math.max(-outputRange, Math.min(outputRange, result));
        return result;
    }

    /**
     * Reset the PID loop. Clearing all previous results
     */
    public void reset() {
        this.previousError = 0;
        this.integral = 0;
        this.dt = 0;
        this.cycleTime = 0;
        this.proportional = 0;
    }

    private void pause(long period) {
        if (period <= 0) return;
        long end = System.currentTimeMillis() + period;
        do {
            try {
                Thread.sleep(period);
            } catch (InterruptedException e) {
                //ignore
            }
            period = end - System.currentTimeMillis();
        } while (period > 0 && !Status.isStopRequested());
    }

    public double getKP() {
        return this.kP;
    }

    public void setKP(double kP) {
        this.kP = kP;
    }

    public double getKI() {
        return this.kI;
    }

    public void setKI(double kI) {
        this.kI = kI;
    }

    public double getKD() {
        return this.kD;
    }

    public void setKD(double kD) {
        this.kD = kD;
    }

    public double getProportional() {
        return this.proportional;
    }

    public double getIntegral() {
        return this.integral;
    }

    public double getDerivative() {
        return this.derivative;
    }

    public double getSetPoint() {
        return this.setPoint;
    }

    public void setSetPoint(double setPoint) {
        this.setPoint = setPoint;
    }

    public double getDelay() {
        return this.delay;
    }

    public void setDelay(double delay) {
        this.delay = delay;
    }

    public double getIntegralRange() {
        return this.integralRange;
    }

    public void setIntegralRange(double integralRange) {
        this.integralRange = integralRange;
    }

    public double getOutputRange() {
        return this.outputRange;
    }

    public void setOutputRange(double outputRange) {
        this.outputRange = outputRange;
    }

}
