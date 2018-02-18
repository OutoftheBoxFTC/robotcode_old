package org.ftc7244.robotcontroller.programs.autonomous;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.ftc7244.robotcontroller.autonomous.PIDAutonomous;

/**
 * Created by BeaverDuck on 12/1/17.
 */
@Autonomous(name = "Blue Right")
public class BlueRight extends PIDAutonomous {
    @Override
    public void run() throws InterruptedException {
        robot.knockOverJewel(Color.RED);//Check color sensor
        robot.getIntakeTop().setPower(-1);
        robot.driveToInch(.2, 31);//Drive off balancing stone
        gyroscope.rotate(0);
        sleep(200);
        robot.getSpring().setPosition(0.5);//Spring out glyph
        RelicRecoveryVuMark image = imageProvider.getImageReading();
        telemetry.addData("Image", image);
        telemetry.update();
        sleep(1000);
        robot.getIntakeServo().setPosition(0.2);
        gyroscope.rotate(90);//Rotate to face glyph pit
        robot.getIntakeBottom().setPower(-1);
        robot.driveIntakeVertical(0.5);
        sleep(200);
        robot.driveIntakeVertical(0);
        gyroscope.drive(0.6, 20);//Drive to glyph pit
        gyroscope.drive(0.6, 25);// Drive into glyph pit
        //0.2: 0.75
        robot.getIntakeServo().setPosition(0.8);
        gyroscope.drive(-0.6, 0.6);
        switch(image) {
            case RIGHT:
                gyroscope.rotate(72);
                gyroscope.drive(0.85, 38.2);
                break;
            case LEFT:
                gyroscope.rotate(852);
                gyroscope.drive(0.7, 36);
                break;
            default:
                gyroscope.rotate(82);
                gyroscope.drive(0.85, 39);
                break;
        }
        robot.getIntakeBottom().setPower(1);
        robot.getIntakeTop().setPower(1);
        gyroscope.drive(-0.6, 10);
        gyroscope.drive(0.6, 4);
        gyroscope.drive(-0.6, 5);
    }
}
