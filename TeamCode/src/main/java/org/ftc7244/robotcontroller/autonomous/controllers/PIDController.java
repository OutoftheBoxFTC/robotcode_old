package org.ftc7244.robotcontroller.autonomous.controllers;

import org.ftc7244.robotcontroller.autonomous.Status;

import lombok.Getter;
import lombok.Setter;

import static java.lang.Double.isInfinite;
import static java.lang.Double.isNaN;

/**
 * Simple PID controller used to respond to a input accordingly. Certain custom features like Integral
 * Range and Output Range were incorporated to support more intelligent drive support.
 */
public class PIDController {

    /**
     * This is the coefficient used to calculate the effect of the proportional of the robot.
     *
     * Increase the P gain until the response to a disturbance is steady oscillation.
     */
    @Getter
    @Setter
    private double kP;

    /**
     * This is the coefficient used to calculate the effect of the integral of the robot.
     *
     * Increase the I gain until it brings you to the setpoint with the number of oscillations
     * desired (normally zero but a quicker response can be had if you don't mind a couple
     * oscillations of overshoot)
     */
    @Getter
    @Setter
    private double kI;

    /**
     * This is the coefficient used to calculate the effect of the derivative of the robot.
     *
     * Increase the D gain until the the oscillations go away (i.e. it's critically damped).
     */
    @Getter
    @Setter
    private double kD;

    @Getter
    private double proportional, integral, derivative;

    /**
     * Information required to calculate each PID Loop
     */
    private double previous_error;


    @Getter
    @Setter
    private double setPoint, delay, integralRange, outputRange;

    /**
     * How often will the new PID value be calculated.
     */
    private double dt, cycleTime;

    public PIDController(double kP, double kI, double kD) {
        this(kP, kI, kD, -1);
    }

    public PIDController(double kP, double kI, double kD, double delay) {
        this(kP, kI, kD, delay, 0, 0);
    }

    public PIDController(double kP, double kI, double kD, double delay, double integralRange, double outputRange) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
        this.delay = delay;
        this.integralRange = integralRange;
        this.outputRange = Math.abs(outputRange);

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
            previous_error = error;
            return 0;
        }

        //calculate error and then find proprtional through adjusting
        proportional = kP * error;

        //check if integral is in range otherwise zero it out
        if ((integralRange == 0 || Math.abs(error) < integralRange)) integral += kI * error * dt;
        else integral = 0;

        //calculate derivative and then increase it by its kD
        derivative = kD * (error - previous_error) / dt;
        //sanity check to prevent errors in the derivative
        derivative = (isNaN(derivative) || isInfinite(derivative) ? 0 : derivative);

        //save previous error for next integral
        previous_error = error;

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
        this.previous_error = 0;
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
}
