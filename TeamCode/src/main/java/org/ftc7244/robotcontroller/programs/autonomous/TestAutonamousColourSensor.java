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
        waitForStart();
        robot.knockOverJewel(Color.RED);
    }
}