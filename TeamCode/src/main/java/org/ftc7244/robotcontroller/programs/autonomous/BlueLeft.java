package org.ftc7244.robotcontroller.programs.autonomous;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.ftc7244.robotcontroller.autonomous.ControlSystemAutonomous;


/**
 * Created by Eeshwar Laptop on 10/29/2017.
 */
@Autonomous(name = "Blue Left")
public class BlueLeft extends ControlSystemAutonomous {

    public void run() throws InterruptedException{
        robot.knockOverJewel(Color.RED);//Check Colour Sensor
        robot.getIntakeTop().setPower(-1);
        robot.driveToInch(.2, 31);//Drive off balancing stone
        gyroscope.rotate(0);//Re-Center the robot
        sleep(200);
        robot.getSpring().setPosition(0.5);//Spring out glyph
        sleep(200);
        RelicRecoveryVuMark image = imageProvider.getImageReading();
        telemetry.addData("Image", image);
        telemetry.update();
        sleep(1000);
        robot.getIntakeServo().setPosition(.2);
        gyroscope.rotate(-45);//Rotate to face glyph pit
        robot.getIntakeBottom().setPower(-1);
        robot.driveIntakeVertical(0.5);
        sleep(200);
        robot.driveIntakeVertical(0);
        gyroscope.drive(0.6, 11);//Drive to glyph pit
        gyroscope.drive(0.6, 12);// Drive into glyph pit
        robot.getIntakeServo().setPosition(0.8);
        sleep(1500);
        gyroscope.drive(-0.6, 10);
        switch(image){
            case RIGHT:
                gyroscope.rotate(154.1);
                gyroscope.drive(0.85, 45);
                break;
            case LEFT:
                gyroscope.rotate(170.5);
                gyroscope.drive(0.85, 37);
                break;
            default:
                gyroscope.rotate(158.3);
                gyroscope.drive(0.85, 43);
        }
        robot.getIntakeBottom().setPower(1);
        robot.getIntakeTop().setPower(1);
        gyroscope.drive(-0.6, 8);
        gyroscope.drive(0.6, 6);
        gyroscope.drive(-0.6, 5);
    }
}