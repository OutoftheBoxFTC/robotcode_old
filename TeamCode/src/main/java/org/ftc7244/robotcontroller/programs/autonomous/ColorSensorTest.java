package org.ftc7244.robotcontroller.programs.autonomous;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.ftc7244.datalogger.Logger;
import org.ftc7244.robotcontroller.hardware.Westcoast;
import org.ftc7244.robotcontroller.sensor.DataFilter;

/**
 * Created by BeaverDuck on 11/27/17.
 */
@Autonomous(name="Color Sensor Test")
public class ColorSensorTest extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        Westcoast robot = new Westcoast(this);
        robot.init();
        waitForStart();
        while(opModeIsActive()){
            telemetry.addData("RED", robot.isColor(Color.RED));
            telemetry.addData("BLUE", robot.isColor(Color.BLUE));
            telemetry.update();
        }
    }
}