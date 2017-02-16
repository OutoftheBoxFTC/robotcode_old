package org.ftc7244.robotcontroller.autonomous.terminators;

public abstract class Terminator {
    /**
     * This will tell the PIDDriveControl if the PID can stop executing since the robot has
     * reached a state that would be considered "completed"
     *
     * @return if the PID should end
     */
    public abstract boolean shouldTerminate();

    /**
     * Tells the robot the terminator if an outside source ended the code or the drive control
     * did not respect the request to terminate. This is optional since many classes do not use this.
     * This can also act as a "reset" tool.
     *
     * @param status if the code terminated
     */
    public void terminated(boolean status) {
    }
}
