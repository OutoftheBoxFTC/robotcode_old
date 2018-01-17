package org.ftc7244.robotcontroller.autonomous.drivers;

import com.qualcomm.robotcore.util.RobotLog;

import org.ftc7244.robotcontroller.autonomous.controllers.PIDControllerBuilder;
import org.ftc7244.robotcontroller.autonomous.controllers.PIDDriveControl;
import org.ftc7244.robotcontroller.autonomous.terminators.ConditionalTerminator;
import org.ftc7244.robotcontroller.autonomous.terminators.SensitivityTerminator;
import org.ftc7244.robotcontroller.autonomous.terminators.Terminator;
import org.ftc7244.robotcontroller.autonomous.terminators.TimerTerminator;
import org.ftc7244.robotcontroller.hardware.Hardware;
import org.ftc7244.robotcontroller.hardware.Westcoast;
import org.ftc7244.robotcontroller.sensor.gyroscope.GyroscopeProvider;

/**
 * This class is instrumental in the control of the robot and uses the gyroscope as a frame of
 * reference. It does not require that the gyroscope be the phone or external but uses it to
 * transform robot movement
 */
public class GyroscopeDrive extends PIDDriveControl {

    protected GyroscopeProvider gyroProvider;
    protected Hardware robot;
    private double target;

    /**
     * Same as the parent constructor but passes a debug as fault by default since most users will
     * not want to debug the code.
     *
     * @param robot        access to motors on the robot
     * @param gyroProvider base way to read gyroscope values
     */
    public GyroscopeDrive(Hardware robot, GyroscopeProvider gyroProvider) {
        super(new PIDControllerBuilder()
                        .invert()
                        .setProportional(0.012)
                        .setIntegral(0.00007)
                        .setDerivative(1.64)
                        .setIntegralRange(15)
                        .setIntegralReset(true)
                        .setOutputRange(0.4)
                        .setDelay(20)
                        .createController(),
                robot);
        this.gyroProvider = gyroProvider;
        this.robot = robot;
        this.target = 0;
    }

    /**
     * This expands the range of gyrosope input from [-180, 180], to [-540, 540]
     * @return gyroscope input
     */

    @Override
    public double getReading() {
        double reading = this.gyroProvider.getZ();
        if (Math.abs(target) > 160) {
            if (target > 0 && reading < 0) {
                return 360 + reading;
            } else if (target < 0 && reading > 0) {
                return -360 + reading;
            }
        }
       return reading;
    }

    public void drive(double power, double inches) throws InterruptedException {
        drive(power, inches, 0);
    }

    /**
     * This will combine the rotate function from the PID loop with a power offset. The power offset
     * then will then be added to the PID to get the drive. It is important to note that both motors
     * are reset before driving is started and will end once it reaches it's target in inches.
     *
     * @param power  offset of the PID from -1 to 1
     * @param inches total distance to travel
     * @throws InterruptedException if code fails to terminate on stop requested
     */
    public void drive(double power, double inches, double target) throws InterruptedException {
        final double ticks = inches * Westcoast.COUNTS_PER_INCH;
        this.target = 0;
        robot.resetDriveMotors();
        if (inches <= 0) RobotLog.e("Invalid distances!");
        final int offset = robot.getDriveEncoderAverage();
        control(target, power, new Terminator() {
            @Override
            public boolean shouldTerminate() {
                return Math.abs(robot.getDriveEncoderAverage() - offset) >= ticks;
            }
        });
    }


    /**
     * This will rotate the robot until it is within 2 degrees for 300 milliseconds. It will also
     * manually terminate if the rotate takes longer than two seconds. This is important because
     * in certain scenarios the robot can be stuck and be unable to complete the rotation.
     *
     * @param degrees target orientation in degrees
     * @throws InterruptedException if code fails to terminate on stop requested
     */
    public void rotate(double degrees) throws InterruptedException {
        this.target = degrees;
        control(degrees, 0, new ConditionalTerminator(new SensitivityTerminator(this, degrees, 1, 300), new TimerTerminator(6000)));
        resetOrientation();
    }

    /**
     * Resets the current orientation to 0 and waits till the change occurs
     *
     * @throws InterruptedException if code fails to terminate on stop requested
     */
    public void resetOrientation() throws InterruptedException {
        do {
            gyroProvider.setZToZero();
        } while (Math.abs(Math.round(gyroProvider.getZ())) > 1);
    }

    /**
     * Which light sensor to use based off the the user input. This only allows the code to be more
     * streamlined in choice of sensor
     */
    public enum Sensor {
        Leading(0.3),
        Trailing(0.3);

        protected double white;

        Sensor(double white) {
            this.white = white;
        }
    }


}
