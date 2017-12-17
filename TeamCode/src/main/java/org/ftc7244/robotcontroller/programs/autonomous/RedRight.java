package org.ftc7244.robotcontroller.programs.autonomous;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.ftc7244.robotcontroller.autonomous.PIDAutonomous;


/**
 * Created by Eeshwar Laptop on 10/29/2017.
 */
@Autonomous(name = "Red Right")
public class RedRight extends PIDAutonomous {

    public void run() throws InterruptedException{
        robot.knockOverJewel(Color.BLUE);//Check color sensor
        robot.driveToInch(.25, 25);//Drive off balancing stone
        gyroscope.rotate(-gyroProvider.getZ());//Re-Center the robot
        gyroscope.rotate(45);//Rotate
        robot.getSpring().setPosition(.5);//Spring out glyph
        robot.driveintakeVertical(.5);
        sleep(750);
        robot.driveintakeVertical(0);

        robot.jiggleBottomVertical(250, 5000);
        robot.getIntakeBottom().setPower(-1);
        robot.getIntakeServo().setPosition(0.5);//activate intake
        gyroscope.drive(0.4, 15);//Drive to glyph pit
        robot.getIntakeServo().setPosition(0);//Close intake

        gyroscope.drive(-0.4, 5);
        gyroscope.rotate(160);//Rotate so back faces glyph pit
        robot.getIntakeBottom().setPower(0);//disable intake
        gyroscope.drive(0.5, 50);//Drive glyph into the glyph box
        robot.getIntakeBottom().setPower(1);
        gyroscope.drive(-0.4, 2);//Drive foreword
        robot.getIntakeBottom().setPower(0);
        gyroscope.rotate(170);
        gyroscope.drive(0.4, 4);
        robot.getJewelVerticle().setPosition(.15);//Land jewel arm into parking zone
        sleep(750);//wait for deceleration
    }
}