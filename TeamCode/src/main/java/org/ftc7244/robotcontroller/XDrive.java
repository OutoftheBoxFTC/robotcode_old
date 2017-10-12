package org.ftc7244.robotcontroller;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.kauailabs.navx.ftc.AHRS;
import com.qualcomm.hardware.hitechnic.HiTechnicNxtLightSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
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

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuMarkInstanceId;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.ftc7244.robotcontroller.autonomous.Status;
import org.ftc7244.robotcontroller.sensor.SickUltrasonic;

import java.util.Map;
/**
 * Created by Stargamer285 on 10/9/2017.
 */

public class XDrive {
    public static boolean stopIntake = false;
    public static final byte NAVX_DEVICE_UPDATE_RATE_HZ = (byte) 100;
    public static final double COUNTS_PER_INCH = 1120 / (Math.PI * 3);

    @Nullable
    private DcMotor driveTopLeft, driveTopRight, driveBottomLeft, driveBottomRight, launcher, btm_intake_left, btm_intake_right, raising_intake_motor, top_intake_left, top_intake_right, spoolerTop, spoolerBottom, lights;
    @Nullable
    private Servo launcherDoor, beaconPusher, carriageRelease;
    @Nullable
    private AnalogInput lower_input_limit, top_input_limit;
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

    public XDrive(OpMode opMode) {
        this.opMode = opMode;
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
     * This is the codes own way of pausing. This has the the capability of stopping the wait if
     * stop is requested and passing up an exception if it fails as well
     *
     * @param ms the duration to sleep in milliseconds
     * @throws InterruptedException if the code fails to terminate before stop requested
     */
    public static void sleep(long ms) throws InterruptedException {
        long target = System.currentTimeMillis() + ms;
        while (target > System.currentTimeMillis() && !Status.isStopRequested()) Thread.sleep(1);
    }

    /**
     * Identify hardware and then set it up with different objects. Other initialization properties are
     * set to ensure that everything is in the default position or correct mode for the robot.
     */
    public void init() {
        //Initialize or nullify all hardware
        HardwareMap map = opMode.hardwareMap;
        this.driveTopLeft = getOrNull(map.dcMotor, "drive_top_left");
        this.driveTopRight = getOrNull(map.dcMotor, "drive_top_right");
        this.driveBottomLeft = getOrNull(map.dcMotor, "drive_bottom_left");
        this.driveBottomRight = getOrNull(map.dcMotor, "drive_bottom_right");
        this.launcher = getOrNull(map.dcMotor, "launcher");
        this.launcherDoor = getOrNull(map.servo, "launcher_door");
        this.lower_input_limit = getOrNull(map.analogInput, "lower_input_limit");
        this.top_input_limit = getOrNull(map.analogInput, "top_input_limit");
        this.btm_intake_left = getOrNull(map.dcMotor, "btm_intake_left");
        this.btm_intake_right = getOrNull(map.dcMotor, "btm_intake_right");
        this.raising_intake_motor = getOrNull(map.dcMotor, "raising_intake_motor");
        this.top_intake_left = getOrNull(map.dcMotor, "top_intake_left");
        this.top_intake_right = getOrNull(map.dcMotor, "top_intake_right");
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
        if (driveTopLeft != null) driveTopLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        if (driveBottomLeft != null) driveBottomRight.setDirection(DcMotorSimple.Direction.FORWARD);
        if (launcher != null) launcher.setDirection(DcMotorSimple.Direction.REVERSE);
        if (beaconPusher != null) beaconPusher.setPosition(1);
        if (launcherDoor != null) setDoorState(Westcoast.DoorState.CLOSED);
        if (carriageRelease != null) setCarriageState(Westcoast.CarriageState.CLOSED);
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
     * Get the value associated with an id and instead of raising an error return null and log it
     *
     * @param map  the hardware map from the HardwareMap
     * @param name The ID in the hardware map
     * @param <T>  the type of hardware map
     * @return the hardware device associated with the name
     */
    private <T extends HardwareDevice> T getOrNull(@NonNull HardwareMap.DeviceMapping<T> map, String name) {
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
     * Uses the {@link Westcoast#shoot(long)} function to shoot a specified amount of balls. The
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
                setDoorState(Westcoast.DoorState.OPEN);
            }
            //reset the arm to staring position
            setDoorState(Westcoast.DoorState.CLOSED);
        } else {
            launcher.setPower(0);
        }
    }
*/

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
     * Use ${@link Westcoast.DoorState} to either raise the arm to allow a ball in or set it to the zero
     * position to prevent balls to go into the robot.
     *
     * @param state the position of the door
     */
    public void setDoorState(@NonNull Westcoast.DoorState state) {
        launcherDoor.setPosition(state.position);
    }

    /**
     * Uses the ${@link Westcoast.CarriageState} to release the lift or lock the lift.
     *
     * @param state of the lock on the robot
     */
    public void setCarriageState(@NonNull Westcoast.CarriageState state) {
        carriageRelease.setPosition(state.position);
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

    @Nullable
    public DcMotor getLauncher() {
        return this.launcher;
    }



    @Nullable
    public AnalogInput getLower_input_limit(){return this.lower_input_limit;}

    @Nullable
    public AnalogInput getTop_input_limit(){return this.top_input_limit;}

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

    public enum PosAxis{
        X,Y,Z
    }
}
