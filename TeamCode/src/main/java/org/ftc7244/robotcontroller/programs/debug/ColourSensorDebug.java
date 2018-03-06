package org.ftc7244.robotcontroller.programs.debug;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.ftc7244.robotcontroller.hardware.Westcoast;

/**
 * Created by ftc72 on 2/19/2018.
 */
@Autonomous(name = "Color Sensor Debug")
@Disabled
public class ColourSensorDebug extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        Westcoast robot = new Westcoast(this);
        Double grayscale;
        robot.init();
        waitForStart();
        while(opModeIsActive()){
            grayscale = ( (0.3 * robot.getJewelSensor().red()) + (0.59 * robot.getJewelSensor().green()) + (0.11 * robot.getJewelSensor().blue()) );
            telemetry.addData("R", robot.getJewelSensor().red());
            telemetry.addData("G", robot.getJewelSensor().green());
            telemetry.addData("B", robot.getJewelSensor().blue());
            telemetry.addData("Distance (INCH)", robot.getJewelDistance().getDistance(DistanceUnit.INCH));
            telemetry.addData("Grayscale", grayscale);
            telemetry.addData("NaN", grayscale.byteValue());
            telemetry.update();
        }
    }
}
