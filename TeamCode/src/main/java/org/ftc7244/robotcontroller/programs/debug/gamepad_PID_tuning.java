package org.ftc7244.robotcontroller.programs.debug;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.ftc7244.robotcontroller.autonomous.ControlSystemAutonomous;
import org.ftc7244.robotcontroller.input.Button;
import org.ftc7244.robotcontroller.input.ButtonType;

/**
 * Created by ftc72 on 6/18/2018.
 */
@TeleOp(name="gamepadTests")
public class gamepad_PID_tuning extends OpMode {
    double p, i, d;
    @Override
    public void init() {
        p = 0;
        i = 0;
        d = 0;
    }

    @Override
    public void loop() {
        p += gamepad1.left_stick_y;
        i += gamepad1.right_stick_y;
        d += gamepad1.right_trigger - gamepad1.left_trigger;
        telemetry.addData("P I D", "P: " + p + " I: " + i + " D: " + d);
        telemetry.update();
    }
}
