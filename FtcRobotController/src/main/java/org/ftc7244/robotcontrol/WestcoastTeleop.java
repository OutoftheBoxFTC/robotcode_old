package org.ftc7244.robotcontrol;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by OOTB on 10/16/2016.
 */

@TeleOp(name="Westcoast Drive")
public class WestcoastTeleop extends OpMode {

    private WestcoastHardware robot;

    @Override
    public void init() {
        robot = new WestcoastHardware();
        robot.init(hardwareMap);
    }

    @Override
    public void loop() {
        robot.getDriveRight().setPower(gamepad1.right_stick_y);
        robot.getDriveLeft().setPower(gamepad1.left_stick_y);

        robot.getLauncher().setPower(gamepad2.right_bumper ? -100 : 0);
    }
}
