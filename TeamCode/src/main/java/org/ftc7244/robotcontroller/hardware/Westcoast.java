package org.ftc7244.robotcontroller.hardware;

import android.graphics.Color;
import android.support.annotation.Nullable;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.RobotLog;

import org.ftc7244.robotcontroller.Debug;
import org.ftc7244.robotcontroller.autonomous.Status;

public class Westcoast extends Hardware {
    public static final double COUNTS_PER_INCH = 32.9083451562,
                               RELIC_SPOOL_MIN = -1857, RELIC_SPOOL_MAX = 50,
                               INTAKE_REST_POWER = 0.1, INTAKE_OPEN = 0.8,
                               INTAKE_CLOSE = 0.2;
    public static final int INTAKE_HOME_POSITION = 500, INTAKE_MIN_POSITION = 100;

    @Nullable
    private DcMotor driveBackLeft, driveFrontLeft, driveBackRight, driveFrontRight, intakeLift, intakeTop, intakeBottom, relicSpool;
    @Nullable
    private CRServo intakeBottomLeft, intakeBottomRight, intakeTopLeft, intakeTopRight;
    @Nullable
    private Servo jewelVertical, jewelHorizontal, spring, intakeServo, relicWrist, relicFinger;
    @Nullable
    private AnalogInput bottomIntakeSwitch;
    @Nullable
    private ColorSensor jewelSensor;
    @Nullable
    private DistanceSensor jewelDistance;

    private int blueOffset, redOffset;

    public Westcoast(OpMode opMode) {
        super(opMode, COUNTS_PER_INCH);
    }
    /**
     * Identify hardware and then set it up with different objects. Other initialization properties are
     * mset to ensure that everything is in the default position or correct mode for the robot.
     */
    @Override
    public void init() {
        //Initialize or nullify all hardware
        HardwareMap map = opMode.hardwareMap;

        this.jewelSensor = getOrNull(map.colorSensor, "jewelSensor");
        this.jewelDistance = map.get(DistanceSensor.class, "jewelSensor");
        this.spring = getOrNull(map.servo, "spring");
        this.intakeServo = getOrNull(map.servo, "intakeServo");
        this.intakeLift = getOrNull(map.dcMotor, "vertical");

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

        this.jewelVertical = getOrNull(map.servo, "jewelVertical");
        this.jewelHorizontal = getOrNull(map.servo, "jewelHorizontal");

        this.relicSpool = getOrNull(map.dcMotor, "spooler");
        this.relicWrist = getOrNull(map.servo, "relicArm");
        this.relicFinger = getOrNull(map.servo, "relicClaw");

        this.bottomIntakeSwitch = getOrNull(map.analogInput, "bottomIntakeSwitch");


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
        resetMotors(relicSpool, intakeLift);
        if(intakeLift != null)intakeLift.setPower(0.1);
        driveBackLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveBackRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveFrontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveFrontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void initServos(){
        if(jewelVertical != null) jewelVertical.setPosition(0.67);
        if(jewelHorizontal != null) jewelHorizontal.setPosition(0.73);
        if(relicWrist != null) relicWrist.setPosition(0.1);
        if(spring != null){
            spring.setPosition(1);
            spring.setDirection(Servo.Direction.FORWARD);
        }
        intakeServo.setPosition(0.2);
    }

    public void driveIntakeVertical(double power){
        intakeBottomLeft.setPower(power);
        intakeBottomRight.setPower(power);
        intakeTopLeft.setPower(power);
        intakeTopRight.setPower(power);
    }

    @Override
    public void drive(double leftPower, double rightPower, long timeMillis) throws InterruptedException{
        drive(leftPower, rightPower);
        sleep(timeMillis);
        drive(0, 0);
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
        while(!Status.isStopRequested() && getDriveEncoderAverage() <= target);
        drive(0, 0);
        driveBackLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveBackRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveFrontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveFrontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    @Override
    public void resetDriveMotors() {
        resetMotors(driveBackLeft, driveBackRight, driveFrontLeft, driveFrontRight);
    }

    @Override
    public int getDriveEncoderAverage() {
        return (driveBackLeft.getCurrentPosition()-driveBackRight.getCurrentPosition()+driveFrontLeft.getCurrentPosition()-driveFrontRight.getCurrentPosition())/4;
    }

    @Override
    public void resetDriveEncoders() {
        driveBackRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        driveBackLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        driveFrontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        driveFrontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        try {
            sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driveBackRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveBackLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveFrontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveFrontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    /**
     *
     * @param color the color to which the sensor input is to be determined
     * @return whether the given color is equal to the sensor input
     */
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

    /**
     * Sets the intake power proportional to the normalized error from the target in terms of an arbitrary
     * constant value (in this cause, the difference between the home and min positions of the intake
     * @param targetPosition The position intended to travel to
     */
    public void liftIntakeProportional(int targetPosition){
        intakeLift.setPower(INTAKE_REST_POWER + Math.max(0, targetPosition-intakeLift.getCurrentPosition())/(INTAKE_HOME_POSITION-INTAKE_MIN_POSITION));
    }

    public boolean glyphInBottomIntake(){
        return bottomIntakeSwitch.getVoltage()>0.5;
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
    public DcMotor getIntakeLift(){return this.intakeLift;}

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
    public Servo getSpring(){
        return this.spring;
    }

    @Nullable
    public Servo getJewelVertical(){
        return this.jewelVertical;
    }

    @Nullable
    public Servo getJewelHorizontal(){
        return this.jewelHorizontal;
    }

    @Nullable
    public AnalogInput getBottomIntakeSwitch(){
        return bottomIntakeSwitch;
    }

    @Nullable
    public ColorSensor getJewelSensor(){
        return jewelSensor;
    }

    @Nullable
    public Servo getIntakeServo(){
        return intakeServo;
    }

    @Nullable
    public DistanceSensor getJewelDistance(){
        return jewelDistance;
    }

    @Nullable
    public DcMotor getRelicSpool() {
        return relicSpool;
    }

    @Nullable
    public Servo getRelicWrist() {
        return relicWrist;
    }

    @Nullable
    public Servo getRelicFinger() {
        return relicFinger;
    }
}