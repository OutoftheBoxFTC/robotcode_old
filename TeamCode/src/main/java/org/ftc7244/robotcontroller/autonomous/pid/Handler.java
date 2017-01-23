package org.ftc7244.robotcontroller.autonomous.pid;

public abstract class Handler {
    public double offset() {
        return 0;
    }

    public abstract boolean shouldTerminate();
}