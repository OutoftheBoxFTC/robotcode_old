package org.ftc7244.robotcontroller.autonomous.controllers.pid;

public class PIDControllerBuilder {
    private double kP = 0;
    private double kI = 0;
    private double kD = 0;
    private double delay = -1;
    private double inverted = 1;
    private double integralRange = 0;
    private double outputRange = 0;
    private boolean integralReset = false;

    /**
     * This controls the amount of power directly correlated with the amount of error. The larger the
     * kP the larger of an impact error has on the result. Additionally, the larger the error the larger
     * the power value.
     *
     * @param kP ratio between power and error
     * @return a copy of the builder
     */
    public PIDControllerBuilder setProportional(double kP) {
        this.kP = kP;
        return this;
    }

    /**
     * The ratio between error and the amount added to the total integral value per every interval.
     * The larger the value the quicker the integral value will increase. The lower the value the
     * slower the value will build up. This is also dependent on the change in error on the impact
     * of the integral. For example, if the error changes in an exponential fashion the integral will
     * reflect a x^2 of the equation.
     *
     * @param kI the buildup value
     * @return a copy of the builder
     */
    public PIDControllerBuilder setIntegral(double kI) {
        this.kI = kI;
        return this;
    }

    /**
     * This controls the level of impact on dampening the proportional. This is helpful when dealing
     * with systems that move faster the control loop can handle. For example, if the a robot takes time
     * to slow down and has the possibility of exceeding the target this can dampen the change in
     * values.
     *
     * @param kD dampening value
     * @return a copy of the builder
     */
    public PIDControllerBuilder setDerivative(double kD) {
        this.kD = kD;
        return this;
    }

    /**
     * Refer to {@link #invert(boolean)}
     */
    public PIDControllerBuilder invert() {
        invert(true);
        return this;
    }

    /**
     * Determines whether all the values such as PID are inverted and will automatically multiply
     * their values by negative one instead of requiring the user to invert them.
     *
     * Inverts the {@link #setProportional(double)}, {@link #setDerivative(double)}, and
     * {@link #setIntegral(double)} values
     *
     * @param inverted whether the PID values are negative
     */
    public PIDControllerBuilder invert(boolean inverted) {
        this.inverted = inverted ? -1 : 1;
        return this;
    }

    /**
     * Every PID needs to follow a very strict timing in order to prevent adverse effects on the robot
     * as timing is crucial when using any of the control methods. This sets the response rate and
     * thus the amount of time between recalculating the PID / export. The loop will iterate in an open
     * methods if the value is not set otherwise it is a closed loop.
     * <p>
     * THIS SHOULD RARELY BE CHANGED BEYOND A VALUE OF 30
     *
     * @param delay the time between interval in milliseconds (30ms is recommended)
     * @return a copy of the builder
     */
    public PIDControllerBuilder setDelay(double delay) {
        this.delay = delay;
        return this;
    }

    /**
     * The range specifies the amount of error the loop can sustain until the integral is reset to
     * zero. This limits the integral from building too much when the proportional has a large
     * enough value to create an impact on its own. If you desire to keep the robot responding quicker
     * to error its generally better to increase the derivative versus enabling integral range. This is
     * more ideal for the situations where the proportional becomes ineffective and more power is desired
     * but also preventing a integral build up.
     *
     * @param integralRange the amount of error both negative and positive.
     * @return a copy of the builder
     */
    public PIDControllerBuilder setIntegralRange(double integralRange) {
        this.integralRange = integralRange;
        return this;
    }

    /**
     * This enables the PID loop to have a maximum power value so that the robot doeintes not exceed a
     * certain speed in which the sensor can no longer keep up. This is good for finding a balance
     * between speed and also overshoot.
     *
     * @param outputRange maximum power value between 0 and 1
     * @return a copy of the builder
     */
    public PIDControllerBuilder setOutputRange(double outputRange) {
        this.outputRange = outputRange;
        return this;
    }

    /**
     * Whenever the integral crosses the x axis reset the value to prevent overshoot.
     *
     * @param integralReset enable or disable
     * @return a copy of the builder
     */
    public PIDControllerBuilder setIntegralReset(boolean integralReset) {
        this.integralReset = integralReset;
        return this;
    }

    public PIDController createController() {
        return new PIDController(kP * inverted, kI * inverted, kD * inverted, delay, integralRange, outputRange, integralReset);
    }
}