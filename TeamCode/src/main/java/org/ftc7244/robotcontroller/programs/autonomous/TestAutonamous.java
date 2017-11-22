package org.ftc7244.robotcontroller.programs.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.ftc7244.robotcontroller.autonomous.bases.RelicRecoveryPIDAutonamous;
import org.ftc7244.robotcontroller.sensor.gyroscope.RevIMUGyroscopeProvider;

/**
 * Created by FTC 7244 on 10/29/2017.
 */
@Autonomous(name = "Test90")
public class TestAutonamous extends RelicRecoveryPIDAutonamous{
    RevIMUGyroscopeProvider imu = new RevIMUGyroscopeProvider();
    @Override
    public void run() throws InterruptedException {
        imu.start(hardwareMap);
        waitForStart();
        gyroscope.rotate(90);
        while(opModeIsActive()) {
            imu.calibrate();
            telemetry.addData("Z", imu.getZ());
            telemetry.update();
        }
    }
}
