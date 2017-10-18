package org.ftc7244.robotcontroller;

/**
 * Created by Eeshwar's Laptop on 10/18/2017.
 */

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.kauailabs.navx.ftc.AHRS;
import com.qualcomm.hardware.hitechnic.HiTechnicNxtLightSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;

import org.ftc7244.robotcontroller.autonomous.Status;
import org.ftc7244.robotcontroller.hardware.Hardware;
import org.ftc7244.robotcontroller.sensor.SickUltrasonic;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.kauailabs.navx.ftc.AHRS;
import com.qualcomm.hardware.hitechnic.HiTechnicNxtLightSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;

import org.ftc7244.robotcontroller.Debug;
import org.ftc7244.robotcontroller.autonomous.Status;
import org.ftc7244.robotcontroller.sensor.SickUltrasonic;

public class Westcoast extends Hardware {

    public static final byte NAVX_DEVICE_UPDATE_RATE_HZ = (byte) 100;
    public static final double COUNTS_PER_INCH = 1120 / (Math.PI * 3);

    @Nullable
    private DcMotor driveBackLeft, driveFrontLeft, driveBackRight, driveFrontRight, launcher, intake, spoolerTop, spoolerBottom, lights;
    @Nullable
    private Servo launcherDoor, beaconPusher, carriageRelease;
    @Nullable
    private AnalogInput launcherLimit;
    private OpMode opMode;
    @Nullable
    private ColorSensor beaconSensor;
    @Nullable
    private HiTechnicNxtLightSensor leadingLight, trailingLight;
    @Nullable
    private SickUltrasonic leadingUltrasonic, trailingUltrasonic;
    @Deprecated
    private int blueOffset, redOffset;
    @Nullable
    private I2cDevice navx;

    public Westcoast(OpMode opMode) {
        super(opMode);
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
        this.driveBackLeft = getOrNull(map.dcMotor, "drive_back_left");
        this.driveFrontLeft = getOrNull(map.dcMotor, "drive_front_left");
        this.driveBackRight = getOrNull(map.dcMotor, "drive_back_right");
        this.driveFrontRight = getOrNull(map.dcMotor, "drive_front_right");
        this.launcher = getOrNull(map.dcMotor, "launcher");
        this.launcherDoor = getOrNull(map.servo, "launcher_door");
        this.launcherLimit = getOrNull(map.analogInput, "launcher_limit");
        this.intake = getOrNull(map.dcMotor, "intake");
        this.beaconSensor = getOrNull(map.colorSensor, "beacon_sensor");
        this.beaconPusher = getOrNull(map.servo, "beacon_pusher");
        this.spoolerTop = getOrNull(map.dcMotor, "spoolerTop");
        this.spoolerBottom = getOrNull(map.dcMotor, "spoolerBottom");
        this.carriageRelease = getOrNull(map.servo, "carriage_release");
        this.leadingUltrasonic = new SickUltrasonic(getOrNull(map.analogInput, "leading_ultrasonic"));
        this.trailingUltrasonic = new SickUltrasonic(getOrNull(map.analogInput, "trailing_ultrasonic"));
        this.leadingLight = (HiTechnicNxtLightSensor) getOrNull(map.lightSensor, "leading_light");
        this.trailingLight = (HiTechnicNxtLightSensor) getOrNull(map.lightSensor, "trailing_light");
        this.navx = getOrNull(map.i2cDevice, "navx");
        this.lights = getOrNull(map.dcMotor, "lights");

        //Set the default direction for all the hardware and also initialize default positions
        if (driveFrontLeft != null) driveFrontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        if (driveFrontRight != null) driveFrontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        if (launcher != null) launcher.setDirection(DcMotorSimple.Direction.REVERSE);
        if (beaconPusher != null) beaconPusher.setPosition(1);
//        if (launcherDoor != null) setDoorState(org.ftc7244.robotcontroller.hardware.Westcoast.DoorState.CLOSED);
//        if (carriageRelease != null) setCarriageState(org.ftc7244.robotcontroller.hardware.Westcoast.CarriageState.CLOSED);
        if (spoolerTop != null) spoolerTop.setDirection(DcMotorSimple.Direction.REVERSE);
        if (spoolerTop != null && spoolerBottom != null) resetMotors(spoolerBottom, spoolerTop);
        if (beaconSensor != null) {
            redOffset = beaconSensor.red();
            blueOffset = beaconSensor.blue();
        } else {
            redOffset = 0;
            blueOffset = 0;
        }
    }

    /**
     * Uses the {@link org.ftc7244.robotcontroller.hardware.Westcoast#shoot(long)} function to shoot a specified amount of balls. The
     * only time it will end before shooting is if the code is manually stopped; otherwise, it will
     * continue to the specified amount.
     *
     * @param count the amount of times it will shoot
     * @param delay the time in milliseconds to wait before each shoot
     * @throws InterruptedException if the code fails to terminate before stop requested
     */
/*    public void shootLoop(int count, long delay) throws InterruptedException {
        for (int i = 0; i < count; i++) {
            if (Status.isStopRequested()) break;
            this.shoot(delay);
        }
    }
*/
    /**
     * A tuned tool to shoot a ball from the robot with many fail-safes integrated to prevent the
     * shooting from not completing. First it will spin no more than 1000 milliseconds or until the
     * limit switch is triggered. Then if the shooter will continue to run for 200 more milliseconds
     * and lift the arm up for the remaining 500 milliseconds to load another ball. If anything fails
     * it will be reset to its normal positions and turned off.
     *
     * @param delay the time in milliseconds to wait before each shot
     * @throws InterruptedException if the code fails to terminate before stop requested
     */
/*    public void shoot(long delay) throws InterruptedException {
        sleep(delay);

        ElapsedTime timer = new ElapsedTime();
        boolean failed = false;
        //Spin the launcher
        do {
            if (timer.milliseconds() >= 1000 || Status.isStopRequested()) failed = true;
            launcher.setPower(1);
        } while (Math.round(launcherLimit.getVoltage()) != 0 && !failed);

        //If the code hasn't failed allow the arm to lift or reset spinner
        if (!failed) {
            timer.reset();
            while (timer.milliseconds() <= 500 && !Status.isStopRequested()) {
                if (Status.isStopRequested()) break;
                //Stop the spinner after a delay
                if (timer.milliseconds() > 200) launcher.setPower(0);

                //lift the arm
                setDoorState(org.ftc7244.robotcontroller.hardware.Westcoast.DoorState.OPEN);
            }
            //reset the arm to staring position
            setDoorState(org.ftc7244.robotcontroller.hardware.Westcoast.DoorState.CLOSED);
        } else {
            launcher.setPower(0);
        }
    }
*/
    /**
     * Depending on the color specified ${@link Color#BLUE} or ${@link Color#RED} the robot will do
     * a simple greater than comparison to see if the color specified is greater than the other.
     *
     * @param color only  ${@link Color#BLUE} or ${@link Color#RED} can be inputted
     * @return whether it sees the color or not
     */
    public boolean isColor(int color) {
        int blue = beaconSensor.blue() - blueOffset, red = beaconSensor.red() - redOffset;
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
     * Waits a set amount of time to put the beacon arm out and the same amount of time to pull it
     * back in. This takes a total of 1.5 seconds.
     *
     * @throws InterruptedException if the code fails to terminate before stop requested
     */
    public void pushBeacon() throws InterruptedException {
        beaconPusher.setPosition(0);
        sleep(750);
        beaconPusher.setPosition(1);
    }

    /**
     * Use ${@link org.ftc7244.robotcontroller.hardware.Westcoast.DoorState} to either raise the arm to allow a ball in or set it to the zero
     * position to prevent balls to go into the robot.
     *
     * @param state the position of the door
     */
//    public void setDoorState(@NonNull org.ftc7244.robotcontroller.hardware.Westcoast.DoorState state) {
//        launcherDoor.setPosition(state.position);
//    }

    /**
     * Uses the ${@link org.ftc7244.robotcontroller.hardware.Westcoast.CarriageState} to release the lift or lock the lift.
     *
//     * @param state of the lock on the robot
     */
//    public void setCarriageState(@NonNull org.ftc7244.robotcontroller.hardware.Westcoast.CarriageState state) {
//        carriageRelease.setPosition(state.position);
//    }

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
    public DcMotor getIntake() {
        return this.intake;
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
    public Servo getLauncherDoor() {
        return this.launcherDoor;
    }

    @Nullable
    public Servo getBeaconPusher() {
        return this.beaconPusher;
    }

    @Nullable
    public Servo getCarriageRelease() {
        return this.carriageRelease;
    }

    @Nullable
    public AnalogInput getLauncherLimit() {
        return this.launcherLimit;
    }

    @Nullable
    public ColorSensor getBeaconSensor() {
        return this.beaconSensor;
    }

    @Nullable
    public HiTechnicNxtLightSensor getLeadingLight() {
        return this.leadingLight;
    }

    @Nullable
    public HiTechnicNxtLightSensor getTrailingLight() {
        return this.trailingLight;
    }

    @Nullable
    public SickUltrasonic getLeadingUltrasonic() {
        return this.leadingUltrasonic;
    }

    @Nullable
    public SickUltrasonic getTrailingUltrasonic() {
        return this.trailingUltrasonic;
    }

    @Nullable
    public DcMotor getLights() {
        return lights;
    }
    @Deprecated
    public int getBlueOffset() {
        return blueOffset;
    }
    @Deprecated
    public int getRedOffset() {
        return redOffset;
    }

    /**
     * Uses reflection to obtain all the information to properly setup the navx sensor
     *
     * @return setup navx sensor
     */
    @Nullable
    public AHRS getNavX(){
        if(navx==null)return null;
        return AHRS.getInstance((DeviceInterfaceModule) navx.getController(), navx.getPort(), AHRS.DeviceDataType.kProcessedData, NAVX_DEVICE_UPDATE_RATE_HZ);
    }

    public enum DoorState {
        OPEN(0.5),
        CLOSED(0.67);

        protected final double position;

        DoorState(double position) {
            this.position = position;
        }
    }

    public enum CarriageState {
        OPEN(.5),
        CLOSED(0);

        protected final double position;

        CarriageState(double position) {
            this.position = position;
        }
    }
}
