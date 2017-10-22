package org.ftc7244.robotcontroller.hardware;

import android.support.annotation.Nullable;

import com.kauailabs.navx.ftc.AHRS;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cDevice;

import org.ftc7244.robotcontroller.sensor.gyroscope.NavXGyroscopeProvider;


/**
 * Created by Stargamer285 on 10/9/2017.
 */

public class XDrive extends Hardware{
    public static boolean stopIntake = false;
    public static final double COUNTS_PER_INCH = 1;

    @Nullable
    private DcMotor driveTopLeft, driveTopRight, driveBottomLeft, driveBottomRight, btm_intake_left, btm_intake_right, raising_intake_motor, top_intake_left, top_intake_right, spoolerTop, spoolerBottom;
    @Nullable
    private AnalogInput lower_input_limit, top_input_limit;
    @Nullable
    private I2cDevice navx;

    public XDrive(OpMode opMode) {
        super(opMode, COUNTS_PER_INCH);
    }



    /**
     * Identify hardware and then set it up with different objects. Other initialization properties are
     * set to ensure that everything is in the default position or correct mode for the robot.
     */
    @Override
    public void init() {
        //Initialize or nullify all hardware
        HardwareMap map = opMode.hardwareMap;
        this.driveTopLeft = getOrNull(map.dcMotor, "drive_top_left");
        this.driveTopRight = getOrNull(map.dcMotor, "drive_top_right");
        this.driveBottomLeft = getOrNull(map.dcMotor, "drive_bottom_left");
        this.driveBottomRight = getOrNull(map.dcMotor, "drive_bottom_right");
        this.lower_input_limit = getOrNull(map.analogInput, "lower_input_limit");
        this.top_input_limit = getOrNull(map.analogInput, "top_input_limit");
        this.btm_intake_left = getOrNull(map.dcMotor, "btm_intake_left");
        this.btm_intake_right = getOrNull(map.dcMotor, "btm_intake_right");
        this.raising_intake_motor = getOrNull(map.dcMotor, "raising_intake_motor");
        this.top_intake_left = getOrNull(map.dcMotor, "top_intake_left");
        this.top_intake_right = getOrNull(map.dcMotor, "top_intake_right");
        this.spoolerTop = getOrNull(map.dcMotor, "spoolerTop");
        this.spoolerBottom = getOrNull(map.dcMotor, "spoolerBottom");
        this.navx = getOrNull(map.i2cDevice, "navx");

        //Set the default direction for all the hardware and also initialize default positions
        if (driveTopLeft != null) driveTopLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        if (driveTopRight != null) driveTopRight.setDirection(DcMotorSimple.Direction.FORWARD);
        if (driveBottomLeft != null) driveBottomRight.setDirection(DcMotorSimple.Direction.REVERSE);
        if (driveBottomRight != null) driveBottomLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        if (spoolerTop != null) spoolerTop.setDirection(DcMotorSimple.Direction.REVERSE);
        if (spoolerTop != null && spoolerBottom != null) resetMotors(spoolerBottom, spoolerTop);
    }

    @Override
    public void drive(double leftPower, double rightPower) {
        driveBottomLeft.setPower(leftPower);
        driveTopLeft.setPower(leftPower);
        driveBottomRight.setPower(rightPower);
        driveTopRight.setPower(rightPower);
    }

    @Override
    public void resetDriveMotors() {
        resetMotors(driveTopLeft, driveTopRight, driveBottomLeft, driveBottomRight);
    }

    @Override
    public int getDriveEncoderAverage() {
        return 0;
    }

    /**
     * System for intake on the robot.
     * The system spins the intake motors until the limit switch is pressed, then stops the intake
     * motors and spins the raising motors. When the limit switch on the top is pressed, the
     * raising motor stops. When the method is called again, the cube is intaked, but does not
     * raise to prevent hitting the top block
     */
    public void intakeBlock(long delay) throws InterruptedException{
        stopIntake = false;
        sleep(delay);

        while(Math.round(lower_input_limit.getVoltage()) == 0 && stopIntake == false){
            btm_intake_left.setPower(1);
            btm_intake_right.setPower(1);
        }
        btm_intake_left.setPower(0);
        btm_intake_right.setPower(0);
        if(Math.round(top_input_limit.getVoltage()) == 0 && stopIntake == false){
            while (Math.round(top_input_limit.getVoltage()) == 0){
                raising_intake_motor.setPower(1);
            }
            raising_intake_motor.setPower(0);
        }

    }
    /**
     * Class that can be called to cancel IntakeBlock
     */
    public void stopIntake() throws InterruptedException{
        stopIntake = true;
    }
    /**
     * Code to raise the linear slide and expel the block
     */
    public void expelBlock(long delay, boolean top) throws InterruptedException{
        if(top){
            top_intake_left.setDirection(DcMotorSimple.Direction.REVERSE);
            top_intake_right.setDirection(DcMotorSimple.Direction.REVERSE);
            top_intake_left.setPower(1);
            top_intake_right.setPower(1);
            sleep(500);
            top_intake_left.setPower(0);
            top_intake_right.setPower(0);
        }else if(!top) {
            btm_intake_left.setDirection(DcMotorSimple.Direction.REVERSE);
            btm_intake_right.setDirection(DcMotorSimple.Direction.REVERSE);
            btm_intake_left.setPower(1);
            btm_intake_right.setPower(1);
            sleep(500);
            btm_intake_left.setPower(0);
            btm_intake_right.setPower(0);
        }
        top_intake_left.setDirection(DcMotorSimple.Direction.FORWARD);
        top_intake_right.setDirection(DcMotorSimple.Direction.FORWARD);
        btm_intake_left.setDirection(DcMotorSimple.Direction.FORWARD);
        btm_intake_right.setDirection(DcMotorSimple.Direction.FORWARD);

    }

    public void setSpoolerPower(double power) {
        spoolerTop.setPower(power);
        spoolerBottom.setPower(power);
    }

    public int getSpoolerTicks() {
        return (spoolerBottom.getCurrentPosition() + spoolerTop.getCurrentPosition()) / 2;
    }


    @Nullable
    public DcMotor getDriveBottomLeft() {
        return this.driveBottomLeft;
    }
    @Nullable
    public DcMotor getDriveTopLeft() {
        return this.driveTopLeft;
    }

    @Nullable
    public DcMotor getDriveTopRight() {
        return this.driveTopRight;
    }

    @Nullable
    public DcMotor getDriveBottomRight() {
        return this.driveBottomRight;
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
