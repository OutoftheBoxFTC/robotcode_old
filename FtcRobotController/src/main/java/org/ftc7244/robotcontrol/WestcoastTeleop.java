package org.ftc7244.robotcontrol;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.ftc7244.robotcontrol.core.Button;
import org.ftc7244.robotcontrol.core.ButtonType;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by OOTB on 10/16/2016.
 */

@TeleOp(name="Westcoast Drive")
public class WestcoastTeleop extends OpMode {

    private Westcoast robot;
    private Button bButton, triggerL, triggerR, xButton, yButton;
    private AtomicBoolean runningLauncher;
    private ExecutorService service;

    @Override
    public void init() {
        robot = new Westcoast(this);
        bButton = new Button(gamepad2, ButtonType.A);
        triggerL = new Button(gamepad2, ButtonType.LEFT_TRIGGER);
        triggerR = new Button(gamepad2, ButtonType.RIGHT_TRIGGER);
        xButton = new Button(gamepad2, ButtonType.X);
        yButton = new Button(gamepad2, ButtonType.Y);
        runningLauncher = new AtomicBoolean(false);
        service = Executors.newCachedThreadPool();

        robot.init();
    }

    @Override
    public void loop() {
        //Core Drive Code
        robot.getDriveRight().setPower(-gamepad1.right_stick_y);
        robot.getDriveLeft().setPower(-gamepad1.left_stick_y);

        //If we are not in running launcher mode then allow for manual
        if (!runningLauncher.get()) {
            robot.getLauncher().setPower(xButton.isPressed() ? 1 : 0);
            robot.setDoorState(yButton.isPressed() ? Westcoast.DoorState.OPEN : Westcoast.DoorState.CLOSED);
        }

        //Run the automatic shoot system
        if (bButton.isPressed() && !runningLauncher.get()) {
            runningLauncher.set(true);
            service.execute(new Runnable() {
                @Override
                public void run() {
                    robot.shoot(50);
                    runningLauncher.set(false);
                }
            });
        }

        //INTAKE
        //If no triggers are pressed stop the lift
        int intakeSpeed = 0;
        //If the right trigger is pressed start the lift
        if (triggerR.isPressed()) intakeSpeed = 1;
        //If the left trigger is pressed reverse the lift
        if (triggerL.isPressed()) intakeSpeed = -1;
        robot.getIntake().setPower(intakeSpeed);
    }
}
