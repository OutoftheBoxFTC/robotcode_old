package org.ftc7244.robotcontroller.autonomous.pid.drivers;

import com.qualcomm.robotcore.util.RobotLog;

import org.ftc7244.robotcontroller.Westcoast;
import org.ftc7244.robotcontroller.autonomous.pid.PIDController;
import org.ftc7244.robotcontroller.autonomous.pid.PIDDriveControl;
import org.ftc7244.robotcontroller.autonomous.pid.terminators.ConditionalTerminator;
import org.ftc7244.robotcontroller.autonomous.pid.terminators.SensitivityTerminator;
import org.ftc7244.robotcontroller.autonomous.pid.terminators.TimerTerminator;

/**
 * Because of limitations in the ultrasonic sensor moving and reading the values is not possible.
 * However it is still used to rotate to be parallel with the wall.
 */
public class UltrasonicDrive extends PIDDriveControl {

    private static final int offsetLeading = 0, offsetTrailing = 0;

    public UltrasonicDrive(Westcoast robot, boolean debug) {
        super(new PIDController(0.15, 0.0005, 0, 30, 0.75, .22), robot, debug);
    }

    @Override
    public double getReading() {
        double leading = robot.getLeadingUltrasonic().getUltrasonicLevel() - offsetLeading;
        double trailing = robot.getTrailingUltrasonic().getUltrasonicLevel() - offsetTrailing;

        RobotLog.ii("INFO", leading + ":" + trailing);
        return leading - trailing;
    }

    /**
     * Makes the robot parallel using the ultrasonic sensors however it will only run for 5
     * seconds before terminating. It expects to be within .1 of an inch of the wall for 200
     * milliseconds before it can continue
     *
     * @throws InterruptedException  if code fails to terminate on stop requested
     */
    public void parallelize() throws InterruptedException {
        control(0, 0, new ConditionalTerminator(new SensitivityTerminator(this, 0, 0.1, 200), new TimerTerminator(5000)));
    }
}
