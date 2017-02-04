package org.ftc7244.robotcontroller.autonomous.pid.terminators;

/**
 * Created by FTC 7244 on 1/30/2017.
 */

public class TimerTerminator extends Terminator {

    private long timeout, start;

    public TimerTerminator(long timeout) {
        this.timeout = timeout;
        this.start = -1;
    }

    @Override
    public boolean shouldTerminate() {
        if (start == -1 && timeout > 0) start = System.currentTimeMillis();
        return timeout > 0 && System.currentTimeMillis() > start + timeout;
    }

    @Override
    public void terminated(boolean status) {
        if (status) start = -1;
    }
}
