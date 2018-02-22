package org.ftc7244.robotcontroller.programs.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.ftc7244.robotcontroller.autonomous.PIDAutonomous;

/**
 * Created by ftc72 on 2/21/2018.
 */

@Autonomous(name = "Alternate Control System")
public class AlternateControlSystem extends PIDAutonomous {

    @Override
    public void run() throws InterruptedException {
        double target = -90, offset = gyroProvider.getZ(), basePower=0.5, slowRange=60;
        long lastTime = System.currentTimeMillis();
        while (opModeIsActive()&&System.currentTimeMillis()-lastTime<=6000) {
            if(Math.abs(target-(gyroProvider.getZ()-offset))>slowRange)
                robot.drive(basePower, -basePower);
            else {
                double power = basePower*(Math.abs(target-(gyroProvider.getZ()-offset))/slowRange);
                robot.drive(power, -power);
            }
        }
    }
}
