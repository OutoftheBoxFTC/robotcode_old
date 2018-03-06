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
        while(image == RelicRecoveryVuMark.UNKNOWN){
            image = imageProvider.getImageReading();
            sleep(50);
        }
        robot.knockOverJewel(Color.BLUE);//Check color sensor
        robot.getIntakeTop().setPower(-1);
        robot.driveToInch(.3, 36);//Drive off balancing stone
        robot.getSpring().setPosition(0.5);//Spring out glyph
        sleep(750);
        robot.getIntakeServo().setPosition(0.8);
        gyroscopePID.rotate(45);
        robot.getIntakeBottom().setPower(-1);
        gyroscopePID.drive(1, 23);//Drive to glyph pit
        //0.2 : 0.75
        robot.getIntakeServo().setPosition(0.2);
        robot.getIntakeLift().setPower(1);
        sleep(100);
        robot.getIntakeLift().setPower(0.1);
        sleep(150);//Let block come into intake
        gyroscopePID.drive(-0.75, 10);
        switch(image){
            case LEFT:
                gyroscopePID.rotate(-154);
                gyroscopePID.drive(0.5, 51 );
                break;
            case RIGHT:
                gyroscopePID.rotate(-171.5);
                gyroscopePID.drive(1, 48);
                break;
            default:
                gyroscopePID.rotate(-161);
                gyroscopePID.drive(1, 45);
        }
        robot.getIntakeBottom().setPower(1);
        robot.getIntakeTop().setPower(1);
        gyroscopePID.drive(-0.5, 8);
        gyroscopePID.drive(1, 6);
        gyroscopePID.drive(-1, 58);
        gyroscopePID.rotate(180);
        robot.getIntakeTop().setPower(0);
        robot.getIntakeBottom().setPower(0);
        //robot.getRelicSpool().setPower(-1);
        /*while (robot.getRelicSpool().getCurrentPosition()<1600);
        robot.getRelicSpool().setPower(0);*/
    }
}