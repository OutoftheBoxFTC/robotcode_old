package org.ftc7244.robotcontrol;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.Map;

/**
 * Created by OOTB on 10/9/2016.
 */

public class WestcoastHardware {

    private DcMotor driveLeft;
    private DcMotor driveRight;
    private DcMotor launcher;
    private Servo launcherDoor;
    private DcMotor intake;
    private AnalogInput launcherLimit;
    private OpMode opMode;

    public WestcoastHardware(OpMode opMode) {
        this.opMode = opMode;
    }

    public void init() {
        HardwareMap map = opMode.hardwareMap;
        this.driveLeft = getOrNull(map.dcMotor, "drive_left");
        this.driveRight = getOrNull(map.dcMotor, "drive_right");
        this.launcher = getOrNull(map.dcMotor, "launcher");
        this.launcherDoor = getOrNull(map.servo, "launcher_door");
        this.launcherLimit = getOrNull(map.analogInput, "launcher_limit");
        this.intake = getOrNull(map.dcMotor, "intake");

        initHardware();
    }

    public void initHardware() {
        if (driveLeft != null) driveLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        if (driveRight != null) driveRight.setDirection(DcMotorSimple.Direction.REVERSE);
        if (launcher != null) launcher.setDirection(DcMotorSimple.Direction.REVERSE);
        if (launcherDoor != null) launcherDoor.setPosition(1 );
    }

    
    private <T extends HardwareDevice> T getOrNull(HardwareMap.DeviceMapping<T> map, String name) {
        for (Map.Entry<String, T> item : map.entrySet()) {
            if (!item.getKey().equalsIgnoreCase(name)) {
                continue;
            }
            return item.getValue();
        }
        opMode.telemetry.addLine("ERROR: " + name + " not found!");
        System.err.print("ERROR: " + name + " not found!");
        return null;
    }

    public DcMotor getDriveLeft() {
        return driveLeft;
    }

    public DcMotor getDriveRight() {
        return driveRight;
    }

    public DcMotor getLauncher() {
        return launcher;
    }

    public Servo getLauncherDoor() { return launcherDoor; }

    public DcMotor getIntake() { return intake; }

    public AnalogInput getLauncherLimit() {
        return launcherLimit;
    }
}
