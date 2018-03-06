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
        gyroscopePID.rotate(45);//Rotate to glyph pit
        robot.getIntakeBottom().setPower(-1);//Start intake inwards
        gyroscopePID.driveWithLimitSwitch(1, 23, robot.getBottomIntakeSwitch());//Drive until take in glyph
        robot.getIntakeServo().setPosition(Westcoast.INTAKE_CLOSE);//Close intake on glyph
        gyroscopePID.setControlSubTask(LOWER_INTAKE_TO_MIN).drive(-0.75, 10);//Lower intake while driving out of glyph pit
        gyroscopePID.setControlSubTask(RAISE_INTAKE_TO_HOME);
        switch(image){
            case LEFT:
                gyroscopePID.rotate(-154);
                gyroscopePID.drive(0.5, 51);
                break;
            case RIGHT:
                gyroscopePID.rotate(-171.5);
                gyroscopePID.drive(1, 48);
                break;
            default:
                gyroscopePID.rotate(-161);
                gyroscopePID.drive(1, 45);
        }//Rotate and drive to correct column
        robot.getIntakeBottom().setPower(1);
        robot.getIntakeTop().setPower(1);//Outtake blocks
        gyroscopePID.drive(-0.5, 8);//Drive out
        gyroscopePID.drive(1, 6);//Drive back in again
        gyroscopePID.drive(-1, 58);//Drive out back to glyph pit
        gyroscopePID.rotate(180);//Rotate so back faces cryptobox
        robot.getIntakeTop().setPower(0);
        robot.getIntakeBottom().setPower(0);//Shut off intake
        extendRelicArm();//umm... idk
    }
}