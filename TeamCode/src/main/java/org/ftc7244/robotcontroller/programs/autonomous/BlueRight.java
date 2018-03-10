package org.ftc7244.robotcontroller.programs.autonomous;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.ftc7244.robotcontroller.autonomous.ControlSystemAutonomous;

/**
 * Created by BeaverDuck on 12/1/17.
 */
@Autonomous(name = "Blue Right")
public class BlueRight extends ControlSystemAutonomous {
    @Override
    public void run() throws InterruptedException {
        RelicRecoveryVuMark image = imageProvider.getImageReading();
        while(image == RelicRecoveryVuMark.UNKNOWN){
            image = imageProvider.getImageReading();
            sleep(50);
        }
        telemetry.addData("Image", image);
        telemetry.update();
        robot.knockOverJewel(Color.RED);//Check color sensor
        robot.getIntakeTop().setPower(-1);
        robot.getIntakeBottom().setPower(-1);
        robot.driveToInch(.3, 30);//Drive off balancing stone
        robot.getSpring().setPosition(0.5);//Spring out glyph
        sleep(750);
        robot.getIntakeServo().setPosition(0.8);
        gyroscopePID.rotate(90);//Rotate to face glyph
        gyroscopePID.drive(1, 28);
        robot.getIntakeServo().setPosition(0.2);
        sleep(1000);
        robot.getIntakeLift().setPower(1);
        sleep(100);
        robot.getIntakeLift().setPower(0.1);
        sleep(150);//Let block come into intake
        gyroscopePID.drive(-0.75, 3);
        switch (image){
            case RIGHT:
                gyroscopePID.rotate(62.5);
                gyroscopePID.drive(0.5, 47);
                break;
            case LEFT:
                gyroscopePID.rotate(79);
                gyroscopePID.drive(0.5, 36);
                break;
            default:
                gyroscopePID.rotate(68);
                gyroscopePID.drive(0.5, 42);
        }
        robot.getIntakeBottom().setPower(1);
        robot.getIntakeTop().setPower(1);
        gyroscopePID.drive(-0.5, 11);
        gyroscopePID.drive(0.5, 9);
        gyroscopePID.drive(-1, 13);
    }
}
