package org.ftc7244.robotcontroller.programs.debug;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.ftc7244.robotcontroller.autonomous.PIDAutonomous;

/**
 * Created by FTC 7244 on 4/3/2017.
 */
@Autonomous(name = "Color Debug")
public class ColorDebug extends PIDAutonomous {
    @Override
    public void run() throws InterruptedException {
        while (!this.isStopRequested()) {
            telemetry.addLine("Red: " + robot.getBeaconSensor().red() + " Offseted: " + (robot.getBeaconSensor().red() - robot.getRedOffset()));
            telemetry.addLine("Blue: " + robot.getBeaconSensor().blue() + " Offseted: " + (robot.getBeaconSensor().blue() - robot.getBlueOffset()));
            telemetry.update();
        }
    }
}
