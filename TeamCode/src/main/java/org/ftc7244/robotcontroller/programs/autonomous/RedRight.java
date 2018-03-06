package org.ftc7244.robotcontroller.programs.autonomous;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.ftc7244.robotcontroller.autonomous.ControlSystemAutonomous;
import org.ftc7244.robotcontroller.hardware.Westcoast;

@Autonomous(name = "Red Right")
public class RedRight extends ControlSystemAutonomous {

    public void run() throws InterruptedException{
        RelicRecoveryVuMark image = imageProvider.getImageReading();
        while(image == RelicRecoveryVuMark.UNKNOWN){
            image = imageProvider.getImageReading();
            sleep(50);
        }
        knockOverJewel(Color.BLUE);//Check color sensor
        robot.getIntakeTop().setPower(-1);
        robot.driveToInch(.3, 36);//Drive off balancing stone
        robot.getSpring().setPosition(0.5);//Spring out glyph
        sleepWhile(750, RAISE_INTAKE_TO_HOME);
        robot.getIntakeServo().setPosition(Westcoast.INTAKE_OPEN);
        gyroscopePID.rotate(45);
        robot.getIntakeBottom().setPower(-1);
        gyroscopePID.driveWithLimitSwitch(1, 23, robot.getBottomIntakeSwitch());//Drive to glyph pit
        robot.getIntakeServo().setPosition(Westcoast.INTAKE_CLOSE);
        sleepWhile(100, LOWER_INTAKE_TO_MIN);
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
        sleepWhile(100, RAISE_INTAKE_TO_HOME);
        robot.getIntakeBottom().setPower(1);
        robot.getIntakeTop().setPower(1);
        gyroscopePID.drive(-0.5, 8);
        gyroscopePID.drive(1, 6);
        gyroscopePID.drive(-1, 58);
        gyroscopePID.rotate(180);
        robot.getIntakeTop().setPower(0);
        robot.getIntakeBottom().setPower(0);
        extendRelicArm();
    }
}