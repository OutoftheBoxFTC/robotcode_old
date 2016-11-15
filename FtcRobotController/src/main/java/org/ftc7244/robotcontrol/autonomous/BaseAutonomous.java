package org.ftc7244.robotcontrol.autonomous;

import android.hardware.SensorManager;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;

import org.ftc7244.robotcontrol.Westcoast;
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
    private QueuePIDController controller;
    private GyroscopeProvider provider;

    public BaseAutonomous() {
        super();
        this.robot = new Westcoast(this);
        this.controller = new QueuePIDController(50, 1, 0, 0);
        this.provider = new GyroscopeProvider() {
            @Override
            public void onUpdate() {
                RobotLog.ii("Test", this.getX() + ":" + this.getY() + ":" + this.getZ());
                telemetry.update();
            }
        };
    }



    @Override
    public void runOpMode() throws InterruptedException {
        //Setup the motors for our robot
        SensorManager manager = (SensorManager) hardwareMap.appContext.getSystemService(SENSOR_SERVICE);
        provider.start(manager, INTERVAL_PID);
        robot.init();

        //Dont start till the play button is clicked
        waitForStart();
        provider.stop();

        //drive(90, 60);
    }

    public void drive(int degs, int time) {
        //controller.setTarget(Math.toRadians(degs));
        controller.reset();
        ElapsedTime runtime = new ElapsedTime();

        while (runtime.seconds() < time) {
            if (controller.hasValue()) {
                double power = controller.getNextValue();
                robot.getDriveLeft().setPower(power);
                robot.getDriveRight().setPower(power);
            }
        }
    }
}
