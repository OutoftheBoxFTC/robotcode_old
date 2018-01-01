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
        robot.knockOverJewel(Color.BLUE);//Check color sensor
        robot.getIntakeTop().setPower(-1);
        robot.driveToInch(.2, 28);//Drive off balancing stone
        gyroscope.rotate(-gyroProvider.getZ());//Re-Center the robot
        sleep(200);
        robot.getSpring().setPosition(0.6);//Spring out glyph
        sleep(200);
        RelicRecoveryVuMark image = imageProvider.getImageReading();
        telemetry.addData("Image", image);
        telemetry.update();
        sleep(1000);
        robot.getIntakeServo().setPosition(0.45);
        gyroscope.rotate(45);//Rotate to face glyph pit
        robot.getIntakeBottom().setPower(-1);
        gyroscope.drive(0.4, 11);//Drive to glyph pit
        gyroscope.drive(0.3, 12);// Drive into glyph pit
        robot.getIntakeServo().setPosition(0.7);
        sleep(1500);
        gyroscope.drive(-0.3, 10);
        switch(image){
            case LEFT:
                gyroscope.rotate(-153.1);
                robot.getIntakeBottom().setPower(0);//disable outtake
                gyroscope.drive(0.5, 34);

                break;
            case RIGHT:
                gyroscope.rotate(-166);
                robot.getIntakeBottom().setPower(0);//disable outtake
                gyroscope.drive(0.5, 29);
                break;
            case CENTER:
            default:
                gyroscope.rotate(-158);
                robot.getIntakeBottom().setPower(0);//disable outtake
                gyroscope.drive(0.5, 36);
        }
        robot.getIntakeBottom().setPower(1);
        robot.getIntakeTop().setPower(1);
        gyroscope.drive(-0.2, 8);
        gyroscope.drive(0.5, 6);
        gyroscope.drive(-0.5, 5);
        robot.getIntakeBottom().setPower(0);
        robot.getIntakeTop().setPower(0);
    }
}