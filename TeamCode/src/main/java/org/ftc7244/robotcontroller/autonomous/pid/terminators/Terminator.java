package org.ftc7244.robotcontroller.autonomous.pid.terminators;

public abstract class Terminator {
    public abstract boolean shouldTerminate();

    public void terminated(boolean status) {
    }
}
