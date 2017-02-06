package org.ftc7244.robotcontroller.autonomous.pid;

import org.ftc7244.robotcontroller.autonomous.Status;

import lombok.Getter;
import lombok.Setter;

import static java.lang.Double.isInfinite;
import static java.lang.Double.isNaN;

public class PIDController {

    @Getter
    @Setter
    private double kP, kI, kD;

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
        if (cycleTime == 0) {
            this.cycleTime = System.currentTimeMillis();
            return 0;
        }

        double error = setPoint - measured;
        proportional = kP * error;

        if ((integralRange == 0 || Math.abs(error) < integralRange)) integral += kI * error * dt;
        else integral = 0;

        derivative = kD * (error - previous_error) / dt;
        derivative = (isNaN(derivative) || isInfinite(derivative) ? 0 : derivative);

        previous_error = error;

        if (this.delay > 0)
            pause((long) (this.delay - (System.currentTimeMillis() - this.cycleTime)));
        dt = System.currentTimeMillis() - this.cycleTime;
        cycleTime = System.currentTimeMillis();

        double result = proportional + integral + derivative;
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
