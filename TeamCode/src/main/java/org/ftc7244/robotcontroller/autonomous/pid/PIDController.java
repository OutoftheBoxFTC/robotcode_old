package org.ftc7244.robotcontroller.autonomous.pid;

import static java.lang.Double.isInfinite;
import static java.lang.Double.isNaN;

/**
 * Created by OOTB on 10/16/2016.
 */

public class PIDController {

    private double kP, kI, kD;

    /**
     * Information required to calculate each PID Loop
     */
    private double previous_error, proportional, integral, derivative;


    private double setPoint;

    /**
     * How often will the new PID value be calculated.
     */
    private double dt, cycleTime, delay, integralRange, deadband;

    public PIDController(double kP, double kI, double kD) {
        this(kP, kI, kD, -1);
    }

    public PIDController(double kP, double kI, double kD, double delay) {
        this(kP, kI, kD, delay, 0, 0);
    }

    public PIDController(double kP, double kI, double kD, double delay, double integralRange, double deadband) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
        this.delay = delay;
        this.integralRange = integralRange;
        this.deadband = deadband;

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
        if (cycleTime == 0) {
            this.cycleTime = System.currentTimeMillis();
            return 0;
        }



        double error = setPoint - measured;
        if (deadband != 0 && Math.abs(error) < deadband) error = 0;
        proportional = kP * error;

        if ((integralRange == 0 || Math.abs(error) < integralRange) && (deadband == 0 || Math.abs(error) > deadband)) integral += kI * error * dt;
        else integral = 0;

        derivative = kD * (error - previous_error) / dt;
        derivative =(isNaN(derivative) || isInfinite(derivative) ? 0 : derivative);

        previous_error = error;

        if (this.delay > 0) pause((long) (this.delay - (System.currentTimeMillis() - this.cycleTime)));
        dt = System.currentTimeMillis() - this.cycleTime;
        cycleTime = System.currentTimeMillis();

        return proportional + integral + derivative;
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

    /**
     * Increase the  Kp until the output of the loop oscillates, then the kP should be set to
     * approximately half of that value for a "quarter amplitude decay" type response.
     */
    public double getkP() {
        return kP;
    }

    /**
     * Increase kI until any offset is corrected in sufficient time for the process.
     * However, too much kI will cause instability.
     */
    public double getkI() {
        return kI;
    }

    /**
     * Increase kD, if required, until the loop is acceptably quick to reach its reference after
     * a load disturbance. However, too much kD will cause excessive response and overshoot.
     */
    public double getkD() {
        return kD;
    }

    public double getProportional() {
        return proportional;
    }

    public double getIntegral() {
        return integral;
    }

    public double getDerivative() {
        return derivative;
    }

    /**
     * The target value of the robot which will be used to calculate the correction value.
     */
    public double getSetPoint() {
        return setPoint;
    }

    public double getDeadband() {
        return deadband;
    }

    public void setDeadband(double deadband) {
        this.deadband = deadband;
    }

    public double getIntegralRange() {
        return integralRange;
    }

    public void setIntegralRange(double integralRange) {
        this.integralRange = integralRange;
    }

    private void pause(long period) {
        if (period <= 0) return;
        long end = System.currentTimeMillis() + period;
        boolean interrupted = false;
        do {
            try {
                Thread.sleep(period);
            } catch (InterruptedException e) {
            }
            period = end - System.currentTimeMillis();
        } while (period > 0);
    }
}
