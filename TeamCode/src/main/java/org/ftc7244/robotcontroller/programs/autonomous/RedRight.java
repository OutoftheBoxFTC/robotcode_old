package org.ftc7244.robotcontroller.programs.autonomous;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.ftc7244.datalogger.Logger;
import org.ftc7244.robotcontroller.autonomous.ControlSystemAutonomous;


/**
 * Created by Eeshwar Laptop on 10/29/2017.
 */
@Autonomous(name = "Red Right")
public class RedRight extends ControlSystemAutonomous {

    public void run() throws InterruptedException{
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
        robot.driveToInch(.3, 34.5);//Drive off balancing stone
        robot.getSpring().setPosition(0.5);//Spring out glyph
        robot.getIntakeLift().setPower(1);
        sleep(100);
        robot.getIntakeLift().setPower(0.1);
        sleep(150);//Let block come into intake
        robot.getIntakeServo().setPosition(0.8);
        gyroscopePID.rotate(45);
        robot.getIntakeBottom().setPower(-1);
        gyroscopePID.drive(1, 17);//Drive to glyph pit
        //0.2 : 0.75
        robot.getIntakeServo().setPosition(0.2);
        sleep(500);
        robot.getIntakeLift().setPower(-1);
        sleep(100);
        robot.getIntakeLift().setPower(1);
        sleep(100);
        robot.getIntakeLift().setPower(0.1);
        gyroscopePID.drive(-1, 4);
        int degrees = 0, distance = 10;
        switch(image){
            case LEFT:
                gyroscopePID.rotate(-106); //-108
                gyroscopePID.drive(1, 36);
                gyroscopePID.drive(.6,4);
                degrees = 0;
                break;
            case RIGHT:
                gyroscopePID.rotate(-124);//-124
                gyroscopePID.drive(1, 44);
                gyroscopePID.drive(.6,4);
                degrees=10;
                break;
            default:
                gyroscopePID.rotate(-115);//-115
                gyroscopePID.drive(1, 40);
                gyroscopePID.drive(.6, 4);
                degrees=0;
                distance=2;
        }
        robot.getIntakeBottom().setPower(1);
        robot.getIntakeTop().setPower(1);
        sleep(1000);
        gyroscopePID.drive(-0.3, 5);
        gyroscopePID.drive(0.3, 5);
        gyroscopePID.drive(-0.5, 28);
        gyroscopePID.rotate(70); //10
        robot.getIntakeServo().setPosition(0.8);
        robot.getIntakeTop().setPower(-1);
        robot.getIntakeBottom().setPower(-1);
        gyroscopePID.drive(0.5, 20);
        robot.getIntakeServo().setPosition(0.2);
        sleep(500);
        robot.getIntakeLift().setPower(-1);
        sleep(100);
        robot.getIntakeLift().setPower(0);
        robot.getRelicSpool().setPower(-1);
        while (robot.getRelicSpool().getCurrentPosition()>-1700);
        robot.getRelicSpool().setPower(0);
        switch (image){
            case LEFT:
                break;
            case RIGHT:
                break;
            default:
                break;
        }
    }
}