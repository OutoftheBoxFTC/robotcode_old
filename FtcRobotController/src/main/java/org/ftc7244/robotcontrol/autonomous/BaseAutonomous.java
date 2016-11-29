package org.ftc7244.robotcontrol.autonomous;

import android.hardware.SensorManager;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;

import org.ftc7244.robotcontrol.Westcoast;
import org.ftc7244.robotcontrol.controllers.PIDController;
import org.ftc7244.robotcontrol.controllers.QueuePIDController;
import org.ftc7244.robotcontrol.sensor.GyroscopeProvider;

import java.util.Arrays;

import static android.content.Context.SENSOR_SERVICE;

/**
 * Created by OOTB on 10/16/2016.
 */
@Autonomous
public class BaseAutonomous extends LinearOpMode {

    public static final int INTERVAL_PID = 50;

    private Westcoast robot;
    private PIDController controller;
    private GyroscopeProvider provider;

    public BaseAutonomous() {
        super();
        this.robot = new Westcoast(this);
        this.controller = new PIDController(50, 0.03, 0, 0);
        this.provider = new GyroscopeProvider() {
            @Override
            public void onUpdate() {
                double update = 0;
                if (isStarted()) {
                    update = controller.update(this.getX());
                    robot.getDriveLeft().setPower(.5);
                    robot.getDriveRight().setPower(.5 - update);
                }
                RobotLog.ii("TESTER", "PID: " + update + " Gyro: " + this.getX());
            }
        };
    }



    @Override
    public void runOpMode() throws InterruptedException {
        //Setup the motors for our robot
        SensorManager manager = (SensorManager) hardwareMap.appContext.getSystemService(SENSOR_SERVICE);
        provider.start(manager, INTERVAL_PID);
        robot.init();


        provider.setXToZero();
        controller.setTarget(provider.getX());
        waitForStart();
        //Dont start till the play button is clicked
        try {
            sleep(5000);
        } finally {
            provider.stop();
        }
    }
}
