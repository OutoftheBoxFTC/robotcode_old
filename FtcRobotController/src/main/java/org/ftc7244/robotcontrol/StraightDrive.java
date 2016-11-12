package org.ftc7244.robotcontrol;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.RobotLog;

import org.ftc7244.robotcontrol.core.EncoderBaseAutonomous;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by OOTB on 11/11/2016.
 */

@Autonomous
public class StraightDrive extends EncoderBaseAutonomous {
    private ExecutorService service;

    public StraightDrive() {
        service = Executors.newCachedThreadPool();
    }

    @Override
    public void run() throws InterruptedException {
        int red = 0, blue = 0, green = 0;
        while (true) {
            if (red != robot.getBeaconSensor().red() || blue != robot.getBeaconSensor().blue() || green != robot.getBeaconSensor().green()) {
                blue = robot.getBeaconSensor().blue();
                red = robot.getBeaconSensor().red();
                green = robot.getBeaconSensor().green();
                RobotLog.ii("Color", "Blue: " + red + " Red: " + blue + " Green " + green);
            }
            idle();
        }
        /*service.execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    RobotLog.ii("Color", "Blue: " +robot.getBeaconSensor().blue() + " Red: " + robot.getBeaconSensor().red());
                }
            }
        });
        drive(.45, 50, 50);;*/
    }
}
