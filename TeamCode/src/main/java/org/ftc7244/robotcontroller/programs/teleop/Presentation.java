package org.ftc7244.robotcontroller.programs.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.ftc7244.robotcontroller.hardware.Westcoast;
import org.ftc7244.robotcontroller.input.Button;
import org.ftc7244.robotcontroller.input.ButtonType;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

@TeleOp
public class Presentation extends OpMode {

    private Westcoast robot;
    private Button a;
    private ExecutorService service;
    private AtomicBoolean waving;

    @Override
    public void init() {
        robot = new Westcoast(this);
        a = new Button(gamepad1, ButtonType.A);
        waving = new AtomicBoolean(false);
        service = Executors.newCachedThreadPool();


        robot.init();
    }

    @Override
    public void loop() {
        if (a.isPressed() && !waving.get()) {
            waving.set(true);
            service.execute(new Runnable() {
                @Override
                public void run() {

                    for (int i = 0; i < 3; i++) {
                        try {
                            Thread.sleep(250);
                            robot.getLauncherDoor().setPosition(.5);
                            Thread.sleep(250);
                            robot.getLauncherDoor().setPosition(.3);
                        } catch (InterruptedException e) {
                            //ignore
                        }
                    }
                    try {
                        Thread.sleep(250);
                    } catch (Exception e) {
                        //ignore
                    }
                    robot.getLauncherDoor().setPosition(1);
                    waving.set(false);
                }
            });
        }
        robot.getLights().setPower(1);
    }

    @Override
    public void stop() {
        service.shutdown();
        super.stop();
    }
}
