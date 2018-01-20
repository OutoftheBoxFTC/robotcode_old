package org.ftc7244.robotcontroller.programs.autonomous;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.ftc7244.robotcontroller.autonomous.PIDAutonomous;


/**
 * Created by Eeshwar Laptop on 10/29/2017.
 */
@Autonomous(name = "Blue Left")
public class BlueLeft extends PIDAutonomous {

    public void run() throws InterruptedException{
        robot.knockOverJewel(Color.RED);//Check color sensor
        robot.getIntakeTop().setPower(-1);
        robot.driveToInch(.2, 28);//Drive off balancing stone
        gyroscope.rotate(0);//Re-Center the robot
        sleep(200);
        robot.getSpring().setPosition(0.6);//Spring out glyph
        sleep(200);
        RelicRecoveryVuMark image = imageProvider.getImageReading();
        telemetry.addData("Image", image);
        telemetry.update();
        sleep(1000);
        robot.getIntakeServo().setPosition(.5);
        gyroscope.rotate(-45);//Rotate to face glyph pit
        robot.getIntakeBottom().setPower(-1);
        robot.driveIntakeVertical(0.5);
        sleep(200);
        robot.driveIntakeVertical(0);
        gyroscope.drive(0.4, 11);//Drive to glyph pit
        gyroscope.drive(0.3, 12);// Drive into glyph pit
        robot.getIntakeServo().setPosition(.9);
        sleep(1500);
        gyroscope.drive(-0.3, 10);
        switch(image){
            case RIGHT:
                gyroscope.rotate(154.1);
                gyroscope.drive(0.5, 43);
                break;
            case LEFT:
                gyroscope.rotate(170.5);
                gyroscope.drive(0.5, 35);
                break;
            default:
                gyroscope.rotate(161.3);
                gyroscope.drive(0.5, 41);
        }
        robot.getIntakeBottom().setPower(1);
        robot.getIntakeTop().setPower(1);
        gyroscope.drive(-0.2, 8);
        gyroscope.drive(0.5, 6);
        gyroscope.drive(-0.5, 5);
    }
}