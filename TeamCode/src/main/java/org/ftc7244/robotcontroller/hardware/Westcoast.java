package org.ftc7244.robotcontroller.hardware;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.qualcomm.hardware.kauailabs.NavxMicroNavigationSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.teamcode.R;
import org.ftc7244.robotcontroller.Debug;
import org.ftc7244.robotcontroller.autonomous.Status;
import org.ftc7244.robotcontroller.autonomous.drivers.GyroscopeDrive;
import org.ftc7244.robotcontroller.sensor.gyroscope.GyroscopeProvider;
import org.ftc7244.robotcontroller.sensor.gyroscope.NavxRobot;

/**
 * Created by Eeshwar Laptop on 10/16/2017.
 */

public class Westcoast extends Hardware implements NavxRobot{
    public static final double COUNTS_PER_INCH = 403.2 / (3.9 * Math.PI);

    @Nullable
    private DcMotor driveBackLeft, driveFrontLeft, driveBackRight, driveFrontRight, intakeVertical, intakeTop, intakeBottom;
    @Nullable
    private CRServo intakeBottomLeft, intakeBottomRight, intakeTopLeft, intakeTopRight;
    @Nullable
    private Servo jewelVerticle, jewelHorizontal, spring, intakeServo;
    @Nullable
    private AnalogInput vertLimit;
    @Nullable
    private ColorSensor jewelSensor;
    @Nullable
    private DistanceSensor jewelDistance;
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
        this.jewelSensor = getOrNull(map.colorSensor, "jewelSensor");
        this.jewelDistance = map.get(DistanceSensor.class, "jewelSensor");
        this.spring = getOrNull(map.servo, "spring");
        this.intakeServo = getOrNull(map.servo, "intakeServo");
        this.intakeVertical = getOrNull(map.dcMotor, "vertical");

        this.driveBackLeft = getOrNull(map.dcMotor, "driveBackLeft");
        this.driveFrontLeft = getOrNull(map.dcMotor, "driveFrontLeft");
        this.driveBackRight = getOrNull(map.dcMotor, "driveBackRight");
        this.driveFrontRight = getOrNull(map.dcMotor, "driveFrontRight");

        this.intakeBottomLeft = getOrNull(map.crservo, "intakeBLeft");
        this.intakeBottomRight = getOrNull(map.crservo, "intakeBRight");
        this.intakeTopLeft = getOrNull(map.crservo, "intakeTLeft");
        this.intakeTopRight = getOrNull(map.crservo, "intakeTRight");
        this.intakeServo = getOrNull(map.servo, "intakeServo");

        this.intakeTop = getOrNull(map.dcMotor, "intakeT");
        this.intakeBottom = getOrNull(map.dcMotor, "intakeB");

        this.jewelVerticle = getOrNull(map.servo, "jewelVerticle");
        this.jewelHorizontal = getOrNull(map.servo, "jewelHorizontal");

        //Set the default direction for all the hardware and also initialize default positions
        if (driveFrontLeft != null) driveFrontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        if (driveFrontRight != null) driveFrontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        if (jewelSensor != null) {
            redOffset = jewelSensor.red();
            blueOffset = jewelSensor.blue();
        } else {
            redOffset = 0;
            blueOffset = 0;
        }
        if(intakeTopRight != null) intakeTopRight.setDirection(DcMotorSimple.Direction.REVERSE);
        if(intakeBottomRight != null) intakeBottomRight.setDirection(DcMotorSimple.Direction.REVERSE);
        //Init Servos34.4/12
    }

    public void initServos(){
        if(jewelVerticle != null) jewelVerticle.setPosition(0.55);
        if(jewelHorizontal != null) jewelHorizontal.setPosition(0);
        if(spring != null){
            spring.setPosition(1);
            spring.setDirection(Servo.Direction.FORWARD);
        }
    }

    public void driveintakeVertical(double power){
        intakeBottomLeft.setPower(power);
        intakeBottomRight.setPower(power);
        intakeTopLeft.setPower(power);
        intakeTopRight.setPower(power);
    }

    @Override
    public void drive(double leftPower, double rightPower, long timeMillis) throws InterruptedException{
        driveFrontLeft.setPower(leftPower);
        driveBackLeft.setPower(leftPower);
        driveFrontRight.setPower(-rightPower);
        driveBackRight.setPower(-rightPower);
        sleep(timeMillis);
        driveFrontLeft.setPower(0);
        driveBackLeft.setPower(0);
        driveFrontRight.setPower(0);
        driveBackRight.setPower(0);
    }

    @Override
    public void drive(double leftPower, double rightPower) {
        driveFrontLeft.setPower(leftPower);
        driveBackLeft.setPower(leftPower);
        driveFrontRight.setPower(-rightPower);
        driveBackRight.setPower(-rightPower);
    }


    @Override
    public void driveToInch(double power, double inches){
        resetDriveMotors();
        double target = inches * COUNTS_PER_INCH;
        drive(power, power);
        while(!Status.isStopRequested() && getDriveEncoderAverage() <= target){
            opMode.telemetry.addData("Target", target);
            opMode.telemetry.addData("Encoder Average", getDriveEncoderAverage());
            opMode.telemetry.update();
        }
        drive(0, 0);
    }

    @Override
    public void resetDriveMotors() {
        resetMotors(driveBackLeft, driveBackRight, driveFrontLeft, driveFrontRight);
    }

    @Override
    public int getDriveEncoderAverage() {
        return -(driveBackLeft.getCurrentPosition()+driveBackRight.getCurrentPosition()+driveFrontLeft.getCurrentPosition()+driveFrontRight.getCurrentPosition())/4;
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

    public void knockOverJewel(int color) throws InterruptedException {
        getJewelHorizontal().setPosition(0.5);
        getJewelVerticle().setPosition(0.1);
        sleep(1500);
        if(color==Color.RED) {
            getJewelHorizontal().setPosition(isColor(Color.RED) ? 0 : 0.7);
        }else if(color==Color.BLUE){
            getJewelHorizontal().setPosition(isColor(Color.RED) ? 0.7 : 0);

        }
        sleep(500);
        getJewelHorizontal().setPosition(0.4);
        getJewelVerticle().setPosition(0.55);
        sleep(500);
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
    public Servo getSpring(){return this.spring;}

    @Nullable
    public Servo getJewelVerticle(){return this.jewelVerticle;}

    @Nullable
    public Servo getJewelHorizontal(){return this.jewelHorizontal;}

    @Nullable
    public AnalogInput getVertLimit(){return vertLimit;}

    @Nullable
    public ColorSensor getJewelSensor(){return jewelSensor;}

    @Nullable
    public Servo getIntakeServo(){return intakeServo;}

    @Nullable
    public DistanceSensor getJewelDistance(){return jewelDistance;}

    @Nullable
    public NavxMicroNavigationSensor getNavX(){
        return navX;
    }

    public void jiggleBottomVertical(final long time, final long interval) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ElapsedTime timer = new ElapsedTime(0);
                double power = 0.5;
                while (timer.milliseconds()<time){
                    if (timer.milliseconds() >= interval) {
                        power *=-1;
                        timer.reset();
                    }
                    intakeTopLeft.setPower(power);
                    intakeTopRight.setPower(power);
                }
                intakeTopLeft.setPower(0);
                intakeTopRight.setPower(0);
            }
        });
    }
}