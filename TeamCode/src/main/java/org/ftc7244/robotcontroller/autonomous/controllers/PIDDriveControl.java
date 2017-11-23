package org.ftc7244.robotcontroller.autonomous.controllers;

import android.support.annotation.NonNull;

import com.qualcomm.robotcore.util.RobotLog;

import org.ftc7244.datalogger.Logger;
import org.ftc7244.robotcontroller.Debug;
import org.ftc7244.robotcontroller.autonomous.Status;
import org.ftc7244.robotcontroller.autonomous.terminators.Terminator;
import org.ftc7244.robotcontroller.hardware.Hardware;


/**
 * Abstract tool that handles a majority of the PID when driving and handles when the PID should
 * terminate based off of ${@link Terminator}. Furthermore, it takes a PID that has its own tunings
 * and a sensor with readings to respond with the ${@link #control(double, double, Terminator)} function.
 */
public abstract class PIDDriveControl {

    protected PIDController controller;
    protected Hardware robot;

    public PIDDriveControl(PIDController controller, Hardware robot) {
        this.controller = controller;
        this.robot = robot;
    }

    /**
     * Return the value of the sensor so the PID loop knows how to respond
     *
     * @return double of the current value
     */
    public abstract double getReading();

    /**
     * Resets the PID loop then sets the target. After every PID update the current thread is paused
     * until the looping count is matched. It will also update the terminators with termination status
     * and requests to terminate unless the code is stopped otherwise
     * <p>
     * There are many different terminators that can be used: ${@link org.ftc7244.robotcontroller.autonomous.terminators.ConditionalTerminator},
     * ${@link org.ftc7244.robotcontroller.autonomous.terminators.SensitivityTerminator}, ${@link org.ftc7244.robotcontroller.autonomous.terminators.TimerTerminator}
     *
     * @param target      the target value for the sensor
     * @param powerOffset power level from -1 to 1 to convert a rotate function to a drive function
     * @param terminator  tells the PID when to end
     * @throws InterruptedException if the code fails to end on finish request
     */
    protected void control(double target, double powerOffset, @NonNull Terminator terminator) throws InterruptedException {
        //setup the PID loop
        controller.reset();
        controller.setTarget(target);

        do {
            //tell the terminators the code has yet to finish
            terminator.terminated(false);
            //get PID correction value
            double pid = controller.update(getReading());
            RobotLog.i(getReading() + ":" + pid);
            //debug if wanted
            if (Debug.STATUS) {
                Logger.getInstance()
                        .queueData("P", controller.getProportional())
                        .queueData("I", controller.getIntegral())
                        .queueData("D", controller.getDerivative())
                        .queueData("Sum", pid)
                        .queueData("Reading", getReading());
            }

            robot.getOpMode().telemetry.addData("Gyro", getReading());
            robot.getOpMode().telemetry.update();
            //take the PID and provide poweroffset if the robot wants to drive while using PID
            robot.drive(powerOffset + pid, powerOffset - pid);
            //check if the robot should stop driving

        } while (!terminator.shouldTerminate() && !Status.isStopRequested());
        terminator.terminated(true);

        //kill motors just in case
        robot.drive(0, 0);
    }
}
