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
        while (robot.getIntakeLift().getCurrentPosition()<=400){}
        robot.getIntakeLift().setPower(0.1);
        gyroscopePID.rotate(-75);//Rotate to face glyph
        gyroscopePID.driveWithColorSensor(0.6, 28, robot.getDriveColor(), ColorSensorTerminator.Color.RED);
        robot.getIntakeServo().setPosition(0.2);
        robot.getIntakeLift().setPower(1);
        while(robot.getIntakeLift().getCurrentPosition()<=500){}
        robot.getIntakeLift().setPower(0.1);
        sleep(250);
        gyroscopePID.drive(-1, 1);
        switch (image){
            case LEFT:
                gyroscopePID.rotate(-154); //-150
                gyroscopePID.drive(1, 39);
                gyroscopePID.drive(0.5, 4);
                break;
            case RIGHT:
                gyroscopePID.rotate(-171); //-170
                gyroscopePID.drive(1, 36);
                gyroscopePID.drive(0.5, 4);
                break;
            default:
                gyroscopePID.rotate(-161); //-161
                gyroscopePID.drive(1, 37);
                gyroscopePID.drive(0.5, 4);
        }
        outtake();
        robot.getIntakePusher().setPosition(0.5);
        gyroscopePID.drive(-1, 6);
        switch (image){
            case LEFT:
                gyroscopePID.rotate(0);
                break;
            case RIGHT:
                gyroscopePID.rotate(-10);
                break;
            default:
                gyroscopePID.rotate(-5);

        }
        robot.getIntakeLift().setPower(-1);
        while (robot.getIntakeLift().getCurrentPosition()>=400){}
        robot.getIntakeLift().setPower(0.1);
        robot.getIntakeServo().setPosition(0.75);
        robot.getIntakeTop().setPower(-1);
        robot.getIntakeBottom().setPower(-1);
        gyroscopePID.driveWithColorSensor(0.6, 45, robot.getDriveColor(), ColorSensorTerminator.Color.RED);
        robot.getIntakeServo().setPosition(0.2);
        sleep(500);
        robot.getIntakeLift().setPower(-1);
        while (robot.getIntakeLift().getCurrentPosition()>=0){}
        robot.getIntakeLift().setPower(0.1);
        robot.getIntakeLift().setPower(1);
        while (robot.getIntakeLift().getCurrentPosition()<=400){}
        robot.getIntakeLift().setPower(0.1);
        robot.getIntakeServo().setPosition(0.2);
        robot.driveIntakeVertical(0.5);
        gyroscopePID.drive(-1, 3);
        sleep(1000);
        robot.driveIntakeVertical(0);
        robot.getIntakeServo().setPosition(0.75);
        gyroscopePID.drive(1, 20);
        sleep(500);
        robot.getIntakeServo().setPosition(0.2);
        gyroscopePID.driveWithColorSensor(-0.6, 30, robot.getDriveColor(), ColorSensorTerminator.Color.RED);
        switch (image) {
            case LEFT:
                gyroscopePID.rotate(170);
                break;
            case RIGHT:
                gyroscopePID.rotate(-157);
                break;
            default:
                gyroscopePID.rotate(175);
        }
        gyroscopePID.drive(1, 26);
        outtake();
        robot.getIntakePusher().setPosition(0.5);
        gyroscopePID.drive(-1, 2);
    }
}