package org.ftc7244.robotcontroller.programs.autonomous;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.ftc7244.robotcontroller.autonomous.PIDAutonomous;


/**
 * Created by Eeshwar Laptop on 10/29/2017.
 */
@Autonomous(name = "Red Right")
public class RedRight extends PIDAutonomous {

    public void run() throws InterruptedException{
        RelicRecoveryVuMark image = imageProvider.getImageReading();
        while(image == RelicRecoveryVuMark.UNKNOWN){
            image = imageProvider.getImageReading();
            sleep(50);
        }
        robot.knockOverJewel(Color.BLUE);//Check color sensor
        robot.getIntakeTop().setPower(-1);
        long lastTime = System.currentTimeMillis();
        robot.driveToInch(.3, 36);//Drive off balancing stone
        telemetry.addLine("Drive Off Balancing Stone " + (System.currentTimeMillis()-lastTime));
        telemetry.update();
        robot.getSpring().setPosition(0.5);//Spring out glyph
        sleep(750);
        robot.getIntakeServo().setPosition(0.8);
        lastTime = System.currentTimeMillis();
        gyroscope.rotate(45);//Rotate to face glyph pit
        telemetry.addLine("Turn towards glyph pit " + (System.currentTimeMillis()-lastTime));
        telemetry.update();
        robot.getIntakeBottom().setPower(-1);
        lastTime = System.currentTimeMillis();
        gyroscope.drive(1, 23);//Drive to glyph pit
        telemetry.addLine("Drive Into Glyph Pit " + (System.currentTimeMillis()-lastTime));
        telemetry.update();
        //0.2 : 0.75
        robot.getIntakeServo().setPosition(0.2);
        sleep(750);
        lastTime = System.currentTimeMillis();
        gyroscope.drive(-0.75, 10);
        telemetry.addLine("Drive Out Of Glyph Pit " + (System.currentTimeMillis()-lastTime));
        telemetry.update();
        switch(image){
            case LEFT:
                lastTime = System.currentTimeMillis();
                gyroscope.rotate(-154);
                telemetry.addLine("Turn Towards Left Column " + (System.currentTimeMillis()-lastTime));
                telemetry.update();
                lastTime = System.currentTimeMillis();
                gyroscope.drive(1, 46);
                telemetry.addLine("Drive To Left Column " + (System.currentTimeMillis()-lastTime));
                telemetry.update();
                break;
            case RIGHT:
                gyroscope.rotate(-171.5);
                gyroscope.drive(1, 48);
                break;
            default:
                gyroscope.rotate(-161);
                gyroscope.drive(1, 45);
        }
        robot.getIntakeBottom().setPower(1);
        robot.getIntakeTop().setPower(1);
        lastTime = System.currentTimeMillis();
        gyroscope.drive(-1, 8);
        telemetry.addLine("Drive Back " + (System.currentTimeMillis()-lastTime));
        telemetry.update();
        lastTime = System.currentTimeMillis();
        gyroscope.drive(1, 6);
        telemetry.addLine("Drive Forward " + (System.currentTimeMillis()-lastTime));
        telemetry.update();
        lastTime = System.currentTimeMillis();
        gyroscope.drive(-1, 15);
        telemetry.addLine("Drive Back " + (System.currentTimeMillis()-lastTime));
        telemetry.update();
        lastTime = System.currentTimeMillis();
        gyroscope.rotate(180);
        telemetry.addLine("Rotate Out " + (System.currentTimeMillis()-lastTime));
        telemetry.update();
        robot.getJewelVertical().setPosition(0.26);
    }
}