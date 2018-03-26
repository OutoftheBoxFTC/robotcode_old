package org.ftc7244.robotcontroller.programs.debug;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.ftc7244.robotcontroller.autonomous.ControlSystemAutonomous;

/**
 * Created by ftc72 on 3/25/2018.
 */
@Autonomous(name = "Encoder Tick Record")
@Disabled
public class EncoderTickRecord extends ControlSystemAutonomous {
    @Override
    public void run() throws InterruptedException {
        while (opModeIsActive()){
            telemetry.addData("Encoder", robot.getDriveEncoderAverage());
            telemetry.update();
        }
    }
}
