package org.ftc7244.robotcontroller.autonomous.pid.terminators;

/**
 * Created by FTC 7244 on 1/30/2017.
 */

public class ConditionalTerminator implements Terminator {

    private TerminationMode mode;
    private Terminator[] terminators;

    public ConditionalTerminator(Terminator... terminators) {
        this(TerminationMode.OR, terminators);
    }

    public ConditionalTerminator(TerminationMode mode, Terminator... terminators) {
        this.mode = mode;
        this.terminators = terminators;
    }

    @Override
    public boolean shouldTerminate() {
        switch (mode) {
            case OR:
                for (Terminator terminator : terminators) if (terminator.shouldTerminate()) return true;
                return false;
            case AND:
                for (Terminator terminator : terminators) if (!terminator.shouldTerminate()) return false;
                return true;
            default:
                return true;
        }
    }
}
