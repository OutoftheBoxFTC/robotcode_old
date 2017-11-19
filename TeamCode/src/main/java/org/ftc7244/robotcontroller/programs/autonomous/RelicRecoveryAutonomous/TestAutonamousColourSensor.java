package org.ftc7244.robotcontroller.programs.autonomous.RelicRecoveryAutonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.ftc7244.robotcontroller.autonomous.bases.RelicRecoveryPIDAutonamous;
import org.ftc7244.robotcontroller.sensor.gyroscope.RevIMUGyroscopeProvider;

/**
 * Created by FTC 7244 on 10/29/2017.
 */
@Autonomous(name = "Colour Sensor")
public class TestAutonamousColourSensor extends RelicRecoveryPIDAutonamous{
    RevIMUGyroscopeProvider imu = new RevIMUGyroscopeProvider();
    @Override
    public void run() throws InterruptedException {

        while(opModeIsActive()) {

        }
    }
}
