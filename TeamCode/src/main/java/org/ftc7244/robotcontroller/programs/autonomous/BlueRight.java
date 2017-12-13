package org.ftc7244.robotcontroller.programs.autonomous;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.ftc7244.robotcontroller.autonomous.PIDAutonomous;

/**
 * Created by BeaverDuck on 12/1/17.
 */
@Autonomous(name = "Blue Right")
public class BlueRight extends PIDAutonomous {
    @Override
    public void run() throws InterruptedException {
        robot.knockOverJewel(Color.RED);//Knock off jewel
      //  robot.driveToInch(0.2, 12);//Drive off balancing stone
        sleep(1000);//Wait for gyro to calibrate
        gyroscope.rotate(-90);//Rotate parallel to wall
        gyroscope.drive(0.2, 24);//Drive in direction of cryptobox
        gyroscope.rotate(-90);//Turn towards glyph box
        gyroscope.drive(0.2, 24);//Drive towards cryptobox
        robot.getSpring().setPosition(0.5);//
        robot.getIntakeBottom().setPower(1);
        gyroscope.drive(-0.2, 3);// Drive glyph into intake
        robot.getIntakeBottom().setPower(0);//disable outtake
        gyroscope.rotate(-180);//Rotate so back faces glyph box
        gyroscope.drive(-0.4, 3.5);//Drive glyph back into glyph box
        gyroscope.drive(0.4,2);//Drive foreword
        robot.getJewelVerticle().setPosition(.15);//Land jewel arm into parking zone
        sleep(750);//wait for deceleration
    }
}
