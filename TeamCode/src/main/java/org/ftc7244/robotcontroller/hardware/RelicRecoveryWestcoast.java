package org.ftc7244.robotcontroller.hardware;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.kauailabs.navx.ftc.AHRS;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.Servo;

import org.ftc7244.robotcontroller.sensor.gyroscope.NavXGyroscopeProvider;

/**
 * Created by Eeshwar Laptop on 10/16/2017.
 */

public class RelicRecoveryWestcoast extends Hardware{
    public static final double COUNTS_PER_INCH = 1, PIXY_TRANSLATE_MULTIPLE = 100 / 3.3;

    @Nullable
    private DcMotor driveBackLeft, driveFrontLeft, driveBackRight, driveFrontRight;
    @Nullable
    private I2cDevice navx;
    @Nullable
    private CRServo intakeLeft, intakeMiddle, intakeRight;
    @Nullable
    private AnalogInput pixyCam, pixyCamStatus;

    public RelicRecoveryWestcoast(OpMode opMode) {
        super(opMode, COUNTS_PER_INCH);
    }

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
        this.pixyCam = getOrNull(map.analogInput, "PixyCam");

        this.intakeLeft = getOrNull(map.crservo, "intake_left");
        this.intakeMiddle = getOrNull(map.crservo, "intake_left");
        this.intakeRight = getOrNull(map.crservo, "intake_left");

        this.pixyCamStatus = getOrNull(map.analogInput, "PixyCamStatus");
        this.driveBackLeft = getOrNull(map.dcMotor, "driveBackLeft");
        this.driveFrontLeft = getOrNull(map.dcMotor, "driveFrontLeft");
        this.driveBackRight = getOrNull(map.dcMotor, "driveBackRight");
        this.driveFrontRight = getOrNull(map.dcMotor, "driveFrontRight");
        this.navx = getOrNull(map.i2cDevice, "navx");

        //Set the default direction for all the hardware and also initialize default positions
        if (driveFrontLeft != null) driveFrontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        if (driveFrontRight != null) driveFrontRight.setDirection(DcMotorSimple.Direction.REVERSE);

        if(intakeLeft != null)intakeLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        if(intakeMiddle != null)intakeMiddle.setDirection(DcMotorSimple.Direction.REVERSE);
        if(intakeRight != null)intakeRight.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    public void drive(double leftPower, double rightPower) {
        driveFrontLeft.setPower(-leftPower);
        driveBackLeft.setPower(-leftPower);
        driveFrontRight.setPower(rightPower);
        driveBackRight.setPower(rightPower);
    }

    public double getPixyData(){
        if(pixyCamStatus.getVoltage() > 0){
           return pixyCam.getVoltage() * PIXY_TRANSLATE_MULTIPLE;
        }
        return 150;
    }

    @Override
    public void resetDriveMotors() {
        resetMotors(driveBackLeft, driveBackRight, driveFrontLeft, driveFrontRight);
    }

    @Override
    public int getDriveEncoderAverage() {
        return (driveBackLeft.getCurrentPosition()+driveBackRight.getCurrentPosition()+driveFrontLeft.getCurrentPosition()+driveFrontRight.getCurrentPosition())/4;
    }


    @Nullable
    public DcMotor getDriveFrontLeft() {
        return this.driveFrontLeft;
    }
    @Nullable
    public AnalogInput getPixyCam(){
        return this.pixyCam;
    }
    @Nullable
    public AnalogInput getPixyCamStatus(){
        return this.pixyCamStatus;
    }
    @Nullable
    public DcMotor getDriveBackLeft() {
        return this.driveBackLeft;
    }

    @Nullable
    public DcMotor getDriveFrontRight() {
        return this.driveFrontRight;
    }

    @Nullable
    public DcMotor getDriveBackRight() {
        return this.driveBackRight;
    }

    @Nullable
    public CRServo getIntakeLeft() {
        return intakeLeft;
    }

    @Nullable
    public CRServo getIntakeMiddle() {
        return intakeMiddle;
    }

    @Nullable
    public CRServo getIntakeRight() {
        return intakeRight;
    }

    /**
     * Uses reflection to obtain all the information to properly setup the navx sensor
     *
     * @return setup navx sensor
     */
    @Nullable
    public AHRS getNavX(){
        if(navx==null)return null;
        return AHRS.getInstance((DeviceInterfaceModule) navx.getController(), navx.getPort(), AHRS.DeviceDataType.kProcessedData, NavXGyroscopeProvider.NAVX_DEVICE_UPDATE_RATE_HZ);
    }
}
