package org.ftc7244.robotcontroller.autonomous.terminators;

/**
 * Is a way of mixing and matching terminators so that they can be conditional and allowing for
 * more than one terminator.
 */
public class ConditionalTerminator extends Terminator {

    private TerminationMode mode;
    private Terminator[] terminators;

    /**
     * It by default uses ${@link TerminationMode#OR} and allows for a dynmaic amount of terminators
     * to decided if the code should stop
     *
     * @param terminators dynamic amount of terminators
     */
    public ConditionalTerminator(Terminator... terminators) {
        this(TerminationMode.OR, terminators);
    }

    /**
     * Similar to the single argument but allows for changing of modes.
     * <p>
     * There are different supported modes:
     * ${@link TerminationMode#OR} will stop if any are true
     * ${@link TerminationMode#AND} will stop if ONLY if all true
     *
     * @param mode        the mode to use
     * @param terminators dynamic amount of terminators
     */
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
