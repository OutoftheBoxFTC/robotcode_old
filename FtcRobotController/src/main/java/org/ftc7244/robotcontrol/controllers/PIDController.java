package org.ftc7244.robotcontrol.controllers;

import com.qualcomm.robotcore.util.RobotLog;

/**
 * Created by OOTB on 10/16/2016.
 */

public class PIDController {

    /**
     * Increase the  Kp until the output of the loop oscillates, then the kP should be set to
     * approximately half of that value for a "quarter amplitude decay" type response.
     */
    private double kP;
    /**
     * Increase kI until any offset is corrected in sufficient time for the process.
     * However, too much kI will cause instability.
     */
    private double kI;
    /**
     * Increase kD, if required, until the loop is acceptably quick to reach its reference after
     * a load disturbance. However, too much kD will cause excessive response and overshoot.
     */
    private double kD;

    /**
     * Information required to calculate each PID Loop
     */
    private double previous_error, integral;

    /**
     * The target value of the robot which will be used to calculate the correction value.
     */
    private double setPoint;

    /**
     * How often will the new PID value be calculated.
     */
    private double testInterval;

    public PIDController(double testInterval, double kP, double kI, double kD) {
        this.testInterval = testInterval;
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;

        this.previous_error = 0;
        this.integral = 0;
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
    public double[] update(double measured) {
        double error = setPoint - measured;

        integral = integral + error * testInterval;
        double derivative = (error - previous_error) / testInterval;
        previous_error = error;

        return new double[]{kP * error, kI * integral, kD * derivative, (kP * error) + (kI * integral) + (kD * derivative)};
    }

    /**
     * Reset the PID loop. Clearing all previous results
     */
    public void reset() {
        this.previous_error = 0;
        this.integral = 0;
    }
}
