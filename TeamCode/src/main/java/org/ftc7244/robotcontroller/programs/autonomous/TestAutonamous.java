package org.ftc7244.robotcontroller.programs.autonomous;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;
import org.ftc7244.datalogger.Logger;
import org.ftc7244.robotcontroller.autonomous.bases.RelicRecoveryPIDAutonamous;
import org.ftc7244.robotcontroller.hardware.RelicRecoveryWestcoast;
import org.ftc7244.robotcontroller.sensor.gyroscope.RevIMUGyroscopeProvider;

import java.util.Locale;

/**
 * Created by FTC 7244 on 10/29/2017.
 */
@Autonomous(name = "Test")
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
