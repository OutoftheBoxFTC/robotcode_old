package org.ftc7244.robotcontroller.autonomous.drivers;

import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.util.RobotLog;

import org.ftc7244.robotcontroller.Westcoast;
import org.ftc7244.robotcontroller.autonomous.Status;
import org.ftc7244.robotcontroller.autonomous.controllers.PIDController;
import org.ftc7244.robotcontroller.autonomous.controllers.PIDDriveControl;
import org.ftc7244.robotcontroller.autonomous.terminators.ConditionalTerminator;
import org.ftc7244.robotcontroller.autonomous.terminators.SensitivityTerminator;
import org.ftc7244.robotcontroller.autonomous.terminators.TerminationMode;
import org.ftc7244.robotcontroller.autonomous.terminators.Terminator;
import org.ftc7244.robotcontroller.autonomous.terminators.TimerTerminator;
import org.ftc7244.robotcontroller.sensor.accerometer.AccelerometerProvider;
import org.ftc7244.robotcontroller.sensor.gyroscope.GyroscopeProvider;

/**
 * This class is instrumental in the control of the robot and uses the gyroscope as a frame of
 * reference. It does not require that the gyroscope be the phone or external but uses it to
 * transform robot movement
 */
public class GyroscopeDrive extends PIDDriveControl {

    private GyroscopeProvider gyroProvider;

    /**
     * Same as the parent constructor but passes a debug as fault by default since most users will
     * not want to debug the code.
     *
     * @param robot access to motors on the robot
     * @param gyroProvider base way to read gyroscope values
     */
    public GyroscopeDrive(Westcoast robot, GyroscopeProvider gyroProvider) {
        super(new PIDController(0.02, 0.00004, 3.5, 30, 6, 0.8), robot);
        this.gyroProvider = gyroProvider;
    }

    @Override
    public double getReading() {
        return this.gyroProvider.getZ();
    }

    /**
     * This will combine the rotate function from the PID loop with a power offset. The power offset
     * then will then be added to the PID to get the drive. It is important to note that both motors
     * are reset before driving is started and will end once it reaches it's target in inches.
     *
     * @param power offset of the PID from -1 to 1
     * @param inches total distance to travel
     * @throws InterruptedException if code fails to terminate on stop requested
     */
    public void drive(double power, double inches) throws InterruptedException {
        final double ticks = inches * Westcoast.COUNTS_PER_INCH;
        Westcoast.resetMotors(robot.getDriveLeft(), robot.getDriveRight());
        if (inches <= 0) RobotLog.e("Invalid distances!");
        final int offset = getEncoderAverage();
        control(0, power, new Terminator() {
            @Override
            public boolean shouldTerminate() {
                return Math.abs(getEncoderAverage() - offset) >= ticks;
            }
        });
    }

    /**
     * This is similar to the ${@link #drive(double, double)} but is different because it has different
     * termination requirements. This method only takes power and which sensor to read for to know
     * when to terminate driving.
     *
     * @param power offset of the PID from -1 to 1
     * @param mode which sensor to use {@link Sensor#Leading} or {@link Sensor#Trailing}
     * @throws InterruptedException if code fails to terminate on stop requested
     */
    public void driveUntilLine(double power, Sensor mode) throws InterruptedException {
        driveUntilLine(power, mode, 0);
    }

    /**
     * This is similar to ${@link #driveUntilLine(double, Sensor)} but allows for one more parameter
     * allowing for more flexible control. The offset will only be triggered once the line is seen
     * and after that the robot will travel that set distance. There is a slight offset because of
     * sensor lag.
     *
     * @param power offset of the PID from -1 to 1
     * @param mode which sensor to use {@link Sensor#Leading} or {@link Sensor#Trailing}
     * @param offsetDistance distance after line to travel in inches
     * @throws InterruptedException if code fails to terminate on stop requested
     */
    public void driveUntilLine(double power, Sensor mode, double offsetDistance) throws InterruptedException {
        driveUntilLine(power, mode, offsetDistance, 0, 0);
    }

    /**
     * Both {@link #driveUntilLine(double, Sensor)} and {@link #driveUntilLine(double, Sensor, double)}
     * feet into this function but the main difference is that this will only execute parts of the
     * code if the paramters are within a certain range value range.
     *
     * @param power offset of the PID from -1 to 1
     * @param mode which sensor to use {@link Sensor#Leading} or {@link Sensor#Trailing}
     * @param offsetDistance distance after line to travel in inches
     * @param minDistance minimum distance before searching for line to prevent early triggering in inches
     * @param maxDistance maximum distance for searching to prevent overshoot in inches
     * @throws InterruptedException if code fails to terminate on stop requested
     */
    public void driveUntilLine(double power, Sensor mode, double offsetDistance, final double minDistance, final double maxDistance) throws InterruptedException {
        Westcoast.resetMotors(robot.getDriveLeft(), robot.getDriveRight());
        if (offsetDistance <= 0) RobotLog.e("Invalid distances!");
        final double ticks = offsetDistance * Westcoast.COUNTS_PER_INCH,
                maxTicks = maxDistance * Westcoast.COUNTS_PER_INCH,
                minTicks = minDistance * Westcoast.COUNTS_PER_INCH;
        final int encoderError = getEncoderAverage();

        control(0, power, new ConditionalTerminator(
                        new Terminator() {
                            @Override
                            public boolean shouldTerminate() {
                                return Math.abs(getEncoderAverage() - encoderError) >= maxTicks && maxTicks > 0;
                            }
                        },
                        new ConditionalTerminator(TerminationMode.AND,
                                new LineTerminator(mode, encoderError, ticks),
                                new Terminator() {
                                    @Override
                                    public boolean shouldTerminate() {
                                        return Math.abs(getEncoderAverage() - encoderError) > minTicks || minTicks <= 0;
                                    }
                                }
                        )
                )
        );
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
        double target = degrees + gyroProvider.getZ();
        control(target, 0, new ConditionalTerminator(new SensitivityTerminator(this, degrees, 1, 300), new TimerTerminator(2000)));
        resetOrientation();
    }

    /**
     * Resets the current orientation to 0 and waits till the change occurs
     * @throws InterruptedException if code fails to terminate on stop requested
     */
    public void resetOrientation() throws InterruptedException {
        do {
            gyroProvider.setZToZero();
            Thread.sleep(1);
        } while (Math.abs(Math.round(gyroProvider.getZ())) > 1);
    }

    private int getEncoderAverage() {
        return (robot.getDriveLeft().getCurrentPosition() + robot.getDriveRight().getCurrentPosition()) / 2;
    }

    /**
     * Which light sensor to use based off the the user input. This only allows the code to be more
     * streamlined in choice of sensor
     */
    public enum Sensor {
        Leading(0.3),
        Trailing(0.4);

        protected double white;

        Sensor(double white) {
            this.white = white;
        }
    }

    /**
     * Controls how far after the robot should drive if it passes the line and when to trigger that.
     * It also respects if the request to terminate was ignored and continues to only drive until
     * a specific set of parameters were fulfilled;
     */
    private class LineTerminator extends Terminator {

        private double driveAfterDistance, offset, encoderError;
        private LightSensor sensor;
        private double white;

        public LineTerminator(Sensor mode, double encoderError, double driveAfterDistance) {
            this.sensor = mode == Sensor.Trailing ? robot.getTrailingLight() : robot.getLeadingLight();
            this.driveAfterDistance = driveAfterDistance;
            this.white = mode.white;
            this.offset = 0;
            this.encoderError = encoderError;
        }

        @Override
        public boolean shouldTerminate() {
            if (sensor.getLightDetected() > white) {
                offset = getEncoderAverage();
            } else sensor.enableLed(true);

            return status();
        }

        private boolean status() {
            return Math.abs(getEncoderAverage() - encoderError - offset) >= driveAfterDistance && offset != 0;
        }

        @Override
        public void terminated(boolean status) {
            if (!status && status()) offset = 0;
            if (status) sensor.enableLed(false);

        }
    }
}
