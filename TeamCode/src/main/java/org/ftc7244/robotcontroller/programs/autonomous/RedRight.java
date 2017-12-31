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
        robot.getIntakeServo().setPosition(0);
        //robot.knockOverJewel(Color.BLUE);//Check color sensor
        robot.driveToInch(.2, 28);//Drive off balancing stone
        gyroscope.rotate(-gyroProvider.getZ());//Re-Center the robot
        robot.getSpring().setPosition(1);//Spring out glyph
        RelicRecoveryVuMark image = imageProvider.getImageReading();
        telemetry.addData("Image", image);
        telemetry.update();
        sleep(1000);
        gyroscope.rotate(45);//Rotate to face glyph pit
        robot.driveIntakeVertical(0.5);
        sleep(300);
        robot.driveIntakeVertical(0);
        gyroscope.drive(0.4, 11);//Drive to glyph pit
        robot.getIntakeBottom().setPower(-1);//activate Intake
        gyroscope.drive(0.3, 12);// Drive into glyph pit
        robot.getIntakeServo().setPosition(0);
        sleep(1500);
        gyroscope.drive(-0.3, 10);
        switch(image){
            case LEFT:
                gyroscope.rotate(-153.5);
                robot.driveIntakeVertical(0.5);
                sleep(100);
                robot.driveIntakeVertical(0);
                robot.getIntakeBottom().setPower(0);//disable outtake
                gyroscope.drive(0.5, 34);
                robot.getIntakeBottom().setPower(1);
                robot.getIntakeTop().setPower(1);
                sleep(1500);
                gyroscope.drive(0.4, 8);
                gyroscope.drive(-0.4, 8);
                break;
            case CENTER:
                gyroscope.rotate(-158);
                robot.driveIntakeVertical(0.5);
                sleep(100);
                robot.driveIntakeVertical(0);
                robot.getIntakeBottom().setPower(0);//disable outtake
                gyroscope.drive(0.5, 38);
                robot.getIntakeBottom().setPower(1);
                robot.getIntakeTop().setPower(1);
                sleep(1500);
                gyroscope.drive(0.4, 8);
                gyroscope.drive(-0.4, 8);
                break;
            case RIGHT:
                gyroscope.rotate(-166);
                robot.driveIntakeVertical(0.5);
                sleep(100);
                robot.driveIntakeVertical(0);
                robot.getIntakeBottom().setPower(0);//disable outtake
                gyroscope.drive(0.5, 31);
                robot.getIntakeBottom().setPower(1);
                robot.getIntakeTop().setPower(1);
                sleep(1500);
                gyroscope.drive(0.4, 8);
                gyroscope.drive(-0.4, 8);
                break;
            default:
                gyroscope.rotate(180);
                break;
        }
        robot.getIntakeBottom().setPower(1);
        gyroscope.drive(-0.2, 2);
        robot.getIntakeBottom().setPower(0);
    }
}