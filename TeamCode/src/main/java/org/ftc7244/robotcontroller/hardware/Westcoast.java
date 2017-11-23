package org.ftc7244.robotcontroller.hardware;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.qualcomm.hardware.kauailabs.NavxMicroNavigationSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.RobotLog;

import org.ftc7244.robotcontroller.Debug;
import org.ftc7244.robotcontroller.sensor.gyroscope.NavxRobot;

/**
 * Created by Eeshwar Laptop on 10/16/2017.
 */

public class Westcoast extends Hardware implements NavxRobot{
    public static final double COUNTS_PER_INCH = (3.2 * Math.PI)/ 134.4;

    @Nullable
    private DcMotor driveBackLeft, driveFrontLeft, driveBackRight, driveFrontRight, intakeVertical, intakeTop, intakeBottom;
    @Nullable
    private CRServo spring, intakeBottomLeft, intakeBottomRight, intakeTopLeft, intakeTopRight, jewelVerticle, jewelHorizontal;
    @Nullable
    private AnalogInput vertLimit;
    @Nullable
    private ColorSensor jewelSensor;
    @Nullable
    private NavxMicroNavigationSensor navX;

    public Westcoast(OpMode opMode) {
        super(opMode, COUNTS_PER_INCH);
    }
    private int blueOffset, redOffset;
    /**
     * Waits for all the motors to have zero position and if it is not zero tell it to reset
     *
     * @param motors all the motors to reset
     */
    public static void resetMotors(@NonNull DcMotor... motors) {
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

    /**
     * Identify hardware and then set it up with different objects. Other initialization properties are
     * set to ensure that everything is in the default position or correct mode for the robot.
     */
    @Override
    public void init() {
        //Initialize or nullify all hardware
        HardwareMap map = opMode.hardwareMap;
        this.jewelSensor = getOrNull(map.colorSensor, "jewelsensor");
        this.spring = getOrNull(map.crservo, "spring");

        this.intakeVertical = getOrNull(map.dcMotor, "vertical");

        this.driveBackLeft = getOrNull(map.dcMotor, "driveBackLeft");
        this.driveFrontLeft = getOrNull(map.dcMotor, "driveFrontLeft");
        this.driveBackRight = getOrNull(map.dcMotor, "driveBackRight");
        this.driveFrontRight = getOrNull(map.dcMotor, "driveFrontRight");

        this.intakeBottomLeft = getOrNull(map.crservo, "intakeBLeft");
        this.intakeBottomRight = getOrNull(map.crservo, "intakeBRight");
        this.intakeTopLeft = getOrNull(map.crservo, "intakeTLeft");
        this.intakeTopRight = getOrNull(map.crservo, "intakeTRight");

        this.intakeTop = getOrNull(map.dcMotor, "intakeT");
        this.intakeBottom = getOrNull(map.dcMotor, "intakeB");

        this.jewelVerticle = getOrNull(map.crservo, "jewelverticle");
        this.jewelHorizontal = getOrNull(map.crservo, "jewelhorizontal");

//        this.navX = opMode.hardwareMap.get(NavxMicroNavigationSensor.class, "navx");

        //Set the default direction for all the hardware and also initialize default positions
        if (driveFrontLeft != null) driveFrontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        if (driveFrontRight != null) driveFrontRight.setDirection(DcMotorSimple.Direction.REVERSE);
//        if (intakeBottomRight != null) intakeBottomRight.setDirection(DcMotorSimple.Direction.REVERSE);
        if (jewelSensor != null) {
            redOffset = jewelSensor.red();
            blueOffset = jewelSensor.blue();
        } else {
            redOffset = 0;
            blueOffset = 0;
        }
        if(intakeTopRight != null) intakeTopRight.setDirection(DcMotorSimple.Direction.REVERSE);
        if(intakeBottomRight != null) intakeBottomRight.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    public void drive(double leftPower, double rightPower) {
        driveFrontLeft.setPower(leftPower);
        driveBackLeft.setPower(leftPower);
        driveFrontRight.setPower(-rightPower);
        driveBackRight.setPower(-rightPower);
    }


    @Override
    public void resetDriveMotors() {
        resetMotors(driveBackLeft, driveBackRight, driveFrontLeft, driveFrontRight);
    }

    @Override
    public int getDriveEncoderAverage() {
        return (int) ((driveBackLeft.getCurrentPosition()+driveBackRight.getCurrentPosition()+driveFrontLeft.getCurrentPosition()+driveFrontRight.getCurrentPosition())/4/COUNTS_PER_INCH);
    }

    public boolean isColor(int color) {
        int blue = jewelSensor.blue() - blueOffset, red = jewelSensor.red() - redOffset;
        if (Debug.STATUS)
            RobotLog.ii("COLOR", blue + "(" + blueOffset + ")" + ":" + red + "(" + redOffset + ")");
        switch (color) {
            case Color.BLUE:
                return blue > red;
            case Color.RED:
                return blue < red;
            default:
                RobotLog.e("Color does not exist!");
                return false;
        }
    }
    @Nullable
    public DcMotor getDriveFrontLeft() {
        return this.driveFrontLeft;
    }

    @Nullable
    public DcMotor getDriveBackLeft() {
        return this.driveBackLeft;
    }

    @Nullable
    public DcMotor getIntakeVertical(){return this.intakeVertical;}

    @Nullable
    public DcMotor getDriveFrontRight() {
        return this.driveFrontRight;
    }

    @Nullable
    public DcMotor getDriveBackRight() {
        return this.driveBackRight;
    }

    @Nullable
    public CRServo getIntakeBottomLeft() {
        return this.intakeBottomLeft;
    }

    @Nullable
    public CRServo getIntakeBottomRight() {
        return this.intakeBottomRight;
    }
    @Nullable
    public CRServo getIntakeTopLeft(){
        return this.intakeTopLeft;
    }

    @Nullable
    public DcMotor getIntakeBottom() {
        return intakeBottom;
    }

    @Nullable
    public DcMotor getIntakeTop() {
        return intakeTop;
    }

    @Nullable
    public CRServo getIntakeTopRight() {
        return intakeTopRight;
    }

    @Nullable
    public CRServo getSpring(){return this.spring;}

    @Nullable
    public CRServo getJewelVerticle(){return this.jewelVerticle;}

    @Nullable
    public CRServo getJewelHorizontal(){return this.jewelHorizontal;}

    @Nullable
    public AnalogInput getVertLimit(){return vertLimit;}

    @Nullable
    public NavxMicroNavigationSensor getNavX(){
        return navX; //(╯°□°)╯︵ ┻━┻
    }
}
