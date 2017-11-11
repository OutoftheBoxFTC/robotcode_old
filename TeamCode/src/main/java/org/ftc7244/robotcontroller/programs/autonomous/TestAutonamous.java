package org.ftc7244.robotcontroller.programs.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.ftc7244.datalogger.Logger;
import org.ftc7244.robotcontroller.autonomous.bases.RelicRecoveryPIDAutonamous;

/**
 * Created by FTC 7244 on 10/29/2017.
 */
@Autonomous(name = "VexTest")
public class TestAutonamous extends RelicRecoveryPIDAutonamous{
    @Override
    public void run() throws InterruptedException {
        telemetry.addData("Motor Communications with:", robot.getIntakeBtmRt().getDeviceName());
    }
}
