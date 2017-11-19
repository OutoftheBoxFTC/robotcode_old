package org.ftc7244.robotcontroller.programs.autonomous.RelicRecoveryAutonomous;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.ftc7244.robotcontroller.autonomous.bases.RelicRecoveryPIDAutonamous;
import org.ftc7244.robotcontroller.sensor.gyroscope.RevIMUGyroscopeProvider;

/**
 * Created by FTC 7244 on 10/29/2017.
 */
@Autonomous(name = "Colour Sensor")
public class TestAutonamousColourSensor extends RelicRecoveryPIDAutonamous{
    @Override
    public void run() throws InterruptedException {
        waitForStart();
        while(opModeIsActive()) {
            if(robot.isColor(Color.BLUE)){
                telemetry.addData("Colour", "Blue");
            }else if(robot.isColor(Color.RED)){
                telemetry.addData("Colour", "Red");
            }else{
                telemetry.addData("Colour", "Not Found");
            }
        }
    }
}
