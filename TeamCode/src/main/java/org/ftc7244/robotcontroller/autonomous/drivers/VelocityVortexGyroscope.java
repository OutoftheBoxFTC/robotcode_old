package org.ftc7244.robotcontroller.autonomous.drivers;

import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.util.RobotLog;

import org.ftc7244.robotcontroller.Debug;
import org.ftc7244.robotcontroller.autonomous.terminators.ConditionalTerminator;
import org.ftc7244.robotcontroller.autonomous.terminators.TerminationMode;
import org.ftc7244.robotcontroller.autonomous.terminators.Terminator;
import org.ftc7244.robotcontroller.hardware.Hardware;
import org.ftc7244.robotcontroller.hardware.VelocityVortexWestcoast;
import org.ftc7244.robotcontroller.sensor.gyroscope.GyroscopeProvider;

/**
 * Created by FTC 7244 on 11/1/2017.
 */

public class VelocityVortexGyroscope extends GyroscopeDrive{
    private VelocityVortexWestcoast robot;
    public VelocityVortexGyroscope(VelocityVortexWestcoast robot, GyroscopeProvider provider){
        super(robot, provider);
        this.robot = robot;
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

        /**
         * Both {@link #driveUntilLine(double, Sensor)} and {@link #driveUntilLine(double, Sensor, double)}
         * feet into this function but the main difference is that this will only execute parts of the
         * code if the paramters are within a certain range value range.
         *
         * @param power          offset of the PID from -1 to 1
         * @param mode           which sensor to use {@link Sensor#Leading} or {@link Sensor#Trailing}
         * @param offsetDistance distance after line to travel in inches
         * @param minDistance    minimum distance before searching for line to prevent early triggering in inches
         * @param maxDistance    maximum distance for searching to prevent overshoot in inches
         * @throws InterruptedException if code fails to terminate on stop requested
         */
        public void driveUntilLine(double power, Sensor mode, double offsetDistance, final double minDistance, final double maxDistance) throws InterruptedException {
            robot.resetDriveMotors();
            if (offsetDistance <= 0) RobotLog.e("Invalid distances!");
            final double ticks = offsetDistance * VelocityVortexWestcoast.COUNTS_PER_INCH,
                    maxTicks = maxDistance * VelocityVortexWestcoast.COUNTS_PER_INCH,
                    minTicks = minDistance * VelocityVortexWestcoast.COUNTS_PER_INCH;
            final int encoderError = robot.getDriveEncoderAverage();

            control(0, power, new ConditionalTerminator(
                            new Terminator() {
                                @Override
                                public boolean shouldTerminate() {
                                    return Math.abs(robot.getDriveEncoderAverage() - encoderError) >= maxTicks && maxTicks > 0;
                                }
                            },
                            new ConditionalTerminator(TerminationMode.AND,
                                    new LineTerminator(mode, encoderError, ticks),
                                    new Terminator() {
                                        @Override
                                        public boolean shouldTerminate() {
                                            return Math.abs(robot.getDriveEncoderAverage() - encoderError) > minTicks || minTicks <= 0;
                                        }
                                    }
                            )
                    )
            );
        }

        /**
         * This is similar to the ${@link #drive(double, double)} but is different because it has different
         * termination requirements. This method only takes power and which sensor to read for to know
         * when to terminate driving.
         *
         * @param power offset of the PID from -1 to 1
         * @param mode  which sensor to use {@link Sensor#Leading} or {@link Sensor#Trailing}
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
         * @param power          offset of the PID from -1 to 1
         * @param mode           which sensor to use {@link Sensor#Leading} or {@link Sensor#Trailing}
         * @param offsetDistance distance after line to travel in inches
         * @throws InterruptedException if code fails to terminate on stop requested
         */
        public void driveUntilLine(double power, Sensor mode, double offsetDistance) throws InterruptedException {
            driveUntilLine(power, mode, offsetDistance, 0, 0);
        }

        @Override
        public boolean shouldTerminate() {
            if (Debug.STATUS) RobotLog.ii("Light", sensor.getLightDetected() + "");
            if (sensor.getLightDetected() > white) {
                offset = robot.getDriveEncoderAverage();
            } else sensor.enableLed(true);

            return status();
        }

        private boolean status() {
            return Math.abs(robot.getDriveEncoderAverage() - encoderError - offset) >= driveAfterDistance && offset != 0;
        }

        @Override
        public void terminated(boolean status) {
            if (!status && status()) offset = 0;
            if (status) sensor.enableLed(false);

        }
    }



}
