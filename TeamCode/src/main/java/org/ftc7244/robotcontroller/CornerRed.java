package org.ftc7244.robotcontroller;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.ftc7244.robotcontroller.autonomous.PIDAutonomous;

@Autonomous(name = "Corner Red", group = "Red")
public class CornerRed extends PIDAutonomous {

    @Override
    public void run() throws InterruptedException {
        sleep(8000);
        gyroscope.drive(-0.5, 5);
        gyroscope.rotate(45);
        gyroscope.drive(-0.5, 5);
        gyroscope.rotate(77);
        sleep(1000);
        robot.shootLoop(2, 1500);
        gyroscope.rotate(-77);
        gyroscope.drive(-0.5, 36);
    }
}
