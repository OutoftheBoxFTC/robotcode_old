package org.ftc7244.robotcontrol;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.ftc7244.robotcontrol.core.Button;
import org.ftc7244.robotcontrol.core.ButtonType;
import org.ftc7244.robotcontrol.core.PressButton;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by OOTB on 10/16/2016.
 */

@TeleOp(name="Westcoast Drive")
public class WestcoastTeleop extends OpMode {

    private WestcoastHardware robot;
    private Button bButton;

    @Override
    public void init() {
        robot = new WestcoastHardware();
        robot.init(this);
        bButton = new PressButton(gamepad2, ButtonType.B);
    }

    @Override
    public void loop() {
        //Core Drive Code
        robot.getDriveRight().setPower(gamepad1.right_stick_y);
        robot.getDriveLeft().setPower(gamepad1.left_stick_y);

        //Set the launcher if  b is pressed
        robot.getLauncher().setPower(gamepad1.b ? -1 : 0);

        //Open the door if a button is pressed
        if (bButton.isPressed()) {


            telemetry.addLine("CLICKED " + System.currentTimeMillis());
            telemetry.update();
        }
        /*if (bButton.isPressed() && !runningLauncher) {
            runningLauncher = true;
            robot.getLauncherDoor().setPosition(1);
            doorTimer.reset();
        }
        if (runningLauncher) {
            if (doorTimer.isValid()) robot.getLauncherDoor().setPosition(0);

        }*/

        //INTAKE
        //If no triggers are pressed stop the lift
        int intakeSpeed = 0;
        //If the right trigger is pressed start the lift
        if (gamepad2.right_trigger > 0) intakeSpeed = 1;
        //If the left trigger is pressed reverse the lift
        if (gamepad2.left_trigger > 0) intakeSpeed = -1;
        robot.getIntake().setPower(intakeSpeed);
    }
}
