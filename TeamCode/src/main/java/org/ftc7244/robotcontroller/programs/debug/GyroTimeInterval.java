package org.ftc7244.robotcontroller.programs.debug;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.ftc7244.robotcontroller.autonomous.PIDAutonomous;

/**
 * Created by BeaverDuck on 11/24/17.
 */

@Autonomous(name = "Gyro Debug")
public class GyroTimeInterval extends PIDAutonomous {
    @Override
    public void run() throws InterruptedException {
        double lastReading = -1, iterations = 0;
        long lastTime = System.currentTimeMillis(), total = 0;
        robot.drive(-.5, .5);
        while (!isStopRequested()){
            double reading = gyroProvider.getZ();
            if(reading != lastReading) {
                if (lastReading != -1) {
                    total += System.currentTimeMillis() - lastTime;
                    iterations++;
                }
                lastReading = reading;
                lastTime = System.currentTimeMillis();
            }
            telemetry.addData("Avg", total/iterations);
            telemetry.update();
        }
    }
}
