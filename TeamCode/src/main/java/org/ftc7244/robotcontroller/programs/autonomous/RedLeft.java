package org.ftc7244.robotcontroller.programs.autonomous;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.ftc7244.robotcontroller.autonomous.PIDAutonomous;


/**
 * Created by Eeshwar Laptop on 10/29/2017.
 */
@Autonomous(name = "Red Left")
public class RedLeft extends PIDAutonomous {

    public void run() throws InterruptedException{
        RelicRecoveryVuMark image = imageProvider.getImageReading();
        while(image == RelicRecoveryVuMark.UNKNOWN){
            image = imageProvider.getImageReading();
            sleep(50);
        }
        telemetry.addData("Image", image);
        telemetry.update();

        robot.knockOverJewel(Color.BLUE);//Check color sensor
        robot.getIntakeTop().setPower(-1);
        robot.driveToInch(.3, 39);//Drive off balancing stone
        robot.getSpring().setPosition(0.5);//Spring out glyph
        sleep(750);
        robot.getIntakeServo().setPosition(0.8);
        gyroscope.rotate(-90);//Rotate to face glyph pit
        robot.getIntakeBottom().setPower(-1);
        gyroscope.drive(1, 47);//Drive to glyph pit
        //0.2: 0.75
        robot.getIntakeServo().setPosition(0.2);
        gyroscope.drive(-1, 3);
        switch(image) {
            case LEFT:
                gyroscope.rotate(-95);
                gyroscope.drive(1, 39);
                break;
            case RIGHT:
                gyroscope.rotate(-85);
                gyroscope.drive(1, 39);
                break;
            default:
                gyroscope.rotate(-90);
                gyroscope.drive(1, 37);
                break;
        }
        robot.getIntakeBottom().setPower(1);
        robot.getIntakeTop().setPower(1);
        gyroscope.drive(-.5, 10);
        gyroscope.drive(1, 4);
        gyroscope.drive(-1, 15);
        gyroscope.rotate(180);
        robot.getIntakeTop().setPower(-1);
        robot.getIntakeBottom().setPower(-1);
        robot.getIntakeServo().setPosition(0.8);
        gyroscope.drive(1, 20);
        robot.getIntakeServo().setPosition(0.2);
        gyroscope.drive(-0.7, 20);
        switch(image){
            case LEFT:
                gyroscope.rotate(185);
                break;
            case RIGHT:
                gyroscope.rotate(175);
                break;
            default:
                gyroscope.rotate(182);
                break;
        }
        /*gyroscope.drive(1, 30);
        robot.getIntakeBottom().setPower(1);
        robot.getIntakeTop().setPower(1);
        gyroscope.drive(-1, 10);
        gyroscope.drive(1, 4);
        gyroscope.drive(-0.5, 10);*/
    }
}