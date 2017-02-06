package org.ftc7244.robotcontroller.autonomous.pid.terminators;

/**
 * Created by FTC 7244 on 1/30/2017.
 */

public abstract class Terminator {
    public abstract boolean shouldTerminate();

    public void terminated(boolean status) {
    }
}
