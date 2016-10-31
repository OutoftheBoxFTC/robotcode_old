package org.ftc7244.robotcontrol;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.ftc7244.robotcontrol.core.Button;
import org.ftc7244.robotcontrol.core.ButtonType;
import org.ftc7244.robotcontrol.core.PressButton;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by OOTB on 10/16/2016.
 */

@TeleOp(name="Westcoast Drive")
public class WestcoastTeleop extends OpMode {

    private WestcoastHardware robot;
    private Button bButton;
    private AtomicBoolean runningLauncher;
    private ExecutorService service;

    @Override
    public void init() {
        robot = new WestcoastHardware();
        robot.init(this);
        bButton = new PressButton(gamepad2, ButtonType.B);
        runningLauncher = new AtomicBoolean(false);
        service = Executors.newCachedThreadPool();
    }

    @Override
    public void loop() {
        //Core Drive Code
        robot.getDriveRight().setPower(gamepad1.right_stick_y);
        robot.getDriveLeft().setPower(gamepad1.left_stick_y);

        //Open the door if a button is pressed
        if (bButton.isPressed() && !runningLauncher.get()) {
            runningLauncher.set(true);
            service.execute(new Runnable() {
                @Override
                public void run() {
                    telemetry.addLine("Info " + System.currentTimeMillis());
                    telemetry.update();
                    while (Math.round(robot.getLauncherLimit().getVoltage()) != 0) {
                        robot.getLauncher().setPower(-1);
                    }

                    ElapsedTime timer = new ElapsedTime();
                    while (timer.milliseconds() <= 500) {
                        if (timer.milliseconds() > 250) robot.getLauncher().setPower(0);
                        robot.getLauncherDoor().setPosition(.7);
                    }
                    robot.getLauncherDoor().setPosition(1);
                    runningLauncher.set(false);
                }
            });
        }

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
