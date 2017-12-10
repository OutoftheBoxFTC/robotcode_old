package org.ftc7244.robotcontroller.programs.autonomous;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.ftc7244.datalogger.file.DataStorage;
import org.ftc7244.robotcontroller.autonomous.PIDAutonomous;
import org.ftc7244.robotcontroller.sensor.vuforia.ImageTransformProvider;


/**
 * Created by Eeshwar Laptop on 10/29/2017.
 */
@Autonomous(name = "Blue Left1")
public class BlueLeft extends PIDAutonomous {

    public void run(DataStorage storage) throws InterruptedException{
        robot.knockOverJewel(Color.RED);//Check color sensor
        robot.driveToInch(0.2, 12);
        sleep(1000);//Wait for gyro to calibrate
        gyroscope.rotate(90);//Rotate
        gyroscope.drive(0.2, 6.5);//Drive to glyph box
        robot.getSpring().setPosition(.5);//Spring out glyph
        robot.getIntakeBottom().setPower(1);//activate outtake
        gyroscope.drive(-0.2, 3);// Drive glyph into intake
        robot.getIntakeBottom().setPower(0);//disable outtake
        gyroscope.rotate(-170);//Rotate so back faces glyph box
        gyroscope.drive(-0.4, 3.5);//Drive glyph back into glyph box
        gyroscope.drive(0.4,2);//Drive foreword
        robot.getJewelVerticle().setPosition(.15);//Land jewel arm into parking zone
        sleep(750);//wait for deceleration
    }

    @Override
    protected Class<? extends PIDAutonomous> getChildClass() {
        return getClass();
    }
}