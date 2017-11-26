package org.ftc7244.robotcontroller.programs.autonomous;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.ftc7244.robotcontroller.hardware.Westcoast;
import org.ftc7244.robotcontroller.sensor.gyroscope.RevIMUGyroscopeProvider;

/**
 * Created by FTC 7244 on 10/29/2017.
 */
@Autonomous(name = "Colour Sensor")
public class TestAutonamousColourSensor extends LinearOpMode{
    public void runOpMode() throws InterruptedException {
        Westcoast robot = new Westcoast(this);
        robot.init();
        robot.getJewelVerticle().setPosition(0.5);
        robot.getJewelHorizontal().setPosition(1);
        waitForStart();
        robot.getJewelHorizontal().setPosition(0);
        robot.getJewelVerticle().setPosition(0);
        sleep(1000);
        while(opModeIsActive()) {
            boolean isRed = robot.isColor(Color.RED);
            boolean isBlue = robot.isColor(Color.BLUE);
            if (isRed) {
                robot.getJewelHorizontal().setPosition(1);
                telemetry.addData("Colour", "Red");
                telemetry.addData("Distance", robot.getJewelDistance().getDistance(DistanceUnit.INCH));
            } else if (isBlue) {
                robot.getJewelHorizontal().setPosition(-1);
                telemetry.addData("Colour", "Blue");
                telemetry.addData("Distance", robot.getJewelDistance().getDistance(DistanceUnit.INCH));
            } else {
                telemetry.addData("Colour", "Not Found");
                telemetry.addData("Distance", robot.getJewelDistance().getDistance(DistanceUnit.INCH));
            }
            telemetry.update();
        }
    }
}