package org.ftc7244.robotcontroller;

import android.graphics.Color;

import com.qualcomm.hardware.hitechnic.HiTechnicNxtLightSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;

import org.ftc7244.robotcontroller.autonomous.Status;
import org.ftc7244.robotcontroller.sensor.SickUltrasonic;

import java.util.Map;

import lombok.Getter;

/**
 * Created by OOTB on 10/9/2016.
 */

public class Westcoast {

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
     * Identify the hardware in the robot and then
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

    private void sleep(long ms) throws InterruptedException {
        long target = System.currentTimeMillis() + ms;
        while (target > System.currentTimeMillis() && !Status.isStopRequested()) wait();
    }

    public void shootLoop(int count, long delay) throws InterruptedException {
        for (int i = 0; i < count; i++) {
            if (Status.isStopRequested()) break;
            this.shoot(delay);
        }
    }

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

    public boolean isColor(int color) {
        RobotLog.ii("Color", beaconSensor.blue() + ":" + beaconSensor.red());
        switch (color) {
            case Color.BLUE:
                return beaconSensor.blue() > beaconSensor.red();
            case Color.RED:
                RobotLog.ii("Color", "We are reading red");
                return beaconSensor.blue() < beaconSensor.red();
            default:
                RobotLog.ee("ERROR", "Color does not exist!");
                return false;
        }
    }

    public void pushBeacon() throws InterruptedException {
        beaconPusher.setPosition(0);
        sleep(750);
        beaconPusher.setPosition(1);
        sleep(750);
    }

    public void setDoorState(DoorState status) {
        launcherDoor.setPosition(status.position);
    }

    public void setCarriageState(CarriageState status) {
        this.carriageRelease.setPosition(status.position);
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
