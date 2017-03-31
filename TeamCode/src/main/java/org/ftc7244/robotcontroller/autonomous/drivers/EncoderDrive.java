package org.ftc7244.robotcontroller.autonomous.drivers;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.RobotLog;

import org.ftc7244.robotcontroller.Westcoast;
import org.ftc7244.robotcontroller.autonomous.Status;

/**
 * Created by FTC 7244 on 3/19/2017.
 */
@Deprecated
public class EncoderDrive {

    public final static double COUNTS_PER_INCH = 1120 / (Math.PI * 3);
    private final static int ENCODER_THRESHOLD = 100;

    private Westcoast robot;

    public EncoderDrive(Westcoast robot) {
        this.robot = robot;
    }

    /**
     * Move the robot a certain distance and once it reaches a distance or reaches a timeout then stop the robot
     *
     * @param power       the power of the robot from 0 to 1
     * @param leftInches  how many inches the encoder moves in inches
     * @param rightInches how many inches the encoder moves in inches
     * @throws InterruptedException
     */
    public void drive(double power, double leftInches, double rightInches) throws InterruptedException {
        //Initialize values
        DcMotor left = robot.getDriveLeft(), right = robot.getDriveRight();
        Westcoast.resetMotors(right, left);

        int targetLeft = initEncoderMotor(left, leftInches, power);
        int targetRight = initEncoderMotor(right, rightInches, power);
        RobotLog.i("Target Left " + targetLeft + " Right" + targetRight);

        // keep looping while we are still active, and there is time left, and both motors are running.
        int lastLeft = 0, lastRight = 0;
        while (left.getPower() != 0 || right.getPower() != 0) {
            if (lastLeft != left.getCurrentPosition() || lastRight != right.getCurrentPosition()) {
                lastLeft = left.getCurrentPosition();
                lastRight = right.getCurrentPosition();
                RobotLog.i("Drive Left " + left.getCurrentPosition() + " Right" + right.getCurrentPosition());
            }
            stopWhenComplete(left, targetLeft, ENCODER_THRESHOLD);
            stopWhenComplete(right, targetRight, ENCODER_THRESHOLD);
        }

        // Stop all motion
        left.setPower(0);
        right.setPower(0);

        // Turn off RUN_TO_POSITION
        Westcoast.resetMotors(left, right);
        Westcoast.sleep(500);
    }

    /**
     * @param degs distance in degrees of 360
     */
    public void rotate(double power, int degs, Direction direction) throws InterruptedException {
        double distance = ((double) degs) / 360 * Math.PI * 17.5;
        drive(power, distance * direction.leftPower, distance * direction.rightPower);
    }

    private int initEncoderMotor(DcMotor motor, double distance, double power) {
        if (distance == 0 || power == 0) {
            motor.setPower(0);
            return 0;
        }
        if (power < 0) {
            power = Math.abs(power);
            RobotLog.ww("[Encoder]", "Value for power cannot be negative!");
        }
        int distanceTicks = (int) (Math.round(distance * COUNTS_PER_INCH));

        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motor.setTargetPosition(distanceTicks);
        motor.setPower(power * (distance > 0 ? 1 : -1));
        return distanceTicks;
    }

    private boolean stopWhenComplete(DcMotor motor, int target, int threshold) {
        if (Math.sqrt(Math.pow(target, 2) - Math.pow(motor.getCurrentPosition(), 2)) >= threshold) {
            return false;
        }

        motor.setPower(0);
        return true;
    }

    public enum Direction {
        LEFT(1, -1),
        RIGHT(-1, 1);

        public final int leftPower, rightPower;

        Direction(int leftPower, int rightPower) {
            this.leftPower = leftPower;
            this.rightPower = rightPower;
        }
    }
}
