package org.ftc7244.robotcontroller.programs.debug;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.ftc7244.datalogger.Logger;
import org.ftc7244.robotcontroller.autonomous.ControlSystemAutonomous;

/**
 * Created by Out Of The Box on 12/13/2017.
 *
 * Used to tune our PID loop. Rotates to a random value as to make sure to tune for any degree
 * rotation, and not just for rotation to a specific degree
 */
@Autonomous(name = "Turn Random")
public class TurnRandom extends ControlSystemAutonomous {

    @Override
    public void run() throws InterruptedException {
        double degrees = Math.random()*360;
        gyroscopePID.rotate(degrees);
        telemetry.addData("Degrees", degrees);
        telemetry.update();
        while (opModeIsActive()){
            Logger.getInstance().queueData("Reading", gyroProvider.getZ());
        }
    }
}
