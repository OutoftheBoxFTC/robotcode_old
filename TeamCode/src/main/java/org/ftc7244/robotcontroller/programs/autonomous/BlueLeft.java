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
        RelicRecoveryVuMark image = imageProvider.getImageReading();
        long lastTime = System.nanoTime();
        while(image == RelicRecoveryVuMark.UNKNOWN&&System.nanoTime()-lastTime<=2000000000){
            image = imageProvider.getImageReading();
            sleep(50);
        }
        robot.knockOverJewel(Color.RED);//Check color sensor
        robot.getIntakeTop().setPower(-1);

        robot.driveToInch(.3, 36);//Drive off balancing stone
        robot.getSpring().setPosition(0.5);//Spring out
        robot.getIntakeLift().setPower(1);
        sleep(100);
        robot.getIntakeLift().setPower(0.1);
        sleep(150);
        robot.getIntakeServo().setPosition(0.8);
        gyroscopePID.rotate(-45);
        robot.getIntakeBottom().setPower(-1);
        gyroscopePID.drive(1, 23);//Drive to glyph pit
        //0.2 : 0.75
        robot.getIntakeServo().setPosition(0.2);
        sleep(1000);
        robot.getIntakeLift().setPower(1);
        sleep(100);
        robot.getIntakeLift().setPower(0.1);
        sleep(150);//Let block come into intake
        gyroscopePID.drive(-0.75, 15);
        int degrees = 200, d2=0;
        switch(image){
            case LEFT:
                degrees=180;
                gyroscopePID.rotate(170);
                gyroscopePID.drive(0.5, 46);
                gyroscopePID.drive(.6, 4);
                break;
            case RIGHT:
                degrees = 190;
                d2=5;
                gyroscopePID.rotate(153.1);
                gyroscopePID.drive(0.5, 43);
                gyroscopePID.drive(.6, 4);
                break;
            default:
                degrees=180;
                gyroscopePID.rotate(159.5);
                gyroscopePID.drive(0.5, 44);
                gyroscopePID.drive(.6, 4);
        }
        robot.getIntakeBottom().setPower(1);
        robot.getIntakeTop().setPower(1);
        sleep(1000);
        gyroscopePID.drive(-0.3, 5);
        gyroscopePID.drive(0.3, 5);
        gyroscopePID.drive(-0.5, 28);
        gyroscopePID.rotate(degrees);
        robot.getIntakeServo().setPosition(0.8);
        robot.getIntakeTop().setPower(-1);
        robot.getIntakeBottom().setPower(-1);
        gyroscopePID.drive(0.5, 25);
        robot.getIntakeServo().setPosition(0.2);
        sleep(500);
        robot.getIntakeLift().setPower(-1);
        sleep(100);
        robot.getIntakeLift().setPower(0);
        gyroscopePID.rotate(d2);
        robot.getRelicSpool().setPower(-1);
        while (robot.getRelicSpool().getCurrentPosition()>-1500);
        robot.getRelicSpool().setPower(0);
        gyroscopePID.drive(-0.5, 2);
    }
}