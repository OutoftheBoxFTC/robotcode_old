package org.ftc7244.robotcontroller.autonomous.controllers;

import org.ftc7244.robotcontroller.autonomous.Status;

public abstract class ControlSystem {

    protected double setPoint;


    public abstract void reset();


    /**
     * Update the target value of the Control loop so that the algorithm can respond in a new way
     * the new target value will be used in the queue.
     *
     * @param target the new value
     */
    public void setTarget(double target) {
        this.setPoint = target;
    }

    /**
     * Update Control loop based off previous results. This number will be stored in a queue. As well as
     * being returned to the user
     *
     * @param measured what is the measured value? This will give us info based off the target
     * @return the error correction value from the Control loop
     */
    public abstract double update(double measured);

    protected void pause(long period) {
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
