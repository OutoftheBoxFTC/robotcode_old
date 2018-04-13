package org.ftc7244.robotcontroller.programs.autonomous;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.ftc7244.robotcontroller.autonomous.ControlSystemAutonomous;
import org.ftc7244.robotcontroller.autonomous.terminators.ColorSensorTerminator;


/**
 * Created by Eeshwar Laptop on 10/29/2017.
 */
@Autonomous(name = "Red Left")
public class RedLeft extends ControlSystemAutonomous {

    public void run(){
        RelicRecoveryVuMark image = imageProvider.getImageReading();
        long lastTime = System.nanoTime();
        while(image == RelicRecoveryVuMark.UNKNOWN&&System.nanoTime()-lastTime<=2000000000){
            image = imageProvider.getImageReading();
            sleep(50);
        }
        telemetry.addData("Image", image);
        telemetry.update();
        robot.knockOverJewel(Color.BLUE);//Check color sensor
        robot.getIntakeTop().setPower(-1);
        robot.getIntakeBottom().setPower(-1);
        robot.driveToInch(.3, 30);//Drive off balancing stone
        robot.getSpring().setPosition(0.5);//Spring out glyph
        sleep(350);
        robot.getIntakeServo().setPosition(0.75);
        robot.getIntakeLift().setPower(1);
        while (robot.getIntakeLift().getCurrentPosition()<=200){}
        robot.getIntakeLift().setPower(0.1);
        gyroscopePID.rotate(-75);//Rotate to face glyph
        gyroscopePID.driveWithColorSensor(0.6, 28, robot.getDriveColor(), ColorSensorTerminator.Color.RED);
        robot.getIntakeServo().setPosition(0.2);
        robot.getIntakeLift().setPower(-1);
        while (robot.getIntakeLift().getCurrentPosition()>=100){}
        robot.getIntakeLift().setPower(0.1);
        sleep(250);
        robot.driveIntakeVertical(0.5);
        sleep(100);
        robot.driveIntakeVertical(0);
        gyroscopePID.drive(-0.5, 1);
        switch (image){
            case LEFT:
                gyroscopePID.rotate(-151); //-150
                gyroscopePID.drive(0.8, 41);
                gyroscopePID.drive(0.5, 4);
                break;
            case RIGHT:
                gyroscopePID.rotate(-168); //-170
                gyroscopePID.drive(0.8, 36);
                gyroscopePID.drive(0.5, 4);
                break;
            default:
                gyroscopePID.rotate(-159); //-161
                gyroscopePID.drive(0.8, 40);
                gyroscopePID.drive(0.5, 4);
        }
        outtake();
        robot.getIntakePusher().setPosition(0.5);
        gyroscopePID.drive(-1, 6);
    }

}