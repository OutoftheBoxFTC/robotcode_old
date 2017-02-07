package org.ftc7244.robotcontroller;

import android.graphics.Color;

import com.kauailabs.navx.ftc.AHRS;
import com.qualcomm.hardware.hitechnic.HiTechnicNxtLightSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;

import org.ftc7244.robotcontroller.autonomous.Status;
import org.ftc7244.robotcontroller.sensor.SickUltrasonic;

import java.util.Map;

import lombok.Getter;

public class Westcoast {


    public static final byte NAVX_DEVICE_UPDATE_RATE_HZ = 50;
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


    /**
     * Obtains the NavX-Micro from the hardware map expecting a "navx" and then uses reflection
     * to obtain all the information to properly setup the sensor
     *
     * @param map with the "navx" device
     * @return setup navx sensor
     */
    public static AHRS getNavX(HardwareMap map) {
        final I2cDevice navx = map.i2cDevice.get("navx");
        return AHRS.getInstance((DeviceInterfaceModule) navx.getController(), navx.getPort(), AHRS.DeviceDataType.kProcessedData, NAVX_DEVICE_UPDATE_RATE_HZ);
    }

    @Getter
    private DcMotor driveLeft, driveRight, launcher, intake, spooler;
    @Getter
    private Servo launcherDoor, beaconPusher, carriageRelease;
    @Getter
    private AnalogInput launcherLimit;
    private OpMode opMode;
    @Getter
    private ColorSensor beaconSensor;
    @Getter
    private HiTechnicNxtLightSensor leadingLight, trailingLight;
    @Getter
    private SickUltrasonic leadingUltrasonic, trailingUltrasonic;

    public Westcoast(OpMode opMode) {
        this.opMode = opMode;
    }

    /**
     * Identify hardware and then set it up with different objects. Other initialization properties are
     * set to ensure that everything is in the default position or correct mode for the robot.
     */
    public void init() {
        HardwareMap map = opMode.hardwareMap;
        this.driveLeft = getOrNull(map.dcMotor, "drive_left");
        this.driveRight = getOrNull(map.dcMotor, "drive_right");
        this.launcher = getOrNull(map.dcMotor, "launcher");
        this.launcherDoor = getOrNull(map.servo, "launcher_door");
        this.launcherLimit = getOrNull(map.analogInput, "launcher_limit");
        this.intake = getOrNull(map.dcMotor, "intake");
        this.beaconSensor = getOrNull(map.colorSensor, "beacon_sensor");
        this.beaconPusher = getOrNull(map.servo, "beacon_pusher");
        this.spooler = getOrNull(map.dcMotor, "spooler");
        this.carriageRelease = getOrNull(map.servo, "carriage_release");
        this.leadingUltrasonic = new SickUltrasonic(getOrNull(map.analogInput, "leading_ultrasonic"));
        this.trailingUltrasonic = new SickUltrasonic(getOrNull(map.analogInput, "trailing_ultrasonic"));
        this.leadingLight = (HiTechnicNxtLightSensor) getOrNull(map.lightSensor, "leading_light");
        this.trailingLight = (HiTechnicNxtLightSensor) getOrNull(map.lightSensor, "trailing_light");

        //Set the default direction for all the hardware and also initialize default positions
        if (driveLeft != null) driveLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        if (launcher != null) launcher.setDirection(DcMotorSimple.Direction.REVERSE);
        if (beaconPusher != null) beaconPusher.setPosition(1);
        if (launcherDoor != null) setDoorState(DoorState.CLOSED);
        if (carriageRelease != null) setCarriageState(CarriageState.CLOSED);
    }

    /**
     * Get the value associated with an id and instead of raising an error return null and log it
     *
     * @param map  the hardware map from the HardwareMap
     * @param name The ID in the hardware map
     * @param <T>  the type of hardware map
     * @return the hardware device associated with the name
     */
    private <T extends HardwareDevice> T getOrNull(HardwareMap.DeviceMapping<T> map, String name) {
        for (Map.Entry<String, T> item : map.entrySet()) {
            if (!item.getKey().equalsIgnoreCase(name)) {
                continue;
            }
            return item.getValue();
        }
        opMode.telemetry.addLine("ERROR: " + name + " not found!");
        RobotLog.e("ERROR: " + name + " not found!");
        return null;
    }

    /**
     * This is the codes own way of pausing. This has the the capability of stopping the wait if
     * stop is requested and passing up an exception if it fails as well
     *
     * @param ms the duration to sleep in milliseconds
     * @throws InterruptedException if the code fails to terminate before stop requested
     */
    private void sleep(long ms) throws InterruptedException {
        long target = System.currentTimeMillis() + ms;
        while (target > System.currentTimeMillis() && !Status.isStopRequested()) wait();
    }

    /**
     * Uses the {@link Westcoast#shoot(long)} function to shoot a specified amount of balls. The
     * only time it will end before shooting is if the code is manually stopped; otherwise, it will
     * continue to the specified amount.
     *
     * @param count the amount of times it will shoot
     * @param delay the time in milliseconds to wait before each shoot
     * @throws InterruptedException if the code fails to terminate before stop requested
     */
    public void shootLoop(int count, long delay) throws InterruptedException {
        for (int i = 0; i < count; i++) {
            if (Status.isStopRequested()) break;
            this.shoot(delay);
        }
    }

    /**
     *  A tuned tool to shoot a ball from the robot with many fail-safes integrated to prevent the
     *  shooting from not completing. First it will spin no more than 1000 milliseconds or until the
     *  limit switch is triggered. Then if the shooter will continue to run for 200 more milliseconds
     *  and lift the arm up for the remaining 500 milliseconds to load another ball. If anything fails
     *  it will be reset to its normal positions and turned off.
     *
     * @param delay the time in milliseconds to wait before each shot
     * @throws InterruptedException if the code fails to terminate before stop requested
     */
    public void shoot(long delay) throws InterruptedException {
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
                setDoorState(DoorState.OPEN);
            }
            //reset the arm to staring position
            setDoorState(DoorState.CLOSED);
        } else {
            launcher.setPower(0);
        }
    }

    /**
     * Depending on the color specified ${@link Color#BLUE} or ${@link Color#RED} the robot will do
     * a simple greater than comparison to see if the color specified is greater than the other.
     *
     * @param color only  ${@link Color#BLUE} or ${@link Color#RED} can be inputted
     * @return whether it sees the color or not
     */
    public boolean isColor(int color) {
        RobotLog.ii("Color", beaconSensor.blue() + ":" + beaconSensor.red());
        switch (color) {
            case Color.BLUE:
                return beaconSensor.blue() > beaconSensor.red();
            case Color.RED:
                return beaconSensor.blue() < beaconSensor.red();
            default:
                RobotLog.ee("ERROR", "Color does not exist!");
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
        sleep(750);
    }

    /**
     * Use ${@link DoorState} to either raise the arm to allow a ball in or set it to the zero
     * position to prevent balls to go into the robot.
     *
     * @param state the position of the door
     */
    public void setDoorState(DoorState state) {
        launcherDoor.setPosition(state.position);
    }

    /**
     * Uses the ${@link CarriageState} to release the lift or lock the lift.
     *
     * @param state of the lock on the robot
     */
    public void setCarriageState(CarriageState state) {
        carriageRelease.setPosition(state.position);
    }

    public enum DoorState {
        OPEN(0.7),
        CLOSED(1);

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
