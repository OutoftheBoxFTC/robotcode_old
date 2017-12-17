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
        robot.driveToInch(.2, 30);//Drive off balancing stone
        gyroscope.rotate(-gyroProvider.getZ());//Re-Center the robot
        robot.getIntakeServo().setPosition(0.5);
        robot.getSpring().setPosition(.5);//Spring out glyph
        RelicRecoveryVuMark image = imageProvider.getImageReading();
        telemetry.addData("Image", image);
        telemetry.update();
        sleep(1000);
        gyroscope.rotate(45);//Rotate
        robot.driveintakeVertical(0.5);
        sleep(1000);
        robot.driveintakeVertical(0);
        gyroscope.drive(0.4, 11);//Drive to glyph pit
        robot.getIntakeBottom().setPower(-1);//activate Intake
        gyroscope.drive(0.3, 15);// Drive into glyph pit
        robot.getIntakeServo().setPosition(0);
        sleep(3000);
        gyroscope.drive(-0.3, 10);
        robot.getIntakeBottom().setPower(0);//disable outtake
        gyroscope.rotate(-160);//Rotate so back faces glyph pit
        robot.driveintakeVertical(0.5);
        sleep(100);
        robot.driveintakeVertical(0);
        gyroscope.drive(0.5, 38);//Drive glyph into the glyph box
        gyroscope.rotate(-65);
        double driveDistance = 0;
        switch(image){
            case LEFT:
                gyroscope.rotate(80);
                driveDistance = 0.3;
                break;
            case CENTER:
                gyroscope.rotate(90);
                driveDistance = 0.4;
                break;
            case RIGHT:
                gyroscope.rotate(100);
                driveDistance = 0.5;
                break;
            default:
                gyroscope.rotate(180);
                break;
        }
        gyroscope.drive(driveDistance, 2);
        robot.getIntakeBottom().setPower(1);
        gyroscope.drive(-0.2, 2);
        robot.getIntakeBottom().setPower(0);
        /*robot.getIntakeBottom().setPower(1);
        gyroscope.drive(-0.4, 2);//Drive foreword
        robot.getIntakeBottom().setPower(0);
        gyroscope.rotate(170);
        gyroscope.drive(0.4, 4);
        robot.getJewelVerticle().setPosition(.15);//Land jewel arm into parking zone
        sleep(750);//wait for deceleration
    */}
}