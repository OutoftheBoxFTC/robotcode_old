package org.ftc7244.robotcontrol;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

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

    private WestcoastHardware robot;
    private Button bButton, triggerL, triggerR;
    private AtomicBoolean runningLauncher;
    private ExecutorService service;

    @Override
    public void init() {
        robot = new WestcoastHardware(this);
        bButton = new Button(gamepad2, ButtonType.B);
        triggerL = new Button(gamepad2, ButtonType.LEFT_TRIGGER);
        triggerR = new Button(gamepad2, ButtonType.RIGHT_TRIGGER);
        runningLauncher = new AtomicBoolean(false);
        service = Executors.newCachedThreadPool();

        robot.init();
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
                    //Put a pause
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    ElapsedTime timer = new ElapsedTime();
                    boolean failed = false;
                    //Spin the launcher
                    do {
                        if (timer.milliseconds() >= 1000) failed = true;
                        robot.getLauncher().setPower(1);
                    } while (Math.round(robot.getLauncherLimit().getVoltage()) != 0 && !failed);

                    //If the code hasn't failed allow the arm to lift or reset spinner
                    if (!failed) {
                        timer.reset();
                        while (timer.milliseconds() <= 500) {
                            //Stop the spinner after a delay
                            if (timer.milliseconds() > 250) robot.getLauncher().setPower(0);

                            //lift the arm
                            robot.getLauncherDoor().setPosition(.7);
                        }
                        //reset the arm to staring position
                        robot.getLauncherDoor().setPosition(1);
                    } else {
                        robot.getLauncher().setPower(0);
                    }

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
