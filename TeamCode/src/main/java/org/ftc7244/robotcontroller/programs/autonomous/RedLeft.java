package org.ftc7244.robotcontroller.programs.autonomous;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.ftc7244.robotcontroller.autonomous.PIDAutonomous;


/**
 * Created by Eeshwar Laptop on 10/29/2017.
 */
@Autonomous(name = "Red Left")
public class RedLeft extends PIDAutonomous {

    public void run() throws InterruptedException{
        robot.knockOverJewel(Color.BLUE);//Check color sensor
        robot.driveToInch(0.2, 12);
        sleep(1000);//Wait for gyro to calibrate
        gyroscope.rotate(-90);//Rotate
        gyroscope.drive(0.2, 24);//Drive to glyph box
        robot.getSpring().setPosition(.5);//Spring out glyph
        robot.getIntakeBottom().setPower(1);//activate outtake
        gyroscope.drive(-0.2, 3);// Drive glyph into intake
        robot.getIntakeBottom().setPower(0);//disable outtake
        gyroscope.rotate(-180);//Rotate so back faces glyph box
        gyroscope.drive(-0.4, 3.5);//Drive glyph back into glyph box
        gyroscope.drive(0.4,2);//Drive foreword
        robot.getJewelVerticle().setPosition(.15);//Land jewel arm into parking zone
        sleep(750);//wait for deceleration
    }
}