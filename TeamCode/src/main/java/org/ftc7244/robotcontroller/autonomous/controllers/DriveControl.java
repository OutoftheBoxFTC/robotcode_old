package org.ftc7244.robotcontroller.autonomous.controllers;

import android.support.annotation.NonNull;

import com.qualcomm.robotcore.util.RobotLog;

import org.ftc7244.robotcontroller.autonomous.ControlSystemAutonomous;
import org.ftc7244.robotcontroller.autonomous.Status;
import org.ftc7244.robotcontroller.autonomous.terminators.Terminator;
import org.ftc7244.robotcontroller.hardware.Hardware;


/**
 * Abstract tool that handles a majority of the Control System when driving and handles when the loop should
 * terminate based off of ${@link Terminator}. Furthermore, it takes a control system that has its own specifications
 * and a sensor with readings to respond with the ${@link #control(double, double, Terminator)} function.
 */
public abstract class DriveControl {

    private ControlSystem controller;
    protected Hardware robot;

    private ControlSystemAutonomous.SleepTask controlSubTask;

    public DriveControl(ControlSystem controller, Hardware robot) {
        this.controller = controller;
        this.robot = robot;
        controlSubTask = null;
    }

    /**
     * Return the value of the sensor so the control loop knows how to respond
     *
     * @return double of the current value
     */
    public abstract double getReading();

    /**
     * Resets the control loop then sets the target. After every loop update the current thread is paused
     * until the looping count is matched. It will also update the terminators with termination status
     * and requests to terminate unless the code is stopped otherwise
     * <p>
     * There are many different terminators that can be used: ${@link org.ftc7244.robotcontroller.autonomous.terminators.ConditionalTerminator},
     * ${@link org.ftc7244.robotcontroller.autonomous.terminators.SensitivityTerminator}, ${@link org.ftc7244.robotcontroller.autonomous.terminators.TimerTerminator}
     *
     * @param target      the target value for the sensor
     * @param powerOffset power level from -1 to 1 to convert a rotate function to a drive function
     * @param terminator  tells the control loop when to stop
     * @throws InterruptedException if the code fails to stop on finish request
     */
    protected void control(double target, double powerOffset, @NonNull Terminator terminator) throws InterruptedException {
        //setup the control loop
        controller.reset();
        controller.setTarget(target);

        do {
            //tell the terminators the code has yet to finish
            terminator.terminated(false);
            //get control system correction value
            double correction = controller.update(getReading());
            robot.getOpMode().telemetry.addData("Error", getReading());
            robot.getOpMode().telemetry.update();
            RobotLog.i(getReading() + ":" + correction);
            //debug if wanted
            //take the correction and provide poweroffset
            robot.drive(powerOffset + correction, powerOffset - correction);
            //check if the robot should stop driving
            if(controlSubTask != null)controlSubTask.integrate();
        } while (!terminator.shouldTerminate() && !Status.isStopRequested());
        terminator.terminated(true);
        if(controlSubTask != null)controlSubTask.stop();
        //kill motors just in case
        robot.drive(0, 0);
    }

    public DriveControl setControlSubTask(ControlSystemAutonomous.SleepTask controlSubTask) {
        this.controlSubTask = controlSubTask;
        return this;
    }
}
