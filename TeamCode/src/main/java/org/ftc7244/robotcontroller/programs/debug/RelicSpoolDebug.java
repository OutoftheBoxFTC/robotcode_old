package org.ftc7244.robotcontroller.programs.debug;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.ftc7244.robotcontroller.hardware.Westcoast;

/**
 * Created by ftc72 on 3/13/2018.
 */
@TeleOp(name = "Relic Spool Debug")
public class RelicSpoolDebug extends LinearOpMode{
    private Westcoast robot;

    @Override
    public void runOpMode() throws InterruptedException {
        robot = new Westcoast(this);
        robot.init();
        waitForStart();
        robot.initServos();
        while (opModeIsActive()){
            telemetry.addData("Spool", robot.getRelicSpool().getCurrentPosition());
            telemetry.update();
        }
    }
}
