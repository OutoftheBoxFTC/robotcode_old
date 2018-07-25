package org.ftc7244.robotcontroller.programs.control_system_regression;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.ftc7244.robotcontroller.autonomous.drivers.PIDGyroscopeDrive;
import org.ftc7244.robotcontroller.hardware.Westcoast;
import org.ftc7244.robotcontroller.sensor.gyroscope.GyroscopeProvider;
import org.ftc7244.robotcontroller.sensor.gyroscope.RevIMUGyroscopeProvider;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by ftc72 on 7/25/2018.
 */

public class RegressionOpmode extends LinearOpMode {
    private Westcoast westcoast;
    private GyroscopeProvider gyroProvider;
    private PIDGyroscopeDrive gyro;
    private ExecutorService service;

    @Override
    public void runOpMode() throws InterruptedException {
        westcoast = new Westcoast(this);
        westcoast.init();
        westcoast.initServos();

        gyroProvider = new RevIMUGyroscopeProvider();

        gyro = new PIDGyroscopeDrive(westcoast, gyroProvider);

        service = Executors.newCachedThreadPool();

        waitForStart();

    }

}
