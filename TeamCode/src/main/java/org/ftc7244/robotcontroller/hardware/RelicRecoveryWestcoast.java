package org.ftc7244.robotcontroller.hardware;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by FTC 7244 on 10/16/2017.
 */

public class RelicRecoveryWestcoast extends Hardware{
    private DcMotor left1, left2, right1, right2;
    public RelicRecoveryWestcoast(OpMode opMode) {
        super(opMode);
    }

    @Override
    public void init() {
        HardwareMap hardwareMap = opMode.hardwareMap;
        left1 = getOrNull(hardwareMap.dcMotor, "left_1");
        left2 = getOrNull(hardwareMap.dcMotor, "left_2");
        right1 = getOrNull(hardwareMap.dcMotor, "right_1");
        right2 = getOrNull(hardwareMap.dcMotor, "right_2");

        left1.setDirection(DcMotorSimple.Direction.REVERSE);
        left2.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public DcMotor getLeft1() {
        return left1;
    }

    public DcMotor getLeft2() {
        return left2;
    }

    public DcMotor getRight1() {
        return right1;
    }

    public DcMotor getRight2() {
        return right2;
    }
}
