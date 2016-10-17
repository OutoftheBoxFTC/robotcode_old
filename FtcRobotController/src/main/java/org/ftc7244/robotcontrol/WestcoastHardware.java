package org.ftc7244.robotcontrol;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by OOTB on 10/9/2016.
 */

public class WestcoastHardware {

    private DcMotor driveLeft;
    private DcMotor driveRight;
    private DcMotor launcher;

    public void init(HardwareMap map) {
        this.driveLeft = map.dcMotor.get("drive_left");
        this.driveRight = map.dcMotor.get("drive_right");
        //this.launcher = map.dcMotor.get("launcher");
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
}
