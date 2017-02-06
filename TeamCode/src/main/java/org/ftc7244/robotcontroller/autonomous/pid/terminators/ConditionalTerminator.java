package org.ftc7244.robotcontroller.autonomous.pid.terminators;

public class ConditionalTerminator extends Terminator {

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
                for (Terminator terminator : terminators)
                    if (terminator.shouldTerminate()) return true;
                return false;
            case AND:
                for (Terminator terminator : terminators)
                    if (!terminator.shouldTerminate()) return false;
                return true;
            default:
                return true;
        }
    }

    @Override
    public void terminated(boolean status) {
        for (Terminator terminator : terminators) terminator.terminated(status);
    }
}
