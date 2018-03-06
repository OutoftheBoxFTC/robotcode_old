package org.ftc7244.robotcontroller.programs.autonomous;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.ftc7244.robotcontroller.autonomous.ControlSystemAutonomous;

@Autonomous(name = "Blue Right")
public class BlueRight extends ControlSystemAutonomous {
    @Override
    public void run() throws InterruptedException {
        knockOverJewel(Color.RED);//Check color sensor
        robot.getIntakeTop().setPower(-1);
        robot.driveToInch(.2, 31);//Drive off balancing stone
        gyroscopePID.rotate(0);
        sleep(200);
        robot.getSpring().setPosition(0.5);//Spring out glyph
        RelicRecoveryVuMark image = imageProvider.getImageReading();
        telemetry.addData("Image", image);
        telemetry.update();
        robot.getIntakeServo().setPosition(0.2);
        gyroscopePID.rotate(90);//Rotate to face glyph pit
        robot.getIntakeBottom().setPower(-1);
        robot.driveIntakeVertical(0.5);
        sleep(200);
        robot.driveIntakeVertical(0);
        gyroscopePID.drive(0.6, 20);//Drive to glyph pit
        gyroscopePID.drive(0.6, 25);// Drive into glyph pit
        //0.2: 0.75
        robot.getIntakeServo().setPosition(0.8);
        gyroscopePID.drive(-0.6, 0.6);
        switch(image) {
            case RIGHT:
                gyroscopePID.rotate(72);
                gyroscopePID.drive(0.85, 38.2);
                break;
            case LEFT:
                gyroscopePID.rotate(852);
                gyroscopePID.drive(0.7, 36);
                break;
            default:
                gyroscopePID.rotate(82);
                gyroscopePID.drive(0.85, 39);
                break;
        }
        robot.getIntakeBottom().setPower(1);
        robot.getIntakeTop().setPower(1);
        gyroscopePID.drive(-0.6, 10);
        gyroscopePID.drive(0.6, 4);
        gyroscopePID.drive(-0.6, 5);
    }
}
