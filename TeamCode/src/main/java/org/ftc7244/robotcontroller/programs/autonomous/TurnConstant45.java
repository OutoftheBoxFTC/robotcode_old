package org.ftc7244.robotcontroller.programs.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.ftc7244.robotcontroller.autonomous.ControlSystemAutonomous;

@Autonomous(name = "Turn 45 Constant")
public class TurnConstant45 extends ControlSystemAutonomous {
    @Override
    public void run() throws InterruptedException {
        turnTo(45);
    }

    private void turnTo(double target){
        double shutoffRange = 0.5, basePower = 1,
                error = gyroProvider.getZ()-target,
                polarity = error<0?-1:1;
        robot.drive(polarity*basePower, -polarity*basePower);
        while (polarity*(gyroProvider.getZ()-target)<shutoffRange);
        robot.drive(0, 0);
    }
}