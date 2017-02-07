package org.ftc7244.robotcontroller.autonomous.terminators;

/**
 * After a certain duration the terminator will trigger. This acts as a psuedo timeout terminator
 * and is good if something is preventing the PID from reaching its goal
 */
public class TimerTerminator extends Terminator {

    private long timeout, start;

    /**
     * @param timeout the duration before the PID timesout
     */
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
