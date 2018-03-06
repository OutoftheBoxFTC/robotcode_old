package org.ftc7244.robotcontroller.programs.debug;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.ftc7244.robotcontroller.autonomous.ControlSystemAutonomous;

/**
 * Created by ftc72 on 3/5/2018.
 */
@Autonomous
public class LimitSwitchDebug extends ControlSystemAutonomous{
    @Override
    public void run() throws InterruptedException {
        waitForStart();
        while(opModeIsActive()){
            telemetry.addData("Pressed?", robot.getBottomIntakeSwitch().getVoltage() > 0);
            telemetry.update();
            robot.getJewelHorizontal().setPosition(robot.getBottomIntakeSwitch().getVoltage() > 0 ? 0.33 : 0.56);
        }
    }
}
