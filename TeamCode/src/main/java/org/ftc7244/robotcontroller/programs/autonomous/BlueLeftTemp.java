package org.ftc7244.robotcontroller.programs.autonomous;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.ftc7244.robotcontroller.autonomous.ControlSystemAutonomous;
import org.ftc7244.robotcontroller.autonomous.terminators.ColorSensorTerminator;

/**
 * Created by ftc72 on 4/15/2018.
 */
@Autonomous(name = "Blue Left")
public class BlueLeftTemp extends ControlSystemAutonomous{
    @Override
    public void run() {
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
        robot.getIntakeServo().setPosition(0.75);
        robot.getIntakeLift().setPower(0.1);
        sleep(150);//Let block come into intake
        gyroscopePID.rotate(-50);
        robot.getIntakeBottom().setPower(-1);
        gyroscopePID.driveWithColorSensor(0.6, 20, robot.getDriveColor(), ColorSensorTerminator.Color.BLUE);
        robot.getIntakeServo().setPosition(0.2);
        sleep(250);
        robot.getIntakeLift().setPower(1);
        while (robot.getIntakeLift().getCurrentPosition()<=500){}
        robot.getIntakeLift().setPower(0.1);
        sleep(100);
        robot.driveIntakeVertical(0);
        gyroscopePID.drive(-1, 8);
        double degrees = -70;
        switch(image){
            case LEFT:
                gyroscopePID.rotate(123); //-108
                gyroscopePID.drive(0.8, 42);
                gyroscopePID.drive(.3,4);
                degrees = -65;
                break;
            case RIGHT:
                gyroscopePID.rotate(110);//-124
                gyroscopePID.drive(0.8, 32);
                gyroscopePID.drive(.3,4);
                degrees = -75;
                break;
            default:
                gyroscopePID.rotate(117);//-115
                gyroscopePID.drive(0.8, 35);
                gyroscopePID.drive(.3, 4);
        }
        outtake();
        gyroscopePID.drive(-0.6, 5);
        robot.getIntakePusher().setPosition(0.5);
        gyroscopePID.rotate(degrees);
        robot.getIntakeTop().setPower(-1);
        robot.getIntakeBottom().setPower(-1);
        robot.getIntakeLift().setPower(1);
        while (robot.getIntakeLift().getCurrentPosition()<=400){}
        robot.getIntakeLift().setPower(0.1);
        robot.getIntakeServo().setPosition(0.8);
        gyroscopePID.drive(0.8, 60);
        robot.getIntakeServo().setPosition(0.2);
        if(image.equals(RelicRecoveryVuMark.RIGHT)){
            robot.getIntakeLift().setPower(-1);
            sleep(100);
            robot.getIntakeLift().setPower(.1);
            robot.driveIntakeVertical(0.5);
        }
        sleep(550);
        gyroscopePID.driveWithColorSensor(-0.6, 30, robot.getDriveColor(), ColorSensorTerminator.Color.BLUE);
        switch(image){
            case LEFT:
                gyroscopePID.rotate(107);
                gyroscopePID.drive(0.8, 45);
                break;
            case RIGHT:
                gyroscopePID.rotate(118);
                gyroscopePID.drive(1, 30.5);
                break;
            default:
                gyroscopePID.rotate(107.5);
                gyroscopePID.drive(0.8, 45.5);
                break;
        }
        robot.driveIntakeVertical(0);
        robot.getIntakeServo().setPosition(0.8);
        outtake();
        robot.getIntakePusher().setPosition(0.5);
        gyroscopePID.drive(-0.3, 1);
    }
}
