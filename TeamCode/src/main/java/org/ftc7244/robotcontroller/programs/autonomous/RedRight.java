package org.ftc7244.robotcontroller.programs.autonomous;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.ftc7244.datalogger.Logger;
import org.ftc7244.robotcontroller.autonomous.ControlSystemAutonomous;
import org.ftc7244.robotcontroller.autonomous.terminators.ColorSensorTerminator;
import org.ftc7244.robotcontroller.hardware.Westcoast;


/**
 * Created by Eeshwar Laptop on 10/29/2017.
 */
@Autonomous(name = "Red Right")
public class RedRight extends ControlSystemAutonomous {

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
        robot.driveToInch(.3, 38);//Drive off balancing stone
        robot.getSpring().setPosition(0.5);//Spring out glyph
        robot.getIntakeLift().setPower(1);
        while (robot.getIntakeLift().getCurrentPosition()<=400){}
        robot.getIntakeServo().setPosition(0.8);
        robot.getIntakeLift().setPower(0.1);
        sleep(150);//Let block come into intake
        gyroscopePID.rotate(50);
        robot.getIntakeBottom().setPower(-1);
        gyroscopePID.driveWithColorSensor(0.6, 20, robot.getDriveColor(), ColorSensorTerminator.Color.RED);
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
        gyroscopePID.drive(-1, 3);
        switch(image){
            case LEFT:
                gyroscopePID.rotate(-110); //-108
                gyroscopePID.drive(0.5, 38);
                gyroscopePID.drive(.3,4);
                break;
            case RIGHT:
                gyroscopePID.rotate(-125);//-124
                gyroscopePID.drive(0.5, 45);
                gyroscopePID.drive(.3,4);
                break;
            default:
                gyroscopePID.rotate(-118);//-115
                gyroscopePID.drive(0.5, 40);
                gyroscopePID.drive(.3, 6);
        }
        outtake();
        gyroscopePID.drive(-0.6, 30);
        robot.getIntakePusher().setPosition(0.5);
        gyroscopePID.rotate(60);
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
        if(image.equals(RelicRecoveryVuMark.LEFT))
            robot.driveIntakeVertical(0.5);
        gyroscopePID.driveWithColorSensor(-0.6, 30, robot.getDriveColor(), ColorSensorTerminator.Color.RED);
        switch(image){
            case LEFT:
                gyroscopePID.rotate(-126);
                gyroscopePID.drive(1, 36);
                break;
            case RIGHT:
                gyroscopePID.rotate(-110);
                gyroscopePID.drive(0.5, 44);
                break;
            default:
                gyroscopePID.rotate(-110);
                gyroscopePID.drive(0.5, 42);
                break;
        }
        robot.driveIntakeVertical(0);
        robot.getIntakeServo().setPosition(0.8);
        outtake();
        robot.getIntakeServo().setPosition(0.1);
        robot.getIntakePusher().setPosition(0.5);
    }
}