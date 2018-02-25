package org.ftc7244.robotcontroller.programs.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.ftc7244.datalogger.Logger;
import org.ftc7244.robotcontroller.autonomous.ControlSystemAutonomous;

/**
 * Created by ftc72 on 2/21/2018.
 */

@Autonomous(name = "ACS 90")
public class ACS90 extends ControlSystemAutonomous {

    @Override
    public void run() throws InterruptedException {
        double target = -90, offset = gyroProvider.getZ(), basePower=3.5, slowRange=60, stopOffset=10;
        long lastTime = System.currentTimeMillis();
        while (opModeIsActive()&&System.currentTimeMillis()-lastTime<=6000 && Math.abs(target-(gyroProvider.getZ()-offset+stopOffset))>=5){
            if(Math.abs(target-(gyroProvider.getZ()-offset))>slowRange) {
                robot.drive(basePower, -basePower);
                telemetry.addData("Power", basePower);
                Logger.getInstance().queueData("Power", basePower);
            }
            else {
                double power = basePower*(Math.abs(target-(gyroProvider.getZ()-offset))/slowRange);
                telemetry.addData("Power", basePower*(Math.abs(target-(gyroProvider.getZ()-offset))/slowRange));
                robot.drive(power, -power);
                Logger.getInstance().queueData("Power", power);
            }
            Logger.getInstance().queueData("Time", 6000-(System.currentTimeMillis()-lastTime)).
                    queueData("Offset", Math.abs(target-(gyroProvider.getZ()-offset))).
                    queueData("Encoder Avg", robot.getDriveEncoderAverage());

            telemetry.addData("Time", 6000-(System.currentTimeMillis()-lastTime));
            telemetry.addData("Offset", Math.abs(target-(gyroProvider.getZ()-offset)));
            telemetry.addData("Encoder Avg", robot.getDriveEncoderAverage());
            telemetry.addData("Offset", offset);
            telemetry.update();
        }
        robot.drive(0, 0);
    }
}
