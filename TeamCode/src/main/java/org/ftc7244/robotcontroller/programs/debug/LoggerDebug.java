package org.ftc7244.robotcontroller.programs.debug;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.ftc7244.datalogger.Logger;

/**
 * Created by BeaverDuck on 11/19/17.
 */
@Autonomous(name = "Logger Debug")
public class LoggerDebug extends LinearOpMode{
    @Override
    public void runOpMode() throws InterruptedException {
        waitForStart();
        for (int i = 0; i < 100000; i++) {
            Logger.getInstance().addData("Test", Math.random());
        }
    }
}