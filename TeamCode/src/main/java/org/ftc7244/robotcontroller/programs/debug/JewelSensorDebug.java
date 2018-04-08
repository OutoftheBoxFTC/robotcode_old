package org.ftc7244.robotcontroller.programs.debug;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.ftc7244.robotcontroller.hardware.Westcoast;

@Autonomous(name = "Jewel Sensor Debug")
//@Disabled
public class JewelSensorDebug extends LinearOpMode {
    @Override
    public void runOpMode(){
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
