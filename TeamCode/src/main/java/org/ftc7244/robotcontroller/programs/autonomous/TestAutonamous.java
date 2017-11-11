package org.ftc7244.robotcontroller.programs.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.ftc7244.datalogger.Logger;
import org.ftc7244.robotcontroller.autonomous.bases.RelicRecoveryPIDAutonamous;
import org.ftc7244.robotcontroller.hardware.RelicRecoveryWestcoast;

/**
 * Created by FTC 7244 on 10/29/2017.
 */
@Autonomous(name = "VexTest")
public class TestAutonamous extends LinearOpMode{
    public void runOpMode(){
        RelicRecoveryWestcoast robot = new RelicRecoveryWestcoast(this);
        robot.init();
        waitForStart();
        while(opModeIsActive()) {
            telemetry.addData("Motor", robot.getIntakeBtmRt().getManufacturer());
            telemetry.update();
            robot.getIntakeBtmRt().setPower(0.5);
        }
    }
}
