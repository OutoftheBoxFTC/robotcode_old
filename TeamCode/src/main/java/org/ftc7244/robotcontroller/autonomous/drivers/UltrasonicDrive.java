package org.ftc7244.robotcontroller.autonomous.drivers;

import com.qualcomm.robotcore.util.RobotLog;

import org.ftc7244.robotcontroller.Westcoast;
import org.ftc7244.robotcontroller.autonomous.pid.Handler;
import org.ftc7244.robotcontroller.autonomous.pid.PIDController;
import org.ftc7244.robotcontroller.autonomous.pid.PIDDriveControl;
import org.ftc7244.robotcontroller.autonomous.pid.SensitivityTerminator;

import java.util.LinkedList;

/**
 * Created by OOTB on 1/15/2017.
 */

public class UltrasonicDrive extends PIDDriveControl implements UltrasonicDriveControls {

    private static final int offsetLeading = 0, offsetTrailing = 0;

    private LinkedList<Double> setLeading, setTrailing;
    private int memory;

    public UltrasonicDrive(Westcoast robot, boolean debug) {
        this(robot, 10, debug);
    }

    public UltrasonicDrive(Westcoast robot, int memory, boolean debug) {
        super(new PIDController(-0.1, -0.0008, 0, 30, .5, .1), robot, debug);
        this.memory = memory;
        this.setLeading = new LinkedList<>();
        this.setTrailing = new LinkedList<>();
    }

    @Override
    public double getReading() {
        double leading = robot.getLeadingUltrasonic().getUltrasonicLevel() - offsetLeading;
        double trailing = robot.getTrailingUltrasonic().getUltrasonicLevel() - offsetTrailing;

        RobotLog.ii("INFO", leading + ":" + trailing);
        return leading - trailing;
    }

    public void parallelize() throws InterruptedException {
        control(0, new SensitivityTerminator(this, 0, 0.1, 200));
    }

    public void driveParallel(final double power) throws InterruptedException {
        // FIXME: 1/15/2017
        control(0, new Handler() {
            long end = System.currentTimeMillis() + 3000;

            @Override
            public double offset() {
                return power;
            }

            @Override
            public boolean shouldTerminate() {
                return System.currentTimeMillis() > end;
            }
        });
    }

    private double update(double value, LinkedList<Double> list) {
        list.add(value);
        while (list.size() > memory) list.removeLast();

        double total = 0;
        for (Double x : list) total += x;
        return total / ((double) list.size());
    }
}
