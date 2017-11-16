package org.ftc7244.robotcontroller.hardware;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.kauailabs.navx.ftc.AHRS;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.Servo;

import org.ftc7244.robotcontroller.sensor.gyroscope.NavXGyroscopeProvider;
import org.ftc7244.robotcontroller.sensor.gyroscope.RevIMUGyroscopeProvider;

/**
 * Created by Eeshwar Laptop on 10/16/2017.
 */

public class RelicRecoveryWestcoast extends Hardware{
    public static final double COUNTS_PER_INCH = (3.2 * Math.PI)/ 134.4, PIXY_TRANSLATE_MULTIPLE = 100 / 3.3, NANO_TO_SECONDS = 1000000000;

    @Nullable
    private DcMotor driveBackLeft, driveFrontLeft, driveBackRight, driveFrontRight, launcher, spoolerTop, spoolerBottom, intake, intakeVerticle;
    @Nullable
    private I2cDevice navx;
    @Nullable
    private I2cDeviceSynch pixycam;
    @Nullable
    private CRServo spring, intakebleft, intakebright;
    @Nullable
    private AnalogInput vertLimit;
    @Nullable
    private BNO055IMU imu;
    @Nullable
    private RevIMUGyroscopeProvider rev;
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
        this.imu = map.get(BNO055IMU.class, "imu");
        this.spring = getOrNull(map.crservo, "spring");
        this.intakeVerticle = getOrNull(map.dcMotor, "vertical");
        this.driveBackLeft = getOrNull(map.dcMotor, "driveBackLeft");
        this.driveFrontLeft = getOrNull(map.dcMotor, "driveFrontLeft");
        this.driveBackRight = getOrNull(map.dcMotor, "driveBackRight");
        this.driveFrontRight = getOrNull(map.dcMotor, "driveFrontRight");
        this.launcher = getOrNull(map.dcMotor, "launcher");
        this.intakebleft = getOrNull(map.crservo, "intakebleft");
        this.intakebright = getOrNull(map.crservo, "intakebright");
        this.spoolerTop = getOrNull(map.dcMotor, "spoolerTop");
        this.spoolerBottom = getOrNull(map.dcMotor, "spoolerBottom");
        this.intake = getOrNull(map.dcMotor, "intake");
        this.navx = getOrNull(map.i2cDevice, "navx");
        this.pixycam = getOrNull(map.i2cDeviceSynch, "pixycam");
        this.vertLimit = getOrNull(map.analogInput, "vertLimit");

        //Set the default direction for all the hardware and also initialize default positions
        if (driveFrontLeft != null) driveFrontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        if (driveFrontRight != null) driveFrontRight.setDirection(DcMotorSimple.Direction.REVERSE);
//        if (intakebright != null) intakebright.setDirection(DcMotorSimple.Direction.REVERSE);
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

    public void setSpoolerPower(double power) {
        spoolerTop.setPower(power);
        spoolerBottom.setPower(power);
    }

    public int getSpoolerTicks() {
        return (spoolerBottom.getCurrentPosition() + spoolerTop.getCurrentPosition()) / 2;
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
    public I2cDeviceSynch getPixycam(){return this.pixycam;}

    @Nullable
    public DcMotor getIntakeVerticle(){return this.intakeVerticle;}

    @Nullable
    public DcMotor getDriveFrontRight() {
        return this.driveFrontRight;
    }

    @Nullable
    public DcMotor getDriveBackRight() {
        return this.driveBackRight;
    }

    @Nullable
    public DcMotor getLauncher() {
        return this.launcher;
    }

    @Nullable
    public CRServo getIntakeBtmLf() {
        return this.intakebleft;
    }

    @Nullable
    public CRServo getIntakeBtmRt() {
        return this.intakebright;
    }

    @Nullable
    public DcMotor getSpoolerBottom() {
        return spoolerBottom;
    }

    @Nullable
    public DcMotor getSpoolerTop() {
        return spoolerTop;
    }

    @Nullable
    public DcMotor getIntake(){return intake;}

    @Nullable
    public CRServo getSpring(){return this.spring;}

    @Nullable
    public AnalogInput getVertLimit(){return vertLimit;}

    @Nullable
    public BNO055IMU getImu(){return this.imu;}
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
