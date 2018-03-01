package org.ftc7244.robotcontroller.programs.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.ftc7244.datalogger.Logger;
import org.ftc7244.robotcontroller.autonomous.ControlSystemAutonomous;

/**
 * Created by ftc72 on 2/21/2018.
 */

@Autonomous(name = "ACS 45")
public class ACS45 extends ControlSystemAutonomous {

    @Override
    public void run() throws InterruptedException {
        turn(45);
    }

    private void turn(double target){
        double offset = gyroProvider.getZ(), basePower=0.4, slowRange=45, stopOffset=3, polarity = -target<0?-1:1, stopRange = 2;
        long lastTime = System.currentTimeMillis();
        while (opModeIsActive()&&System.currentTimeMillis()-lastTime<=6000 && Math.abs(target-(gyroProvider.getZ()-offset))>=stopRange){
            if(Math.abs(target-(gyroProvider.getZ()-offset))>slowRange) {
                robot.drive(basePower, -basePower);
                telemetry.addData("Power", basePower);
                Logger.getInstance().queueData("Power", basePower*100);
            }
            else {
                double power = -basePower*(target-(gyroProvider.getZ()-offset+stopOffset*polarity))/slowRange;
                robot.drive(power, -power);
                Logger.getInstance().queueData("Power", power*100);
            }
            Logger.getInstance().queueData("Time", 6000-(System.currentTimeMillis()-lastTime)).
                    queueData("Offset", Math.abs(target-(gyroProvider.getZ()-offset))).
                    queueData("Encoder Avg", robot.getDriveEncoderAverage());
            telemetry.addData("Offset", Math.abs(target-(gyroProvider.getZ()-offset)));
            telemetry.update();
        }
        robot.drive(0, 0);
    }
}