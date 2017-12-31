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
        robot.getIntakeServo().setPosition(0.5);
        robot.knockOverJewel(Color.BLUE);//Check color sensor
        robot.driveToInch(.2, 28);//Drive off balancing stone
        gyroscope.rotate(-gyroProvider.getZ());//Re-Center the robot
    }
}
