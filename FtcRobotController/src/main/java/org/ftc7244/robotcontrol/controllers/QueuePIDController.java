package org.ftc7244.robotcontrol.controllers;

import com.qualcomm.robotcore.util.RobotLog;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by OOTB on 10/16/2016.
 */

public class QueuePIDController extends PIDController {

    /**
     * Results given back from the PID loop stored by when they were inserted into
     * the queue
     */
    private Queue<Double> results;

    public QueuePIDController(double testInterval, double kP, double kI, double kD) {
        super(testInterval, kP, kI, kD);
        this.results = new ConcurrentLinkedQueue<>();
    }

    /**
     * Returns the updated value as well as adding it to a queue to be read later if the code
     * is ran on two separate threads
     *
     * @param measured what is the measured value? This will give us info based off the target
     * @return
     */
    @Override
    public double update(double measured) {
        double output = super.update(measured);
        results.add(output);
        return output;
    }

    /**
     * Gets the next value in the updated PID loop and removes it from the queue
     *
     * @return next value in the PID
     */
    public double getNextValue() {
        return results.poll();
    }

    /**
     * If the queue is not empty then get the next value in the queue
     *
     * @return is the queue not empty
     */
    public boolean hasValue() {
       return results.isEmpty();
    }
}
