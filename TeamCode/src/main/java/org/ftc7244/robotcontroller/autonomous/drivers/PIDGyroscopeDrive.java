package org.ftc7244.robotcontroller.autonomous.drivers;

import android.graphics.Color;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.util.RobotLog;

import org.ftc7244.robotcontroller.autonomous.controllers.pid.PIDControllerBuilder;
import org.ftc7244.robotcontroller.autonomous.controllers.DriveControl;
import org.ftc7244.robotcontroller.autonomous.terminators.ColorSensorTerminator;
import org.ftc7244.robotcontroller.autonomous.terminators.ConditionalTerminator;
import org.ftc7244.robotcontroller.autonomous.terminators.RangeTerminator;
import org.ftc7244.robotcontroller.autonomous.terminators.TerminationMode;
import org.ftc7244.robotcontroller.autonomous.terminators.Terminator;
import org.ftc7244.robotcontroller.autonomous.terminators.TimerTerminator;
import org.ftc7244.robotcontroller.hardware.Hardware;
import org.ftc7244.robotcontroller.hardware.Westcoast;
import org.ftc7244.robotcontroller.sensor.gyroscope.GyroscopeProvider;

/**
 * This class is instrumental in the control of the robot and uses the gyroscopePID as a frame of
 * reference. It does not require that the gyroscopePID be the phone or external but uses it to
 * transform robot movement
 */
public class PIDGyroscopeDrive extends DriveControl {

    protected GyroscopeProvider gyroProvider;
    protected Hardware robot;
    private double target;

    /**
     * Same as the parent constructor but passes a debug as fault by default since most users will
     * not want to debug the code.
     *
     * @param robot        access to motors on the robot
     * @param gyroProvider base way to read gyroscopePID values
     */
    public PIDGyroscopeDrive(Hardware robot, GyroscopeProvider gyroProvider) {
        super(new PIDControllerBuilder()
                        .invert()
                        .setProportional(0.0035)
                        .setIntegral(0)
                        .setOutputRange(1)
                        .setDerivative(0.1)
                        .setIntegralRange(6)
                        .setIntegralReset(true)
                        .setOutputRange(0.2)
                        .setDelay(20)
                        .createController(),
                robot);
        this.gyroProvider = gyroProvider;
        this.robot = robot;
        this.target = 0;
    }

    /**
     * This expands the range of gyrosope input from [-180, 180], to [-540, 540]
     * @return gyroscopePID input
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

    public void drive(double power, double units){
        if(units!=0)drive(power, units, target);
    }

    /**
     * This will combine the rotate function from the PID loop with a power offset. The power offset
     * then will then be added to the PID to get the drive. It is important to note that both motors
     * are reset before driving is started and will end once it reaches it's target in memes.
     *
     * @param power  offset of the PID from -1 to 1
     * @param units total distance to travel
     * @throws InterruptedException if code fails to terminate on stop requested
     */
    public void drive(double power, double units, final double target) {
        final double ticks = units * Westcoast.COUNTS_PER_INCH;
        robot.resetDriveEncoders();
        if (units <= 0) RobotLog.e("Invalid distances!");
        final int offset = robot.getDriveEncoderAverage();
        control(target, power, new Terminator() {
            @Override
            public boolean shouldTerminate() {
                robot.getOpMode().telemetry.addData("Target", target);
                return Math.abs(robot.getDriveEncoderAverage() - offset) >= ticks;
            }
        });
    }

    public void driveWithColorSensor(double power, double maxUnits, final ColorSensor colorSensor, ColorSensorTerminator.Color color){
        final double ticks = maxUnits*Westcoast.COUNTS_PER_INCH;
        robot.resetDriveEncoders();
        if(maxUnits<=0){RobotLog.e("Invalid Distance");}
        final int offset = robot.getDriveEncoderAverage();
        control(target, power, new ConditionalTerminator(TerminationMode.OR,
                new ColorSensorTerminator(colorSensor, color, 0),
                new Terminator() {
                    @Override
                    public boolean shouldTerminate() {
                        return Math.abs(robot.getDriveEncoderAverage() - offset) >= ticks;
                    }
                }));
    }

    /**
     * This will combine the rotate function from the PID loop with a power offset. The power offset
     * then will then be added to the PID to get the drive. It is important to note that both motors
     * are reset before driving is started and will end once it reaches it's target in inches, or until a limit switch is pressed.
     */
    public void driveWithLimitSwitch(double power, double units, final AnalogInput LimitSwitch){
        final double ticks = units * Westcoast.COUNTS_PER_INCH;
        robot.resetDriveMotors();
        if (units <= 0){RobotLog.e("Invalid Distance");}
        final int offset = robot.getDriveEncoderAverage();
        control(target, power, new Terminator() {
            @Override
            public boolean shouldTerminate() {
                return LimitSwitch.getVoltage() > 0 || Math.abs(robot.getDriveEncoderAverage() - offset) >= ticks;
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
    public void rotate(double degrees){
        this.target = degrees;
        control(degrees, 0, new ConditionalTerminator(new RangeTerminator(this, degrees, 1.5), new TimerTerminator(6000)));
    }

    /**
     * Resets the current orientation to 0 and waits till the change occurs
     *
     * @throws InterruptedException if code fails to terminate on stop requested
     */
    public void resetOrientation() {
        gyroProvider.setZOffset(gyroProvider.getZOffset());
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
