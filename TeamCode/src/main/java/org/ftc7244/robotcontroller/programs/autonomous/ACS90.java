package org.ftc7244.robotcontroller.programs.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.ftc7244.datalogger.Logger;
import org.ftc7244.robotcontroller.autonomous.ControlSystemAutonomous;

/**
 * Created by ftc72 on 2/21/2018.
 */

public class ACS90 extends ControlSystemAutonomous {

    @Override
    public void run() throws InterruptedException {
        turn(270);
    }
    private double lastTarget = 0;
    private void turn(double target){
        double basePower=0.3, slowRotationalRange=60, tolerance = 1, minimumPower = 0.05;
        long lastTime = System.currentTimeMillis();
        while (opModeIsActive()&&System.currentTimeMillis()-lastTime<=6000 && !isFinished(target, tolerance)){
            double power = determinePower(target, slowRotationalRange, basePower, minimumPower);
            if(remainingAngle(target)>0){
                robot.drive(-power, power);
            }
            else {
                robot.drive(power, -power);
            }
            Logger.getInstance().queueData("Time", 6000-(System.currentTimeMillis()-lastTime)).
                    queueData("Offset", Math.abs(target-(gyroProvider.getZ()))).
                    queueData("Encoder Avg", robot.getDriveEncoderAverage()).
                    queueData("Power", power);
            telemetry.addData("Offset", Math.abs(target-(gyroProvider.getZ())));
            telemetry.update();
        }
        robot.drive(0, 0);
        lastTarget += target;
    }

    private double determinePower(double target, double slowRotationalRange, double basePower, double minimumPower){
        double power = basePower;
        double remainingAngle = Math.abs(remainingAngle(target));
        if(Math.abs(remainingAngle)<=slowRotationalRange){
            power = basePower *(remainingAngle/slowRotationalRange);
        }
        return power<minimumPower?minimumPower:power;
    }

    private double remainingAngle(double target){
        return target-(gyroProvider.getZ()-lastTarget);
    }


    private boolean isFinished(double target, double stopRange){
        return Math.abs(remainingAngle(target))<=stopRange;
    }
}