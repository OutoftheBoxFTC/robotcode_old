package org.ftc7244.robotcontroller.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * This is a way of telling if the robot is executing a OpMode and if is not running an OpMode tell
 * all other threads or code to shut down. Acts as an easier alternative than passing around instances
 * of the {@link com.qualcomm.robotcore.eventloop.opmode.OpMode} code.
 */
public class Status {

    private static LinearOpMode autonomous;

    /**
     * Set the autonomous or clear it to be used in ${@link #isStopRequested()} to decide to
     * terminate other operations
     *
     * @param autonomous current autonomous
     */
    public static void setAutonomous(LinearOpMode autonomous) {
        Status.autonomous = autonomous;
    }

    /**
     * If there is an autonomous present and ${@link LinearOpMode#isStopRequested()} then
     * tell the code to terminate or if the thread is interrupted tell the code to terminate
     *
     * @return if stop was requested
     */
    public static boolean isStopRequested() {
        return (autonomous != null && autonomous.isStopRequested()) || Thread.interrupted();
    }


}
