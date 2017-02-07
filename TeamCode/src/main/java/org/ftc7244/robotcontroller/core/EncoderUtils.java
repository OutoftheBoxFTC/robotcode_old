package org.ftc7244.robotcontroller.core;

import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Acts as a mediator for the amount of rotations for each wheel and also as a way of reseting the
 * encoders in general
 */
public class EncoderUtils {

    public final static double COUNTS_PER_INCH = 1120 / (Math.PI * 3);

    /**
     * Waits for all the motors to have zero position and if it is not zero tell it to reset
     *
     * @param motors all the motors to reset
     */
    public static void resetMotors(DcMotor... motors) {
        boolean notReset = true;
        while (notReset) {
            boolean allReset = true;
            for (DcMotor motor : motors) {
                if (motor.getCurrentPosition() == 0) {
                    continue;
                }
                allReset = false;
                motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            }
            notReset = !allReset;
        }
        for (DcMotor motor : motors) motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
}
