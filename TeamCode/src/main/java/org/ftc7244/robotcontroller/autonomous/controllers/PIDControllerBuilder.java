package org.ftc7244.robotcontroller.autonomous.controllers;

public class PIDControllerBuilder {
    private double kP = 0;
    private double kI = 0;
    private double kD = 0;
    private double delay = -1;
    private double integralRange = 0;
    private double outputRange = 0;
    private boolean integralReset = false;

    public PIDControllerBuilder setProportional(double kP) {
        this.kP = kP;
        return this;
    }

    public PIDControllerBuilder setIntegral(double kI) {
        this.kI = kI;
        return this;
    }

    public PIDControllerBuilder setDerivative(double kD) {
        this.kD = kD;
        return this;
    }

    public PIDControllerBuilder setDelay(double delay) {
        this.delay = delay;
        return this;
    }

    public PIDControllerBuilder setIntegralRange(double integralRange) {
        this.integralRange = integralRange;
        return this;
    }

    public PIDControllerBuilder setOutputRange(double outputRange) {
        this.outputRange = outputRange;
        return this;
    }

    public PIDControllerBuilder setIntegralReset(boolean integralReset) {
        this.integralReset = integralReset;
        return this;
    }

    public PIDController createController() {
        return new PIDController(kP, kI, kD, delay, integralRange, outputRange, integralReset);
    }
}