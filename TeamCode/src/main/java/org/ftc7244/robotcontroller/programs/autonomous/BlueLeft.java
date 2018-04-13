package org.ftc7244.robotcontroller.programs.autonomous;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.ftc7244.robotcontroller.autonomous.ControlSystemAutonomous;
import org.ftc7244.robotcontroller.autonomous.terminators.ColorSensorTerminator;


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
        gyroscopePID.driveWithColorSensor(0.6, 20, robot.getDriveColor(), ColorSensorTerminator.Color.RED);        //0.2 : 0.75
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
                gyroscopePID.rotate(111.5); //-108
                gyroscopePID.drive(0.5, 38);
                gyroscopePID.drive(.3,1);
                break;
            case LEFT:
                gyroscopePID.rotate(123);//-124
                gyroscopePID.drive(0.5, 51);
                gyroscopePID.drive(.3,1);
                break;
            default:
                gyroscopePID.rotate(118);//-115
                gyroscopePID.drive(0.5, 47);
                gyroscopePID.drive(.3, 1);
        }
        outtake();
        gyroscopePID.drive(-0.6, 30);
        robot.getIntakePusher().setPosition(0.5);
        gyroscopePID.rotate(-65);
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
        gyroscopePID.driveWithColorSensor(-0.6, 20, robot.getDriveColor(), ColorSensorTerminator.Color.BLUE);
        switch(image){
            case RIGHT:
                gyroscopePID.rotate(120);
                gyroscopePID.drive(1, 30.5);
                break;
            case LEFT:
                gyroscopePID.rotate(109);
                gyroscopePID.drive(0.5, 45);
                break;
            default:
                gyroscopePID.rotate(110.5);
                gyroscopePID.drive(0.5, 44.5);
        }
        robot.driveIntakeVertical(0);
        robot.getIntakeServo().setPosition(0.8);
        outtake();
        robot.getIntakeServo().setPosition(0.1);
        robot.getIntakePusher().setPosition(0.5);
    }
}