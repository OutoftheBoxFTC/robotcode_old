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
        sleep(750);
        robot.getIntakeServo().setPosition(0.8);
        gyroscopePID.rotate(-90);//Rotate to face glyph
        gyroscopePID.drive(1, 28);
        robot.getIntakeServo().setPosition(0.2);
        sleep(1000);
        robot.getIntakeLift().setPower(1);
        sleep(100);
        robot.getIntakeLift().setPower(0.1);
        sleep(150);//Let block come into intake
        gyroscopePID.drive(-0.75, 1);
        int relicExtend = 1000, degrees=10;
        switch (image){
            case LEFT:
                gyroscopePID.rotate(-60); //-150
                gyroscopePID.drive(0.5, 45);
                break;
            case RIGHT:
                gyroscopePID.rotate(-80); //-170
                gyroscopePID.drive(0.5, 34);
                relicExtend = 1200;
                degrees=15;
                break;
            default:
                gyroscopePID.rotate(-71); //-161
                gyroscopePID.drive(0.5, 40);
                relicExtend = 1200;
        }
        robot.getIntakeBottom().setPower(1);
        robot.getIntakeTop().setPower(1);
        sleep(1000);
        gyroscopePID.drive(-0.3, 11);
        gyroscopePID.drive(0.5, 9);
        gyroscopePID.drive(-1, 13);
        gyroscopePID.rotate(170); //9
        robot.getIntakeServo().setPosition(0.8);
        robot.getIntakeTop().setPower(-1);
        robot.getIntakeBottom().setPower(-1);
        gyroscopePID.drive(0.5, 30);
        robot.getIntakeServo().setPosition(0.2);
        sleep(500);
        robot.getIntakeLift().setPower(-1);
        sleep(100);
        robot.getIntakeLift().setPower(0);
        gyroscopePID.rotate(degrees); //19
        robot.getRelicSpool().setPower(-1);
        while (robot.getRelicSpool().getCurrentPosition()>-relicExtend);
        robot.getRelicSpool().setPower(0);
    }
}