package org.ftc7244.robotcontroller.programs.debug;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.ftc7244.robotcontroller.sensor.gyroscope.PhoneGyroscopeProvider;

/**
 * Created by Eeshwar Laptop on 10/27/2017.
 */
@Autonomous(name="gyro")
public class GyroInputTest extends LinearOpMode {
    public void runOpMode(){
        PhoneGyroscopeProvider gyro = new PhoneGyroscopeProvider();
        gyro.calibrate();
        gyro.start(hardwareMap);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        telemetry.addData("Val X:", gyro.getX());
        telemetry.addData(" Val Y:",gyro.getY());
        telemetry.addData(" Val Z:", gyro.getZ());
        telemetry.update();
    }
}
