package org.ftc7244.robotcontroller.programs.debug;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.ftc7244.robotcontroller.hardware.Westcoast;
import org.ftc7244.robotcontroller.input.Button;
import org.ftc7244.robotcontroller.input.ButtonType;

/**
 * Created by ftc72 on 4/19/2018.
 */
@TeleOp(name="Intake Lift Test")
@Disabled
public class IntakeLiftTest extends OpMode {
    Westcoast robot = new Westcoast(this);
    Button raiseButton, lowerButton;
    @Override
    public void init() {
        robot.init();
        robot.initServos();
        raiseButton = new Button(gamepad1, ButtonType.D_PAD_UP);
        lowerButton = new Button(gamepad1, ButtonType.D_PAD_DOWN);
    }

    @Override
    public void loop() {
        robot.getIntakeLift().setPower(raiseButton.isPressed() ? 0.5 : lowerButton.isPressed() ? -0.5 : 0.1);
        telemetry.addData("Lift", robot.getIntakeLift().getCurrentPosition());
        telemetry.update();
    }
}
