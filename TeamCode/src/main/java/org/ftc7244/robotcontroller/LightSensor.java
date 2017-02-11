package org.ftc7244.robotcontroller;

import com.qualcomm.hardware.hitechnic.HiTechnicNxtLightSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.ftc7244.robotcontroller.autonomous.PIDAutonomous;

/**
 * Created by FTC 7244 on 2/11/2017.
 */
@Autonomous
public class LightSensor extends PIDAutonomous {
    @Override
    public void run() throws InterruptedException {
        HiTechnicNxtLightSensor sensor = robot.getTrailingLight();
        sensor.enableLed(true);
        while (!this.isStopRequested()) {
            telemetry.addLine(sensor + "");
            telemetry.update();
        }
        sensor.enableLed(false);
    }
}
