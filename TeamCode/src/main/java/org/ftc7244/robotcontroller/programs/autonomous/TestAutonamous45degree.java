package org.ftc7244.robotcontroller.programs.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.ftc7244.robotcontroller.autonomous.bases.RelicRecoveryPIDAutonamous;
import org.ftc7244.robotcontroller.hardware.Westcoast;
import org.ftc7244.robotcontroller.sensor.gyroscope.RevIMUGyroscopeProvider;

/**
 * Created by FTC 7244 on 10/29/2017.
 */
@Autonomous(name = "Test45")
public class TestAutonamous45degree extends RelicRecoveryPIDAutonamous{

    @Override
    public void run() throws InterruptedException {
        gyroscope.rotate(45);
    }
}
