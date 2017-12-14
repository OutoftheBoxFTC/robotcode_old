package org.ftc7244.robotcontroller.programs.debug;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.ftc7244.datalogger.Logger;
import org.ftc7244.robotcontroller.autonomous.PIDAutonomous;

/**
 * Created by Out Of The Box on 12/13/2017.
 */
@Autonomous(name = "Turn Random")
public class TurnRandom extends PIDAutonomous {

    @Override
    public void run() throws InterruptedException {
        double degrees = Math.random()*150+20;
        gyroscope.rotate(degrees);
        telemetry.addData("Degrees", degrees);
        telemetry.update();
        while (opModeIsActive()){
            Logger.getInstance().queueData("Reading", gyroProvider.getZ());
        }
    }
}
