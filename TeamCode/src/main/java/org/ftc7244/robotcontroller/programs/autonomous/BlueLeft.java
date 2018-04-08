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

    public void run(){
        RelicRecoveryVuMark image = imageProvider.getImageReading();
        long lastTime = System.nanoTime();
        while(image == RelicRecoveryVuMark.UNKNOWN&&System.nanoTime()-lastTime<=2000000000){
            image = imageProvider.getImageReading();
            sleep(50);
        }

        telemetry.addData("Image", image);
        telemetry.update();
        robot.knockOverJewel(Color.RED);//Check color sensor
        robot.getIntakeTop().setPower(-1);
        robot.driveToInch(.3, 38);//Drive off balancing stone
        robot.getSpring().setPosition(0.5);//Spring out glyph
        robot.getIntakeLift().setPower(1);
        while (robot.getIntakeLift().getCurrentPosition()<=400){}
        robot.getIntakeServo().setPosition(0.8);
        robot.getIntakeLift().setPower(0.1);
        sleep(150);//Let block come into intake
        gyroscopePID.rotate(-50);
        robot.getIntakeBottom().setPower(-1);
        gyroscopePID.drive(1, 10);//Drive to glyph pit
        //0.2 : 0.75
        robot.getIntakeServo().setPosition(0.2);
        sleep(250);
        robot.getIntakeLift().setPower(-1);
        while (robot.getIntakeLift().getCurrentPosition()>=100){}
        robot.getIntakeLift().setPower(0.1);
        robot.getIntakeLift().setPower(1);
        while (robot.getIntakeLift().getCurrentPosition()<=200){}
        robot.getIntakeLift().setPower(0.1);
        robot.driveIntakeVertical(0.5);
        sleep(100);
        robot.driveIntakeVertical(0);
        gyroscopePID.drive(-1, 8);
        switch(image){
            case RIGHT:
                gyroscopePID.rotate(105.5); //-108
                gyroscopePID.drive(0.5, 32);
                gyroscopePID.drive(.3,1);
                break;
            case LEFT:
                gyroscopePID.rotate(123.5);//-124
                gyroscopePID.drive(0.5, 40);
                gyroscopePID.drive(.3,1);
                break;
            default:
                gyroscopePID.rotate(117);//-115
                gyroscopePID.drive(0.5, 33);
                gyroscopePID.drive(.3, 1);
        }
        outtake();
        gyroscopePID.drive(-0.6, 30);
        robot.getIntakePusher().setPosition(0.5);
        gyroscopePID.rotate(-60);
        robot.getIntakeTop().setPower(-1);
        robot.getIntakeBottom().setPower(-1);
        robot.getIntakeLift().setPower(1);
        while (robot.getIntakeLift().getCurrentPosition()<=400){}
        robot.getIntakeLift().setPower(0.1);
        robot.getIntakeServo().setPosition(0.8);
        gyroscopePID.drive(0.6, 30);
        robot.getIntakeServo().setPosition(0.2);
        sleep(500);
        robot.getIntakeLift().setPower(-1);
        while (robot.getIntakeLift().getCurrentPosition()>=100){}
        robot.getIntakeLift().setPower(1);
        while (robot.getIntakeLift().getCurrentPosition()<=300){}
        robot.getIntakeLift().setPower(0.1);
        robot.driveIntakeVertical(0.5);
        sleep(50);
        robot.driveIntakeVertical(0);
        if(image.equals(RelicRecoveryVuMark.RIGHT))
            robot.driveIntakeVertical(0.5);
        switch(image){
            case RIGHT:
                gyroscopePID.drive(-0.8, 20);
                gyroscopePID.rotate(120);
                gyroscopePID.drive(1, 23);
                break;
            case LEFT:
                gyroscopePID.drive(-0.8, 5);
                gyroscopePID.rotate(106);
                gyroscopePID.drive(0.5, 38);
                break;
            default:
                gyroscopePID.drive(-0.8, 20);
                gyroscopePID.rotate(104);
                gyroscopePID.drive(0.5, 37);
        }
        robot.driveIntakeVertical(0);
        robot.getIntakeServo().setPosition(0.8);
        outtake();
        gyroscopePID.drive(-0.5, 7);
        robot.getIntakePusher().setPosition(0.5);
    }
}