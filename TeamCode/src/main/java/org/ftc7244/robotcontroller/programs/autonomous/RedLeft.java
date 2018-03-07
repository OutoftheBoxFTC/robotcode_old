package org.ftc7244.robotcontroller.programs.autonomous;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.ftc7244.robotcontroller.autonomous.ControlSystemAutonomous;


/**
 * Created by Eeshwar Laptop on 10/29/2017.
 */
@Autonomous(name = "Red Left")
public class RedLeft extends ControlSystemAutonomous {

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
        gyroscopePID.rotate(-90);//Rotate to face glyph pit
        robot.getIntakeBottom().setPower(-1);
        gyroscopePID.drive(1, 47);//Drive to glyph pit
        //0.2: 0.75
        robot.getIntakeServo().setPosition(0.2);
        gyroscopePID.drive(-1, 3);
        switch(image) {
            case LEFT:
                gyroscopePID.rotate(-95);
                gyroscopePID.drive(1, 39);
                break;
            case RIGHT:
                gyroscopePID.rotate(-85);
                gyroscopePID.drive(1, 38);
                break;
            default:
                gyroscopePID.rotate(-90);
                gyroscopePID.drive(1, 38);
                break;
        }
        robot.getIntakeBottom().setPower(1);
        robot.getIntakeTop().setPower(1);
        gyroscopePID.drive(-.5, 10);
        gyroscopePID.drive(1, 6);
        gyroscopePID.drive(-1, 15);
        gyroscopePID.rotate(180);
        robot.getIntakeTop().setPower(-1);
        robot.getIntakeBottom().setPower(-1);
        robot.getIntakeServo().setPosition(0.8);
        gyroscopePID.drive(1, 20);
        robot.getIntakeServo().setPosition(0.2);
        gyroscopePID.drive(-0.7, 20);
        switch(image){
            case LEFT:
                gyroscopePID.rotate(185);
                break;
            case RIGHT:
                gyroscopePID.rotate(175);
                break;
            default:
                gyroscopePID.rotate(182);
                break;
        }
        /*gyroscopePID.drive(1, 30);
        robot.getIntakeBottom().setPower(1);
        robot.getIntakeTop().setPower(1);
        gyroscopePID.drive(-1, 10);
        gyroscopePID.drive(1, 4);
        gyroscopePID.drive(-0.5, 10);*/
    }
}