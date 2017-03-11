package org.ftc7244.robotcontroller.programs.debug;

import com.qualcomm.hardware.hitechnic.HiTechnicNxtLightSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.ftc7244.robotcontroller.autonomous.PIDAutonomous;

/**
 * Created by FTC 7244 on 2/11/2017.
 */
@Autonomous(name="Light Sensor Debug")
public class LightDebug extends PIDAutonomous {
    @Override
    public void run() throws InterruptedException {
        HiTechnicNxtLightSensor trailing = robot.getTrailingLight();
        HiTechnicNxtLightSensor leading = robot.getLeadingLight();
        leading.enableLed(true);
        trailing.enableLed(true);
        leading.enableLed(true);
        while (!this.isStopRequested()) {
            telemetry.addLine("Light Leading: " + leading);
            telemetry.addLine("Light Trailing: " + trailing);
            telemetry.update();
        }
        leading.enableLed(false);
        trailing.enableLed(false);
    }
}
